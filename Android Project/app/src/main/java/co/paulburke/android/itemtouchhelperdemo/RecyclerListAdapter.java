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

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import co.kazander.Alarm;
import co.kazander.Counter;
import co.kazander.Pedometer;
import co.kazander.StopWatch;
import co.kazander.Timer;
import co.paulburke.android.itemtouchhelperdemo.helper.ItemTouchHelperAdapter;
import co.paulburke.android.itemtouchhelperdemo.helper.ItemTouchHelperViewHolder;
import co.paulburke.android.itemtouchhelperdemo.helper.OnStartDragListener;

/**
 * Simple RecyclerView.Adapter that implements {@link ItemTouchHelperAdapter} to respond to move and
 * dismiss events from a {@link android.support.v7.widget.helper.ItemTouchHelper}.
 *
 * @author Paul Burke (ipaulpro)
 */
public class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerListAdapter.ItemViewHolder>
        implements ItemTouchHelperAdapter {

    public final ArrayList<Integer> mItems = new ArrayList<>();
    public final ArrayList<Counter> mCounters = new ArrayList<>();
    public final ArrayList<Timer> mTimers = new ArrayList<>();
    public final ArrayList<StopWatch> mStopwatches = new ArrayList<>();
    public final ArrayList<Pedometer> mPedometers = new ArrayList<>();
    public final ArrayList<Alarm> mAlarms = new ArrayList<>();




    int counterCurrentPosition = 0;
    int timerCurrentPosition = 0;
    int stopWatchCurrentPosition = 0;
    int pedometerCurrentPosition = 0;
    int alarmCurrentPosition = 0;

    private final OnStartDragListener mDragStartListener;

    public RecyclerListAdapter(Context context, OnStartDragListener dragStartListener) {
        mDragStartListener = dragStartListener;
        this.notifyDataSetChanged();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view, parent.getContext());


        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {

        holder.setIsRecyclable(false);

        int dataValue = mItems.get(position);
        int type = dataValue/1000;
        int actualPosition = dataValue%1000;



        if(type == 1){
            mCounters.get(actualPosition).reinflate(holder.lltext, holder.ll);
        }
        else if(type == 2){
            mStopwatches.get(actualPosition).reinflate(holder.lltext, holder.ll);
        }
        else if(type == 3){
            mTimers.get(actualPosition).reinflate(holder.lltext, holder.ll, holder.itemView);
        }
        else if(type == 4){
            mPedometers.get(actualPosition).reinflate(holder.lltext, holder.ll);
        }
        else if(type == 5){
            mAlarms.get(actualPosition).reinflate(holder.lltext, holder.ll, holder.itemView);
        }

        // Start a drag whenever the handle view it touched
        holder.handleView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(holder);
                }
                return false;
            }
        });
    }

    @Override
    public void onItemDismiss(int position) {
        int dataValue = mItems.get(position);
        int type = dataValue/1000;
        int actualPosition = dataValue%1000;
        if(type == 2){
            mStopwatches.get(actualPosition).stopIt();
        }
        else if(type == 3){
            mTimers.get(actualPosition).stopIt();
        }
        else if(type == 5){
            mAlarms.get(actualPosition).stopIt();
        }
        mItems.remove(position);
        mItems.trimToSize();
        notifyItemRemoved(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mItems, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    /**
     * Simple example of a view holder that implements {@link ItemTouchHelperViewHolder} and has a
     * "handle" view that initiates a drag event when touched.
     */
    public static class ItemViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder{

        public final ImageView handleView;
        public final LinearLayout ll;
        public final LinearLayout lltext;

        public ItemViewHolder(View itemView, Context context) {
            super(itemView);


            ll = (LinearLayout) itemView.findViewById(R.id.ll);
            lltext = (LinearLayout) itemView.findViewById(R.id.lltext);

            handleView = (ImageView) itemView.findViewById(R.id.handle);

        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }

    }

    public void addCounter(){
        int addedInteger = 1000 + counterCurrentPosition;
        mCounters.add(new Counter());
        mItems.add(addedInteger);
        ++counterCurrentPosition;
        notifyDataSetChanged();
    }

    public void addStopWatch() {
        int addedInteger = 2000 + stopWatchCurrentPosition;
        mStopwatches.add(new StopWatch());
        mItems.add(addedInteger);
        ++stopWatchCurrentPosition;
        notifyDataSetChanged();
    }

    public void addTimer() {
        int addedInteger = 3000 + timerCurrentPosition;
        mTimers.add(new Timer());
        mItems.add(addedInteger);
        ++timerCurrentPosition;
        notifyDataSetChanged();
    }

    public void addPedometer() {
        int addedInteger = 4000 + pedometerCurrentPosition;
        mPedometers.add(new Pedometer());
        mItems.add(addedInteger);
        ++pedometerCurrentPosition;
        notifyDataSetChanged();
    }

    public void addAlarm(Context context) {
        int addedInteger = 5000 + alarmCurrentPosition;
        mAlarms.add(new Alarm(context));
        mItems.add(addedInteger);
        ++alarmCurrentPosition;
        notifyDataSetChanged();
    }



    public void updatePedometers(){
        for(Pedometer item : mPedometers){
            item.update();
        }
    }

    public void deleteAll(){
        mItems.clear();
        mCounters.clear();
        for(Timer item : mTimers){
            item.stopIt();
        }
        mTimers.clear();
        for(StopWatch item : mStopwatches){
            item.stopIt();
        }
        mStopwatches.clear();
        for(Alarm item : mAlarms){
            item.stopIt();
        }
        mAlarms.clear();
        mPedometers.clear();
        counterCurrentPosition = 0;
        timerCurrentPosition=0;
        stopWatchCurrentPosition=0;
        pedometerCurrentPosition=0;
        alarmCurrentPosition=0;
        notifyDataSetChanged();
    }

}
