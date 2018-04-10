package com.go2smartphone.paidui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.go2smartphone.paidui.R;
import com.go2smartphone.paidui.model.FetchTicketResult;

import java.util.List;

public class FragmentTable extends Fragment {
    private View view;
//    private Timer timer;
    private ListView mListView;
    public PaiduiListAdapter mAdapter;
    public int queueId;

    public FragmentTable(){

    }
    public void setQueueId(int queueId){
        this.queueId = queueId;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        view = inflater.inflate(R.layout.fragment__atable, null);
        init();
        return view;
    }

    public void init(){
        mListView = (ListView) view.findViewById(R.id.listView_table);
        mAdapter = new PaiduiListAdapter(getActivity());
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setTickets(List<FetchTicketResult> list) {
        if (mAdapter != null) {
            mAdapter.setTickets(list);
        }
    }

    public void addTicket(FetchTicketResult tick ){
        if(mAdapter!=null){
            mAdapter.addTicket(tick);
        }
    }
    public void refresh() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden)
        {
            Log.e("```","1");
        }else
        {
            Log.e("```","2");
        }
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
         super.onResume();
//        timer = new Timer();
//        timer.schedule(new TimerTask() {
//
//            @Override
//            public void run() {
//                // TODO Auto-generated method stub
//            }
//        }, 0, 60000);

    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
//        timer.cancel();
    }

}