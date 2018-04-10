package com.go2smartphone.sdk.checkServices;

public interface CheckListener {

	public void checkFail(String msg);
	public void noUpdate();
	
	public void downloadCompleted(String apkPath);
	public void downloadCancel();
	public void downloadFail();
}
