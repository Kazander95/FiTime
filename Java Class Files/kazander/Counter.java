
package co.kazander;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;

import co.paulburke.android.itemtouchhelperdemo.R;

/**
 * Created by Kazander on 3/5/2017.
 */

public class Counter{


    public int counter = 0;
    KeyboardEditText labelText;
    TextView counterText;
    TextView classLabel;
    Button plusButton;
    Button minusButton;
    Button resetButton;
    String mLabelText = "Custom Label";
    int colorHolder;


    public Counter(){
        colorHolder = getRandomColor();
    }



    void plusCounter(TextView view){
        ++counter;
        view.setText(Integer.toString(counter));
    }

    void minusCounter(TextView view){
        if(counter != 0) --counter;
        view.setText(Integer.toString(counter));
    }

    void resetCounter(TextView view){
        counter = 0;
        view.setText(Integer.toString(counter));
    }

    public int getRandomColor(){
        Random rnd = new Random();
        return Color.argb(80, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    public void destroy(LinearLayout a, LinearLayout b){
        a.removeAllViews();
        b.removeAllViews();
    }


    public void reinflate(final LinearLayout lltext, LinearLayout ll){


        classLabel = (TextView) lltext.findViewById(R.id.classLabel);
        classLabel.setText("Counter");
        classLabel.setTextSize(15);
        classLabel.setTextColor(Color.WHITE);


        labelText = (KeyboardEditText) lltext.findViewById(R.id.label);
        labelText.setText(mLabelText);
        labelText.setTextSize(12);
        labelText.setFocusable(false);
        labelText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        labelText.setTextColor(Color.WHITE);


        counterText = new TextView(ll.getContext());
        counterText.setText(Integer.toString(counter));
        counterText.setTextSize(32);
        counterText.setTextColor(Color.WHITE);

        plusButton = new Button(ll.getContext());
        plusButton.setText("+");
        plusButton.getBackground().setColorFilter(colorHolder, PorterDuff.Mode.SRC_ATOP);
        minusButton = new Button(ll.getContext());
        minusButton.setText("-");
        minusButton.getBackground().setColorFilter(colorHolder, PorterDuff.Mode.SRC_ATOP);
        resetButton = new Button(ll.getContext());
        resetButton.setText("RESET");
        resetButton.getBackground().setColorFilter(colorHolder, PorterDuff.Mode.SRC_ATOP);

        LinearLayout.LayoutParams smallLayout = new LinearLayout.LayoutParams(175, 175);
        smallLayout.setMargins(0, 0, 0, 0);
        LinearLayout.LayoutParams leftmostLayout = new LinearLayout.LayoutParams(175, 175);
        leftmostLayout.setMargins(70, 20, 0, 0);
        LinearLayout.LayoutParams longLayout = new LinearLayout.LayoutParams(200, 175);
        longLayout.setMargins(0, 0, 0, 0);
        ll.addView(counterText, leftmostLayout);
        ll.addView(plusButton, smallLayout);
        ll.addView(minusButton, smallLayout);
        ll.addView(resetButton, longLayout);


        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                plusCounter(counterText);
            }
        });
        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                minusCounter(counterText);
            }
        });
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetCounter(counterText);
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
    }


}




