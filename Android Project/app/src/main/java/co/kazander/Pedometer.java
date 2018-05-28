package co.kazander;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Random;

import co.paulburke.android.itemtouchhelperdemo.R;

/**
 * Created by Kazander on 5/10/2017.
 */

public class Pedometer {

    KeyboardEditText labelText;
    TextView counterText;
    TextView classLabel;
    Button resetButton;
    int count;
    Switch aSwitch;
    boolean _update;
    String mLabelText = "Custom Label";
    int colorHolder;

    public Pedometer(){
        colorHolder = getRandomColor();
    }

    public int getRandomColor(){
        Random rnd = new Random();
        return Color.argb(80, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    public void reinflate(final LinearLayout lltext, final LinearLayout ll){

        classLabel = (TextView) lltext.findViewById(R.id.classLabel);
        classLabel.setText("Pedometer");
        classLabel.setTextSize(15);
        classLabel.setTextColor(Color.WHITE);


        labelText = (KeyboardEditText) lltext.findViewById(R.id.label);
        labelText.setText(mLabelText);
        labelText.setTextSize(12);
        labelText.setFocusable(false);
        labelText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        labelText.setTextColor(Color.WHITE);

        counterText = new TextView(ll.getContext());
        counterText.setText(Integer.toString(count));
        counterText.setTextSize(32);
        counterText.setTextColor(Color.WHITE);



        aSwitch = new Switch(ll.getContext());
        aSwitch.setChecked(_update);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    _update = true;
                }
                else{
                    _update = false;
                }

            }
        });

        resetButton = new Button(ll.getContext());
        resetButton.setText("RESET");
        resetButton.getBackground().setColorFilter(colorHolder, PorterDuff.Mode.SRC_ATOP);

        LinearLayout.LayoutParams leftmostLayout = new LinearLayout.LayoutParams(175, 175);
        leftmostLayout.setMargins(70, 20, 0, 0);
        LinearLayout.LayoutParams longLayout = new LinearLayout.LayoutParams(200, 175);
        longLayout.setMargins(0, 0, 0, 0);

        ll.addView(counterText, leftmostLayout);
        ll.addView(aSwitch);
        ll.addView(resetButton, longLayout);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset();
            }
        });

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



    };

    public void update(){
        if(_update){
            count++;
            counterText.setText(Integer.toString(count));
        }
    }

    public void reset(){
        count = 0;
        counterText.setText(Integer.toString(count));
    }

}
