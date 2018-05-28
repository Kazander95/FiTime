/*
 * Copyright (C) 2015 Paul Burke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/* Code for the list containing the widgets was created by Paul Burke, app template from https://github.com/iPaulPro/Android-ItemTouchHelper-Demo
 used and heavily edited by Kazander Antonio, Daniel Domingo, Chase Moynihan, and Kevin Tran at California State University Fullerton for their FiTime Project..
 If there are any questions regarding the code Kazander can be reached at Kaz95@csu.fullerton.edu.
 */
package co.paulburke.android.itemtouchhelperdemo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import static android.R.attr.fragment;
import static android.R.attr.prompt;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * @author Paul Burke (ipaulpro)
 */
public class MainActivity extends AppCompatActivity implements SensorEventListener{

    final static RecyclerListFragment fragment = new RecyclerListFragment();
    Button buttonCreate;
    Button buttonClear;
    Button buttonInfo;
    Button buttonStart;
    ImageView fittime_info;
    TextView infotext;
    SpawnerDialogFragment spawner = new SpawnerDialogFragment();
    ClearDialogFragment clearer = new ClearDialogFragment();
    private GoogleApiClient client;
    private SensorManager sensorManager;
    private Sensor countSensor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fittime_info = (ImageView) findViewById(R.id.imageView2);
        fittime_info.bringToFront();
        fittime_info.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // NOTE: This prevents the touches from propagating through the view and incorrectly invoking the button behind it
                return true;
            }
        });
        buttonStart = (Button) findViewById(R.id.start);
        buttonStart.bringToFront();
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeInvis();
            }
        });
        infotext = (TextView) findViewById(R.id.textInfo);
        infotext.bringToFront();


        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        sensorManager.registerListener(this,countSensor,SensorManager.SENSOR_DELAY_UI);

        buttonCreate = (Button) findViewById(R.id.buttonCreate);
        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spawner.show(getFragmentManager(), "test");
            }
        });
        buttonClear = (Button) findViewById(R.id.buttonClear);
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearer.show(getFragmentManager(), "test");
            }
        });
        buttonInfo = (Button) findViewById(R.id.buttonInfo);
        buttonInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeVis();
            }
        });


        if (savedInstanceState == null) {

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content, fragment)
                    .commit();

        }


    }
    //Pedometer Info
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
       fragment.updatePedometers();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    public static class SpawnerDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.spawner)
                    .setItems(R.array.menu_items, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // The 'which' argument contains the index position
                            // of the selected item
                            if (which == 0) {
                                fragment.addCounter();
                            }
                            if (which == 1) {
                                fragment.addTimer();
                            }
                            if (which == 2) {
                                fragment.addStopWatch();
                            }
                            if (which == 3) {
                                fragment.addPedometer();
                            }
                            if (which == 4) {
                                fragment.addAlarm();
                            }
                        }
                    });
            return builder.create();
        }


    }

    public static class ClearDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Would you like to clear all?")
                    .setPositiveButton("Clear All", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            fragment.deleteAll();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ClearDialogFragment.this.getDialog().cancel();
                        }
                    });

            return builder.create();
        }

    }

    private void makeInvis(){
        fittime_info.setVisibility(INVISIBLE);
        buttonStart.setVisibility(INVISIBLE);
        infotext.setVisibility(INVISIBLE);
        buttonStart.setText("Close");
    }

    private void makeVis(){
        fittime_info.setVisibility(VISIBLE);
        buttonStart.setVisibility(VISIBLE);
        infotext.setVisibility(VISIBLE);
    }

    @Override
    public void onBackPressed() {
        makeVis();
    }
}
