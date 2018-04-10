package com.smartqueue.setting;


import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.view.View;

import com.go2smartphone.paidui.R;
import com.smartpos.login.LoginActivity;

public class GeneralSettingFragment extends PreferenceFragment implements OnPreferenceChangeListener {

	private CheckBoxPreference agentPreference;
	private Preference accessCodePreference;
	private EditTextPreference masterPreference;

	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
		String key = preference.getKey();
		Log.d("stamhe", "key = " + key);

		return super.onPreferenceTreeClick(preferenceScreen, preference);

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View v1 = getActivity().findViewById(android.R.id.content);
		v1.setPadding(0, 30, 0, 0);

		// Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.preference);

		// agentPreference = (CheckBoxPreference)
		// findPreference("enable_server_on_boot");
		// agentPreference.setChecked(LoginActivity.settings.getBoolean("is_master",
		// false));
		// agentPreference.setOnPreferenceChangeListener(this);

		findPreference("access_code").setSummary(LoginActivity.settings.getString("access_code", "未知"));
		int DeviceId = LoginActivity.settings.getInt("device_id", -1);
		if (DeviceId == -1) {
			findPreference("device_id").setSummary("未知");
		} else {
			findPreference("device_id").setSummary(String.valueOf(DeviceId));
		}
//		findPreference("app_version").setSummary(
//				Restaurant.getVersionName(getActivity()) + " - Build No." + String.valueOf(Restaurant.getVersionCode(getActivity())));

		masterPreference = (EditTextPreference) findPreference("agent_address");
		masterPreference.setSummary(LoginActivity.settings.getString("agent_address", ""));
		masterPreference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference arg0, Object arg1) {
				// TODO Auto-generated method stub

				return false;
			}
		});
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		// TODO Auto-generated method stub
		String key = preference.getKey();
		Log.d("stamhe", "key = " + key + "value:" + String.valueOf(newValue));
		if (key.equals("access_code")) {
			Preference connectionPref = findPreference(key);
			// Set summary to be the user-description for the selected value
			connectionPref.setSummary(newValue.toString());
		}
		if (key.equals("enable_server_on_boot")) {
			Preference connectionPref = findPreference(key);
			// Set summary to be the user-description for the selected value
			LoginActivity.settings.edit().putBoolean("is_master", agentPreference.isChecked());
		}

		return false;
	}


}