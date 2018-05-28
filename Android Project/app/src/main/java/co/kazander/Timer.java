package co.kazander;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.text.InputType;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.Random;

import co.paulburke.android.itemtouchhelperdemo.MainActivity;
import co.paulburke.android.itemtouchhelperdemo.R;

import static android.R.id.message;
import static android.support.design.R.attr.title;
import static android.support.v7.appcompat.R.attr.icon;

/**
 * Created by Kazander on 4/12/2017.
 */

public class Timer{

    CountDownTimer countDownTimer; //function for countdown timer in android

    boolean theWorld = false; //bool to check if time has started or stopped

    boolean pullFromFakeString = false;

    String fakeString = "";

    static int setMinutes = 0;

    static int setSeconds = 0;

    static boolean timeSet;

    static boolean wait = true;

    boolean timerWent;

    String mLabelText = "Custom Label";

    String savedCountDownTime = "00:00:00";

    String savedPlayState = "►";

    int colorHolder;

    TextView countdownTimeText;










    public Timer(){

        colorHolder = getRandomColor();

    }

    public int getRandomColor(){
        Random rnd = new Random();
        return Color.argb(75, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    public static class TimerDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            LayoutInflater inflater = getActivity().getLayoutInflater();

            View mView = inflater.inflate(R.layout.timer_dialog,null);

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setView(mView);

            final NumberPicker minutePicker = (NumberPicker) mView.findViewById(R.id.numberPicker);
            minutePicker.setMinValue(0);
            minutePicker.setMaxValue(59);
            minutePicker.setValue(setMinutes);
            final NumberPicker secondPicker = (NumberPicker) mView.findViewById(R.id.numberPicker2);
            secondPicker.setMinValue(0);
            secondPicker.setMaxValue(59);
            secondPicker.setValue(setSeconds);


            builder.setTitle(R.string.settime)
                    .setPositiveButton(R.string.set, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            setSeconds(secondPicker.getValue());
                            setMinutes(minutePicker.getValue());
                            timeSet = true;
                            wait = false;
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            TimerDialogFragment.this.getDialog().cancel();
                        }
                    });
            return builder.create();
        }


    }



    public void reinflate(final LinearLayout lltext, final LinearLayout ll, final View holder){

        if(timerWent){
            holder.setBackgroundColor(Color.RED);
        }


        countdownTimeText = new TextView(lltext.getContext());
        countdownTimeText.setTextColor(Color.WHITE);



        countdownTimeText.setGravity(Gravity.CENTER);
        countdownTimeText.setText(savedCountDownTime);

        class mycountDownTimer extends CountDownTimer {
            public mycountDownTimer(long timeVal, long interval) {
                super(timeVal, interval);
            }

            @Override
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                //formats the time in hh:mm:ss
                countdownTimeText.setText(String.format("%02d:%02d:%02d", seconds / 3600,
                        (seconds % 3600) / 60, (seconds % 60)));
                savedCountDownTime = String.format("%02d:%02d:%02d", seconds / 3600,
                        (seconds % 3600) / 60, (seconds % 60));


            }

            @Override
            public void onFinish() {
                Vibrator v = (Vibrator) ll.getContext().getSystemService(Context.VIBRATOR_SERVICE);

                countdownTimeText.setText("Times up");
                savedCountDownTime = "Times up";
                pullFromFakeString = false;
                v.vibrate(1000);

                holder.setBackgroundColor(Color.RED);
                timerWent=true;
                //vibrate phone for 1000ms

                //Define Notification Manager
                NotificationManager notificationManager = (NotificationManager) ll.getContext().getSystemService(Context.NOTIFICATION_SERVICE);

                //Define sound URI
                Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                Intent notificationIntent = new Intent(ll.getContext(), MainActivity.class);

                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                PendingIntent intent = PendingIntent.getActivity(ll.getContext(), 0,
                        notificationIntent, 0);

                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ll.getContext())
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("Time's Up!")
                        .setContentText("Time is up for a timer in FiTime!")
                        .setSound(soundUri) //This sets the sound to play
                        .setContentIntent(intent);



                //Display notification
                notificationManager.notify(0, mBuilder.build());
            }
        }



        final TextView classLabel;
        final KeyboardEditText labelText;
        classLabel = (TextView) lltext.findViewById(R.id.classLabel);
        classLabel.setText("Timer");
        classLabel.setTextSize(15);
        classLabel.setTextColor(Color.WHITE);


        labelText = (KeyboardEditText) lltext.findViewById(R.id.label);
        labelText.setText(mLabelText);
        labelText.setTextSize(12);
        labelText.setFocusable(false);
        labelText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        labelText.setTextColor(Color.WHITE);


        final Button startTimer = new Button(ll.getContext());
        startTimer.getBackground().setColorFilter(colorHolder, PorterDuff.Mode.SRC_ATOP);
        startTimer.setText(savedPlayState);

        final Button resetTimer = new Button(ll.getContext());
        resetTimer.getBackground().setColorFilter(colorHolder, PorterDuff.Mode.SRC_ATOP);
        resetTimer.setText("RESET");

        final Button editTimer = new Button(ll.getContext());
        editTimer.getBackground().setColorFilter(colorHolder, PorterDuff.Mode.SRC_ATOP);
        editTimer.setText("SET");

        LinearLayout.LayoutParams smallLayout = new LinearLayout.LayoutParams(175, 175);
        smallLayout.setMargins(0, 0, 0, 0);
        LinearLayout.LayoutParams leftmostLayout = new LinearLayout.LayoutParams(249, 175);
        leftmostLayout.setMargins(200, 20, 0, 0);
        LinearLayout.LayoutParams longLayout = new LinearLayout.LayoutParams(200, 175);
        longLayout.setMargins(0, 0, 0, 0);
        leftmostLayout.setMargins(0,0,0,0);
        smallLayout.setMargins(0,0,0,0);
        ll.addView(countdownTimeText, leftmostLayout);
        ll.addView(startTimer, smallLayout);
        ll.addView(resetTimer, longLayout);
        ll.addView(editTimer, longLayout);



        startTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if(timeSet) {

                    String holdSeconds = "00";

                    if(setSeconds < 10){
                        holdSeconds = "0" + Integer.toString(setSeconds);
                    }
                    else{
                        holdSeconds = Integer.toString(setSeconds);
                    }

                    String holdTime = Integer.toString(setMinutes) + ":" + holdSeconds;
                    long timeVal = 0;
                    if (!holdTime.isEmpty() && pullFromFakeString == false) {
                        if (holdTime.contains(":")) {
                            String tempString = holdTime.substring(0, holdTime.indexOf(':'));
                            long minutes = 0;
                            if (!tempString.isEmpty()) {
                                minutes = Long.parseLong(holdTime.substring(0, holdTime.indexOf(':'))) * 60;
                            }
                            long seconds = Long.parseLong(holdTime.substring(holdTime.indexOf(':') + 1, holdTime.indexOf(':') + 3));
                            timeVal = minutes + seconds;
                        } else {
                            timeVal = Long.parseLong(holdTime);
                        }
                    } else if (!fakeString.isEmpty() && pullFromFakeString == true && fakeString != "Times up") {
                        if (fakeString.contains(":")) {
                            long minutes = Long.parseLong(fakeString.substring(0, fakeString.indexOf(':'))) * 60;
                            long seconds = Long.parseLong(fakeString.substring(fakeString.indexOf(':') + 1, fakeString.indexOf(':') + 3));
                            timeVal = minutes + seconds;
                        } else {
                            timeVal = Long.parseLong(fakeString);
                        }

                    }
                    //change user input to string

                    if (!theWorld) {

                        timeVal = timeVal * 1000;
                        countDownTimer = new mycountDownTimer(timeVal, 1000);
                        countDownTimer.start();
                        theWorld = true;
                        startTimer.setText("■");
                        savedPlayState = "■";
                    } else {
                        //supposed to pause but resets countdown instead
                        String changeTimer = countdownTimeText.getText().toString();
                        if (changeTimer != "Times up") {
                            fakeString = changeTimer.substring(changeTimer.length() - 5, changeTimer.length());
                        }
                        countDownTimer.cancel();
                        theWorld = false;
                        pullFromFakeString = true;
                        startTimer.setText("►");
                        savedPlayState = "►";
                    }
                }

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

        editTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimerDialogFragment timerDialog = new TimerDialogFragment();
                timerDialog.show(((Activity) ll.getContext()).getFragmentManager(), "tag");
            }
        });



        resetTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(countDownTimer != null) {
                    countDownTimer.cancel();
                }
                theWorld = false;
                pullFromFakeString = false;
                holder.setBackgroundColor(Color.TRANSPARENT);
                timerWent=false;
                timeSet = false;
                startTimer.setText("►");
                savedPlayState = "►";
                countdownTimeText.setText("00:00:00"); //bad way of resetting the timer
                savedCountDownTime = "00:00:00";
            }
        });

        labelText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                labelText.setFocusableInTouchMode(true);

                return false;
            }
        });



    }

    static void setSeconds(int a){
        setSeconds = a;
    }

    static void setMinutes(int a){
        setMinutes = a;
    }

    public void stopIt(){
        if(countDownTimer !=  null) {
            countDownTimer.cancel();
        }
    }
}
