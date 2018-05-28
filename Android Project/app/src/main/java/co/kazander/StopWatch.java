package co.kazander;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;

import co.paulburke.android.itemtouchhelperdemo.R;

import static java.sql.Types.NULL;

/**
 * Created by Kazander on 3/5/2017.
 */

public class StopWatch{

    private long startClock; //the current uptime of the system timer
    private long milliseconds; //difference between the start of the timer and the uptime of the device in miliseconds
    private long updateTime; //updates the stored time
    private long currentTime; //used for math
    private String mLabelText = "Custom Label";
    private String holdTimeText = "00:00:000";
    int colorHolder;

    private TextView stopWatchTime;


    Handler timerHandle = new Handler(); //updates the clock
    boolean flip = true;

    public StopWatch(){


        colorHolder = getRandomColor();


    }

    Runnable timerOn = new Runnable() {
        @Override
        public void run() {
            milliseconds = SystemClock.uptimeMillis() - startClock;
            int millisecond = (int) (currentTime % 1000);
            currentTime = updateTime + milliseconds;
            int second = (int) currentTime / 1000;
            stopWatchTime.setText(String.format("%02d:%02d:%03d", (second % 3600) / 60,
                    (second % 60), millisecond ));
            holdTimeText = String.format("%02d:%02d:%03d", (second % 3600) / 60,
                    (second % 60), millisecond );
            timerHandle.postDelayed(this,0);
        }
    };

    private void destroy(LinearLayout layout, RelativeLayout top){
        layout.removeAllViews();
        top.removeView(layout);
    }

    private void startTimer(){
        if(flip == true) {
            startClock = SystemClock.uptimeMillis(); //gets the current time on the phone
            timerHandle.postDelayed(timerOn, 0);
        }
        flip = false;
    }

    private void stopTimer() {
        if(flip == false) {
            updateTime += milliseconds; //saves the current time
            timerHandle.removeCallbacks(timerOn); //stops the runnable
        }
        flip = true;
    }

    private void resetTimer() {
        flip = true;
        timerHandle.removeCallbacks(timerOn); // stops the runnable
        stopWatchTime.setText("00:00:000"); //bad way of resetting the timer
        holdTimeText = "00:00:000";

        //sets every value back to 0
        startClock = 0;
        milliseconds = 0;
        currentTime = 0;
        updateTime = 0;
    }

    public int getRandomColor(){
        Random rnd = new Random();
        return Color.argb(75, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    public void reinflate(final LinearLayout lltext, final LinearLayout ll){

        final TextView classLabel;
        final KeyboardEditText labelText;
        classLabel = (TextView) lltext.findViewById(R.id.classLabel);
        classLabel.setText("Stop Watch");
        classLabel.setTextSize(15);
        classLabel.setTextColor(Color.WHITE);


        labelText = (KeyboardEditText) lltext.findViewById(R.id.label);
        labelText.setText(mLabelText);
        labelText.setTextSize(12);
        labelText.setFocusable(false);
        labelText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        labelText.setTextColor(Color.WHITE);



        stopWatchTime = new TextView(ll.getContext());
        stopWatchTime.setText(holdTimeText);
        stopWatchTime.setGravity(Gravity.CENTER);
        stopWatchTime.setTextColor(Color.WHITE);

        Button startWatch = new Button(ll.getContext()); //start button
        startWatch.setText("►");
        startWatch.getBackground().setColorFilter(colorHolder, PorterDuff.Mode.SRC_ATOP);

        Button stopWatch = new Button(ll.getContext()); // stop button
        stopWatch.setText("■");
        stopWatch.getBackground().setColorFilter(colorHolder, PorterDuff.Mode.SRC_ATOP);

        Button resetWatch = new Button(ll.getContext());
        resetWatch.setText("RESET");// reset clock
        resetWatch.getBackground().setColorFilter(colorHolder, PorterDuff.Mode.SRC_ATOP);


        LinearLayout.LayoutParams smallLayout = new LinearLayout.LayoutParams(175, 175);
        smallLayout.setMargins(0, 0, 0, 0);
        LinearLayout.LayoutParams leftmostLayout = new LinearLayout.LayoutParams(249, 175);
        leftmostLayout.setMargins(200, 20, 0, 0);
        LinearLayout.LayoutParams longLayout = new LinearLayout.LayoutParams(200, 175);
        longLayout.setMargins(0, 0, 0, 0);
        leftmostLayout.setMargins(0,0,0,0);
        smallLayout.setMargins(0,0,0,0);
        ll.addView(stopWatchTime, leftmostLayout);
        ll.addView(startWatch, smallLayout);
        ll.addView(stopWatch, smallLayout);
        ll.addView(resetWatch, longLayout);


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

        startWatch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startTimer();
            }
        });
        stopWatch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                stopTimer();
            }
        });
        resetWatch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                resetTimer();
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

    public void stopIt(){
        timerHandle.removeCallbacks(timerOn); //stops the runnable
    }

}
