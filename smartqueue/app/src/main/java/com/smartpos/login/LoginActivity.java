package com.smartpos.login;


import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.go2smartphone.paidui.R;
import com.go2smartphone.paidui.model.Restaurant;
import com.go2smartphone.paidui.utils.rx.DeviceUtils;
import com.go2smartphone.paidui.utils.rx.RegisteDataUtils;

public class LoginActivity extends FragmentActivity {
    public static FragmentManager loginFragmentManager;
    public static SharedPreferences settings;
    private TextView currentVersion;
    public final int PERMISSIONS_WRITE_REQUEST = 6;
    private AlertDialog dialog;
    private int GET=9;


    private Handler handler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            showHostFragment();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        Restaurant.registerInfo = RegisteDataUtils.getPosRegisteData(this);
        Restaurant.VERSION_CODE = DeviceUtils.getVersionCode(this);
        Restaurant.VERSION_NAME = DeviceUtils.getVersionName(this);
        currentVersion = (TextView) findViewById(R.id.tv_login_currentversion);
        currentVersion.setText("当前版本: " + Restaurant.VERSION_NAME + "." + Restaurant.VERSION_CODE);

        if (Restaurant.registerInfo != null) {
            Restaurant.DEVICE_ID = Long.toString(Restaurant.registerInfo.getId());
            Restaurant.DEVICE_NAME = Restaurant.registerInfo.getName();
            Restaurant.SHOP_ID = Long.toString(Restaurant.registerInfo.getShop_id());
        }
        getPermission();
//        showHostFragment();
    }

    private void getPermission() {
        if (Build.VERSION.SDK_INT > 22) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                // 检测到没有读写权限,向用户请求
                ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_WRITE_REQUEST);
            } else {
                //已有读写权限,进行下一步操作
                showHostFragment();
            }
        } else {
            //系统版本低于6.0 不需要动态申请权限
            showHostFragment();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                // 判断用户是否 点击了不再提醒。(检测该权限是否还可以申请)
                boolean b = shouldShowRequestPermissionRationale(permissions[0]);
                if (b) {
                    // 没有点击"不再提醒",可提示用户跳转去应用设置界面手动开启权限
                    showDialogTipUserGoToAppSettting(requestCode,false);
                } else
                    // 用户选择了"不再提醒",提示用户可以手动去开启权限
                    showDialogTipUserGoToAppSettting(requestCode,true);
//                    finish();
            } else if(requestCode == PERMISSIONS_WRITE_REQUEST){
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                Toast.makeText(this, R.string.get_permission_successed, Toast.LENGTH_SHORT).show();
                handler.sendEmptyMessage(0);
            }else {
                Toast.makeText(LoginActivity.this, "权限请求失败,请重试", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

    }

    // 提示用户去应用设置界面手动开启权限

    private void showDialogTipUserGoToAppSettting(int requestCode,boolean never) {

        if (requestCode == PERMISSIONS_WRITE_REQUEST&&!never) {
            //不给存储权限
            dialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.no_write_permission)
                    .setMessage(R.string.request_write_permission)
                    .setPositiveButton(R.string.open_permission_now, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 跳转到应用设置界面
                            goToAppSetting(PERMISSIONS_WRITE_REQUEST);
                        }
                    })
                    .setNegativeButton(R.string.cancel_permission_now, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).setCancelable(false).show();
        } else if (requestCode==PERMISSIONS_WRITE_REQUEST&&never)
        {
            //不给存储权限,且不再提醒
            dialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.no_write_permission)
                    .setMessage(R.string.request_write_never_permission)
                    .setPositiveButton(R.string.open_permission_now, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 跳转到应用设置界面
                            goToAppSetting(PERMISSIONS_WRITE_REQUEST);
                        }
                    })
                    .setNegativeButton(R.string.cancel_permission_now, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).setCancelable(false).show();
        }
        else {
            Toast.makeText(LoginActivity.this, R.string.get_permission_failed, Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    // 跳转到当前应用的设置界面
    private void goToAppSetting(int requestCode) {
        Intent intent = new Intent();

        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);

        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PERMISSIONS_WRITE_REQUEST) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // 检查该权限是否已经获取
                int i = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                if (i != PackageManager.PERMISSION_GRANTED) {
                    // 提示用户应该去应用设置界面手动开启权限
                    showDialogTipUserGoToAppSettting(PERMISSIONS_WRITE_REQUEST,false);
                } else {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Toast.makeText(this, R.string.get_permission_successed, Toast.LENGTH_SHORT).show();
                    showHostFragment();
                }
            }
        }
    }


    public void showHostFragment(){//输入主机地址
        loginFragmentManager = getSupportFragmentManager();
        loginFragmentManager.beginTransaction().replace(R.id.fragment_login, new ConnectHostFragment()).commit();
    }

    public void showFragment() {
        // loginFragmentManager.beginTransaction().replace(R.id.fragment_login,
        // new InputPasswordFragment()).commit();
        if (Restaurant.registerInfo != null && Restaurant.registerInfo.device_register) {
            loginFragmentManager.beginTransaction().replace(R.id.fragment_login, new InputPasswordFragment()).commit();
        } else {
            loginFragmentManager.beginTransaction().replace(R.id.fragment_login, new DeviceRegistrationFragment()).commit();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {

            finish();

            return false;

        }
        return false;

    }
}
