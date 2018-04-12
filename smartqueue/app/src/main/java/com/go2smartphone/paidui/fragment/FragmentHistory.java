package com.go2smartphone.paidui.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.go2smartphone.paidui.R;
import com.go2smartphone.paidui.model.FetchTicketResult;

import java.util.Collections;
import java.util.List;

public class FragmentHistory extends Fragment {
    public static final String TAG = " Fragment History";
    private SharedPreferences spfs;
    private ListView listView;
    private View view;
    private List<FetchTicketResult> list;
    private HistoryAdapter mAdapter;

    public void setTickets(List<FetchTicketResult> list) {
        this.list = list;
        if (mAdapter != null) {
            mAdapter.setTickets(list);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        view = inflater.inflate(R.layout.fragment__history, null);
        listView = (ListView) view.findViewById(R.id.listView_history);
        mAdapter = new HistoryAdapter(getActivity());
        listView.setAdapter(mAdapter);
        return view;
    }

    public void refresh() {
        if (mAdapter != null) {
            //按创建时间排序，先创建的在后面
            Collections.sort(list, (fetchTicketResult, t1) -> fetchTicketResult.createon.compareTo(t1.createon) * -1);
            mAdapter.notifyDataSetChanged();
        }
    }


}
