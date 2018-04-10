package com.go2smartphone.paidui.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.go2smartphone.paidui.Paidui;
import com.go2smartphone.paidui.R;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class FragmentStatistics extends Fragment {
	private ListView listView;
	private SQLiteDatabase db;
	public static String table_name[] = new String[]{"table12", "table34", "table5"};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.statistics, null);
		listView = (ListView) view.findViewById(R.id.listView_statistics);
		initList();
		return view;
	}
	private void initList() {
		// TODO Auto-generated method stub
		db = SQLiteDatabase.openOrCreateDatabase(Paidui.sQLiteDir(getActivity()), null);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < 3; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			String sql = "select * from " + table_name[i];
			Cursor cursor = db.rawQuery(sql, null);
			int takePepleCount = 0;
			int mealPepleCount = 0;
			int mealTableCount = 0;
			int waitingtime = 0;
			int p = 0;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				String peple = cursor.getString(cursor.getColumnIndex("peplecount"));
				String currentstate = cursor.getString(cursor.getColumnIndex("currentstate"));
				takePepleCount = takePepleCount + Integer.valueOf(peple);
				if (Integer.valueOf(currentstate) == 1) {
					mealPepleCount = mealPepleCount + Integer.valueOf(peple);
					mealTableCount = mealTableCount + 1;
					String arrivetime = cursor.getString(cursor.getColumnIndex("arrivetime"));
					String mealtime = cursor.getString(cursor.getColumnIndex("mealtime"));
					waitingtime = waitingtime + (getArrDate(Long.parseLong(mealtime) - Long.parseLong(arrivetime)));
					p = p++;
				}
			}
			map.put("count", cursor.getCount());
			map.put("takePepleCount", takePepleCount);
			map.put("mealPepleCount", mealPepleCount);
			map.put("mealTableCount", mealTableCount);
			map.put("waitingtime", waitingtime);
			map.put("p", p);
			list.add(map);

		}
		listView.setAdapter(new StatisticsAdapter(list, getActivity()));

	}
	public class StatisticsAdapter extends BaseAdapter {
		private List<Map<String, Object>> list;
		private Context context;
		public StatisticsAdapter(List<Map<String, Object>> list, Context context) {
			this.list = list;
			this.context = context;
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
			view = inflater.inflate(R.layout.statistics_listview_item, null);
			TextView textView_statistics_table_type = (TextView) view.findViewById(R.id.textView_statistics_table_type);
			TextView textView_statistics_fetch_number_count = (TextView) view.findViewById(R.id.textView_statistics_fetch_number_count);
			TextView textView_statistics_meal_table_count = (TextView) view.findViewById(R.id.textView_statistics_meal_table_count);
			TextView textView_statistics_meal_rate = (TextView) view.findViewById(R.id.textView_statistics_meal_rate);
			TextView textView_statistics_leave_table_count = (TextView) view.findViewById(R.id.textView_statistics_leave_table_count);
			TextView textView_statistics_leave_rate = (TextView) view.findViewById(R.id.textView_statistics_leave_rate);
			TextView textView_statistics_mean_wait = (TextView) view.findViewById(R.id.textView_statistics_mean_wait);
			switch (position) {
				case 0 :
					textView_statistics_table_type.setText("小桌");
					break;
				case 1 :
					textView_statistics_table_type.setText("中桌");
					break;
				case 2 :
					textView_statistics_table_type.setText("大桌");
					break;
				default :
					break;
			}
			textView_statistics_fetch_number_count.setText(list.get(position).get("count") + "("
					+ list.get(position).get("takePepleCount").toString() + "人)");
			textView_statistics_meal_table_count.setText(list.get(position).get("mealTableCount") + "(" + list.get(position).get("mealPepleCount")
					+ "人)");
			double i = (double) Integer.valueOf(list.get(position).get("mealPepleCount").toString())
					/ (double) Integer.valueOf(list.get(position).get("takePepleCount").toString());
			textView_statistics_meal_rate.setText(Double.parseDouble(String.format("%.2f", i)) * 100 + "%");
			int j = Integer.valueOf(list.get(position).get("count").toString())
					- Integer.valueOf(list.get(position).get("mealTableCount").toString());
			textView_statistics_leave_table_count.setText(j + "");
			double b = Integer.valueOf(list.get(position).get("count").toString());
			double leave = (double) j / b;
			textView_statistics_leave_rate.setText(Double.parseDouble(String.format("%.2f", leave)) * 100 + "%");
			if (Integer.valueOf(list.get(position).get("p").toString()) != 0) {
				textView_statistics_mean_wait.setText(Long.parseLong(list.get(position).get("waitingtime").toString())
						/ Long.parseLong(list.get(position).get("p").toString()) + "");
			} else {
				textView_statistics_mean_wait.setText("0");
			}
			return view;
		}
	}
	public int getArrDate(Long unixDate) {
		SimpleDateFormat fm = new SimpleDateFormat("HH:mm");
		int i = (Integer.valueOf(fm.format(new Date(unixDate)).substring(0, 2)) - 8) * 60
				+ Integer.valueOf(fm.format(new Date(unixDate)).substring(3, 5));
		return i;
	}
}