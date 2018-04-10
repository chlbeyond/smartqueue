package com.smartqueue.setting;

import android.app.AlertDialog;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;
import android.view.View;

import com.go2smartphone.paidui.R;

public class AdvancedSettingFragment extends PreferenceFragment {

	AlertDialog dialog;
	private Preference smartqueue_display_config;
	private Preference smartqueue_display_next_video;
	private Preference smartqueue_set_printer;
	private Preference smartqueue_call_config;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View v1 = getActivity().findViewById(android.R.id.content);
		v1.setPadding(0, 30, 0, 0);
		// Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.advanced_preference);
		smartqueue_display_config = this.findPreference("smartqueue_display_config");
		smartqueue_display_next_video = this.findPreference("smartqueue_display_next_video");
		smartqueue_set_printer = this.findPreference("smartqueue_set_printer");
		smartqueue_call_config = this.findPreference("smartqueue_call_config");
		initDisplay();
		initNextVideo();
		initSetPrinter();
		initSetCall();

	}

	private void initSetCall() {
		// TODO Auto-generated method stub
		smartqueue_call_config.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				// TODO Auto-generated method stub

				return true;
			}
		});
	}

	private void initSetPrinter() {
		// TODO Auto-generated method stub

		smartqueue_set_printer.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				// TODO Auto-generated method stub

				return true;
			}
		});
	}

	private void initNextVideo() {
		// TODO Auto-generated method stub
		smartqueue_display_next_video.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				// TODO Auto-generated method stub

				return true;
			}
		});
	}

	private void initDisplay() {
		// TODO Auto-generated method stub
		String[] printerNames = new String[2];
		printerNames[0] = "视频";
		printerNames[1] = "图片";
		((ListPreference) smartqueue_display_config).setEntryValues(printerNames);
		((ListPreference) smartqueue_display_config).setEntries(printerNames);
		smartqueue_display_config.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				// TODO Auto-generated method stub
				smartqueue_display_config.setSummary(newValue.toString());
				return true;
			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}