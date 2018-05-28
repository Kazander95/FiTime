package co.kazander;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.icu.util.Calendar;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Random;

import co.paulburke.android.itemtouchhelperdemo.R;
import co.paulburke.android.itemtouchhelperdemo.AlarmReceiver;

/**
 * Created by Kazander on 5/10/2017.
 */

public class Alarm {

    TimePicker myTimePicker;
    Button buttonstartSetDialog;
    TextView textAlarmPrompt;
    LinearLayout contextHolder;
    KeyboardEditText labelText;
    TextView classLabel;
    String mLabelText ="Custom Label";
    int colorHolder;
    Context holdingBase;
    String mPromptText = "NOT SET";
    PendingIntent pendingIntent;
    Intent intent;

    TimePickerDialog timePickerDialog;

    public final int RQS_1 = (int) System.currentTimeMillis();

    public Alarm(Context context) {
        colorHolder = getRandomColor();
        holdingBase = context;

    }

    public void reinflate(final LinearLayout lltext, final LinearLayout ll, View holder){

        classLabel = (TextView) lltext.findViewById(R.id.classLabel);
        classLabel.setText("Alarm");
        classLabel.setTextSize(15);
        classLabel.setTextColor(Color.WHITE);


        labelText = (KeyboardEditText) lltext.findViewById(R.id.label);
        labelText.setText(mLabelText);
        labelText.setTextSize(12);
        labelText.setFocusable(false);
        labelText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        labelText.setTextColor(Color.WHITE);

        labelText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                labelText.setFocusableInTouchMode(true);

                return false;
            }
        });

        labelText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE)
                {
                    // Hide cursor
                    labelText.setFocusable(false);

                    // Set EditText to be focusable again
                    labelText.setFocusable(true);
                    labelText.setFocusableInTouchMode(true);

                    mLabelText = labelText.getText().toString();
                }

                InputMethodManager inputManager = (InputMethodManager)
                        lltext.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.toggleSoftInput(0, 0);

                if(labelText.getText().toString().isEmpty() == true) {
                    labelText.setText("Label");
                }

                return false;
            }
        });

        contextHolder = ll;
        textAlarmPrompt = new TextView(lltext.getContext());
        textAlarmPrompt.setTextColor(Color.WHITE);
        textAlarmPrompt.setTextSize(18);
        textAlarmPrompt.setText(mPromptText);


        buttonstartSetDialog = new Button(lltext.getContext());
        buttonstartSetDialog.setText("Set Alarm");
        buttonstartSetDialog.getBackground().setColorFilter(colorHolder, PorterDuff.Mode.SRC_ATOP);

        LinearLayout.LayoutParams leftmostLayout = new LinearLayout.LayoutParams(300, 175);
        leftmostLayout.setMargins(20, 0, 0, 0);
        LinearLayout.LayoutParams longLayout = new LinearLayout.LayoutParams(400, 175);
        longLayout.setMargins(0, 0, 0, 0);

        ll.addView(textAlarmPrompt, leftmostLayout);
        ll.addView(buttonstartSetDialog, longLayout);
        buttonstartSetDialog.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                openTimePickerDialog(false);
            }});

    }


    private void openTimePickerDialog(boolean is24r){
        Calendar calendar = Calendar.getInstance();

        timePickerDialog = new TimePickerDialog(
                contextHolder.getContext(),
                onTimeSetListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                is24r);
        timePickerDialog.setTitle("Set Alarm Time");

        timePickerDialog.show();

    }

    TimePickerDialog.OnTimeSetListener onTimeSetListener
            = new TimePickerDialog.OnTimeSetListener(){

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            Calendar calNow = Calendar.getInstance();
            Calendar calSet = (Calendar) calNow.clone();

            calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calSet.set(Calendar.MINUTE, minute);
            calSet.set(Calendar.SECOND, 0);
            calSet.set(Calendar.MILLISECOND, 0);

            if(calSet.compareTo(calNow) <= 0){
                //Today Set time passed, count to tomorrow
                calSet.add(Calendar.DATE, 1);
            }

            setAlarm(calSet);
        }};

    private void setAlarm(Calendar targetCal){

        String hourString;
        String minuteString = Integer.toString(targetCal.get(Calendar.MINUTE));
        int hour = targetCal.get(Calendar.HOUR_OF_DAY);
        String amPmString;
        if(hour >= 12){
            amPmString = "PM";
        }
        else{
            amPmString = "AM";
        }
        hour = hour%12;
        if(hour == 0){hour=12;}
        hourString = Integer.toString(hour);

        textAlarmPrompt.setText("@" + hourString + ":" + minuteString + amPmString);
        mPromptText=textAlarmPrompt.getText().toString();

        intent = new Intent(holdingBase, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(holdingBase, RQS_1, intent, 0);
        AlarmManager alarmManager = (AlarmManager) holdingBase.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);

    }

    public int getRandomColor(){
        Random rnd = new Random();
        return Color.argb(80, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    public void stopIt(){
        if(pendingIntent != null) {
            pendingIntent.getBroadcast(holdingBase, RQS_1, intent, 0).cancel();
        }
    }
}
