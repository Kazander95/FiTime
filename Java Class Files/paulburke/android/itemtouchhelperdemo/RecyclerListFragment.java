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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import co.paulburke.android.itemtouchhelperdemo.helper.OnStartDragListener;
import co.paulburke.android.itemtouchhelperdemo.helper.SimpleItemTouchHelperCallback;

/**
 * @author Paul Burke (ipaulpro)
 */
public class RecyclerListFragment extends Fragment implements OnStartDragListener {

    private ItemTouchHelper mItemTouchHelper;
    RecyclerListAdapter adapter;



    public RecyclerListFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return new RecyclerView(container.getContext());
    }
    @Override
    public void onViewCreated (View view, @Nullable Bundle savedInstanceState) {




        super.onViewCreated(view, savedInstanceState);
        adapter = new RecyclerListAdapter(getActivity(), this);
        RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));



        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    public void addCounter(){
        adapter.addCounter();
    }

    public void addStopWatch(){
        adapter.addStopWatch();
    }

    public void addTimer(){
        adapter.addTimer();
    }

    public void addPedometer(){
        adapter.addPedometer();
    }

    public void addAlarm(){
        adapter.addAlarm(getActivity().getBaseContext());
    }

    public void updatePedometers(){
        adapter.updatePedometers();
    }

    public void deleteAll() { adapter.deleteAll(); }
}
