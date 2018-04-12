package com.go2smartphone.paidui.utils.rx;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.go2smartphone.paidui.model.RegisterResult;


public class RegisteDataUtils {


	/**
	 * 获取POS机注册信息
	 * 
	 * @param context
	 * @return
	 */
	public static RegisterResult getPosRegisteData(Context context) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		RegisterResult registeData = new RegisterResult();
		registeData.setName(sp.getString(SmartPosPrivateKey.SP_RD_POSNAME, ""));
		registeData.setSalt(sp.getString(SmartPosPrivateKey.SP_RD_SALT, ""));
		registeData.setDevice_register(sp.getBoolean(SmartPosPrivateKey.SP_RD_DEVICEREGISTERED, false));
		registeData.setId(sp.getLong(SmartPosPrivateKey.SP_RD_DEVICEID, -1));
		registeData.setShop_id(sp.getLong(SmartPosPrivateKey.SP_RD_SHOPID, -1));
		registeData.setAccess_code(sp.getString(SmartPosPrivateKey.SP_RD_ACCESSCODE, ""));
		return registeData;
	}

	/**
	 * 保存安卓POS注册信息
	 * 
	 * @param context
	 */
	public static void savePosRegisteData(Context context, RegisterResult rd) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		Editor ed = sp.edit();
		ed.putString(SmartPosPrivateKey.SP_RD_SALT, rd.getSalt());
		ed.putString(SmartPosPrivateKey.SP_RD_POSNAME, rd.getName());
		ed.putString(SmartPosPrivateKey.SP_RD_ACCESSCODE, rd.getAccess_code());
		ed.putLong(SmartPosPrivateKey.SP_RD_DEVICEID, rd.getId());
		ed.putLong(SmartPosPrivateKey.SP_RD_SHOPID, rd.getShop_id());
		ed.putBoolean(SmartPosPrivateKey.SP_RD_DEVICEREGISTERED, true);
		ed.commit();
	}
}
