package com.go2smartphone.paidui;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Paidui extends Application {
	private static Paidui ourInstance = new Paidui();
	public boolean log = false;
	public Gson gson;

	public static final long ONE_KB = 1024L;
	public static final long ONE_MB = ONE_KB * 1024L;
	public static final long CACHE_DATA_MAX_SIZE = ONE_MB * 3L;

	public static Paidui getInstance() {
		return ourInstance;
	}

	public static String currentDate() {
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(now);
	}

	public static String currentTime() {
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(now);
	}

	public static String sQLiteDir(Context context) {
		return context.getFilesDir() + "/" + currentDate() + ".db3";
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		ourInstance = this;
		this.initGson();
	}

	public static SQLiteDatabase SQLiteDate(Context context) {
		return SQLiteDatabase.openOrCreateDatabase(context.getFilesDir() + "/" + Paidui.currentDate() + ".db3", null);
	}

	private void initGson() {
		this.gson = new GsonBuilder()
				.setDateFormat(
						"yyyy-MM-dd'T'HH:mm:ss"
				)
				.create();
	}


}
