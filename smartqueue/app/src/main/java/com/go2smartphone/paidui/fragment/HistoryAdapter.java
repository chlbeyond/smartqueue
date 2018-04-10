package com.go2smartphone.paidui.fragment;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.go2smartphone.paidui.R;
import com.go2smartphone.paidui.model.FetchTicketResult;
import com.go2smartphone.paidui.model.UpdateStateParam;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by ss on 2016/7/27.
 */
public class HistoryAdapter extends BaseAdapter {
    private Context context;
    private List<FetchTicketResult> list;

    public HistoryAdapter(Context context) {
        this.context = context;

    }

    public void setTickets(List<FetchTicketResult> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list != null ? list.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.historylistitem, null);
        if (position % 2 == 0) {
            view.setBackgroundColor(Color.parseColor("#ededed"));
        }
        TextView textView_history_past_number = (TextView) view.findViewById(R.id.textView_history_past_number);
        TextView textView_history_past_peoplecount = (TextView) view.findViewById(R.id.textView_history_past_peoplecount);
        TextView textView_history_currentstate = (TextView) view.findViewById(R.id.textView_history_currentstate);
        switch (list.get(position).state) {
            case UpdateStateParam.PASS:
                textView_history_currentstate.setText("已过号");
                break;
            case UpdateStateParam.REPAST:
                textView_history_currentstate.setText("已就餐");
                break;

        }
        TextView textView_history_calltimes = (TextView) view.findViewById(R.id.textView_history_calltimes);
        TextView textView_history_phonenumber = (TextView) view.findViewById(R.id.textView_history_phonenumber);
        ImageView imageView_history_callicon = (ImageView) view.findViewById(R.id.imageView_history_callicon);
        TextView textView_history_arrivetime = (TextView) view.findViewById(R.id.textView_history_arrivetime);
        textView_history_past_number.setText(list.get(position).number);
        textView_history_past_peoplecount.setText(list.get(position).count + "人");
//        spfs = context.getSharedPreferences("Paidui", Context.MODE_PRIVATE + Context.MODE_APPEND);
//        final SharedPreferences.Editor editor = spfs.edit();

        if (list.get(position).phone.isEmpty()) {
            textView_history_phonenumber.setVisibility(View.GONE);
            imageView_history_callicon.setVisibility(View.GONE);
        } else {

            textView_history_phonenumber.setText(String.valueOf(list.get(position).phone));
        }
        textView_history_arrivetime.setText(getDate(String.valueOf(list.get(position).createon.getTime())));

        return view;
    }
    public String getDate(String unixDate) {
        SimpleDateFormat fm = new SimpleDateFormat("HH:mm");
        return fm.format(new Date(Long.parseLong(unixDate))).toString();
    }

    public String getArrDate(Long unixDate) {
        SimpleDateFormat fm = new SimpleDateFormat("HH:mm");
        int i = (Integer.valueOf(fm.format(new Date(unixDate)).substring(0, 2)) - 8) * 60
                + Integer.valueOf(fm.format(new Date(unixDate)).substring(3, 5));
        return i + "";
    }
}
