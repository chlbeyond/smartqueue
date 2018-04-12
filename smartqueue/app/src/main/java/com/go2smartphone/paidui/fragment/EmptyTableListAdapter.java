package com.go2smartphone.paidui.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.go2smartphone.paidui.R;
import com.sanyipos.sdk.model.SeatEntity;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class EmptyTableListAdapter extends BaseAdapter {
    private List<SeatEntity> list = new ArrayList<SeatEntity>();
    private Context context;

    public EmptyTableListAdapter(List<SeatEntity> shopTable, Context context) {
        this.list = parseEmptyTable(shopTable);
        this.context = context;
    }

    public void setSeatEntitys(List<SeatEntity> shoptables) {
        this.list = parseEmptyTable(shoptables);
    }

    public List<SeatEntity> parseEmptyTable(List<SeatEntity> tables) {//筛选出空桌子
        List<SeatEntity> seatEntities = new ArrayList<>();
        for (SeatEntity seat : tables) {
            if (seat.state == 1) {
                seatEntities.add(seat);
            }
        }
        return seatEntities;
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup group) {
        // TODO Auto-generated method stub+
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.empty_table_list, null);
        TextView textView_empty_table_name = (TextView) view.findViewById(R.id.textView_empty_table_name);
        TextView textView_empty_table_size = (TextView) view.findViewById(R.id.textView_empty_table_size);
        textView_empty_table_name.setText(list.get(position).tableName);
        textView_empty_table_size.setText(list.get(position).personCount + "人");
        return view;
    }
}
