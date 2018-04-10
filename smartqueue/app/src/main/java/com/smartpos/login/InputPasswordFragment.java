package com.smartpos.login;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.go2smartphone.paidui.Api.ErrorHandler;
import com.go2smartphone.paidui.Api.PaiduiApi;
import com.go2smartphone.paidui.Api.PaiduiHttp;
import com.go2smartphone.paidui.R;
import com.go2smartphone.paidui.SanyiMainActivity;
import com.go2smartphone.paidui.model.Restaurant;
import com.go2smartphone.paidui.utils.rx.ToastUtils;
import com.go2smartphone.pritln.SanyiUSBDriver;
import com.go2smartphone.pritln.UsbPrinter;
import com.go2smartphone.sdk.checkServices.AppChecker;
import com.go2smartphone.sdk.checkServices.CheckListener;
import com.sanyipos.sdk.api.ClientConfig;
import com.sanyipos.sdk.api.SanyiSDK;
import com.sanyipos.sdk.model.OperationData;
import com.sanyipos.sdk.model.rest.Rest;
import com.sanyipos.sdk.model.rest.StaffRest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class InputPasswordFragment extends Fragment implements OnClickListener {

    static final String TAG = "LoginFragment";

    View mainView;

    private String password = new String();

    private RadioButton radioButtonPassWord1;
    private RadioButton radioButtonPassWord2;
    private RadioButton radioButtonPassWord3;
    private RadioButton radioButtonPassWord4;
    private RadioButton radioButtonPassWord5;
    private RadioButton radioButtonPassWord6;
    private RadioButton radioButtonPassWord7;
    private RadioButton radioButtonPassWord8;

    // login digit pad
    private Button buttonLoginDigit0;
    private Button buttonLoginDigit1;
    private Button buttonLoginDigit2;
    private Button buttonLoginDigit3;
    private Button buttonLoginDigit4;
    private Button buttonLoginDigit5;
    private Button buttonLoginDigit6;
    private Button buttonLoginDigit7;
    private Button buttonLoginDigit8;
    private Button buttonLoginDigit9;
    private ImageButton buttonLoginDelete;
    private ImageButton buttonLoginCancel;
    EditText editRFID;

    private static final int UPGRADE = 0;// 用于区分正在下载
    private static final int DOWN = 1;// 用于区分正在下载
    private static final int DOWN_FINISH = 2;// 用于区分下载完成
    private static final int POSINSTALL = 3;// 用于区分下载完成
    private static final int START_PRINT_FAIL = 4;
    private HashMap<String, String> hashMap;// 存储跟心版本的xml信息
    private String fileSavePath;// 下载新apk的厨房地点
    private int progress;// 获取新apk的下载数据量,更新下载滚动条
    private boolean cancelUpdate = false;// 是否取消下载
    private Context context;
    private ProgressBar progressBar;
    private TextView textViewProgress;
    private Dialog downLoadDialog;
    public Long time = Long.valueOf(1);
    private Handler handler = new Handler() {// 跟心ui

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch ((Integer) msg.obj) {
                case POSINSTALL:
                    downloadRest();
                    break;

                case UPGRADE:
                    showUpdateVersionDialog();
                    break;
                case DOWN:
                    progressBar.setProgress(progress);
                    textViewProgress.setText(String.valueOf(progress) + "%");
                    break;
                case DOWN_FINISH:
                    Toast.makeText(context, "文件下载完成,正在安装更新", Toast.LENGTH_SHORT).show();
                    installAPK();
                    break;
                case START_PRINT_FAIL:
                    Toast.makeText(context, "打印服务启动失败，请重新确认打印机安装正常或重新配置打印机", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }

    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inputpassword, container, false);

        mainView = rootView;
        context = rootView.getContext();

        // Set up the login form.

        editRFID = (EditText) rootView.findViewById(R.id.editTextRFID);
        editRFID.requestFocus();
        editRFID.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (!editRFID.hasFocus()) {
                    editRFID.requestFocus();
                }
            }

        });
        editRFID.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    password = editRFID.getText().toString();
                    attemptLogin();
                    editRFID.setText("");
                    return true;
                }
                return false;

            }
        });
        radioButtonPassWord1 = (RadioButton) rootView.findViewById(R.id.RadioButtonPassWord1);
        radioButtonPassWord2 = (RadioButton) rootView.findViewById(R.id.RadioButtonPassWord2);
        radioButtonPassWord3 = (RadioButton) rootView.findViewById(R.id.RadioButtonPassWord3);
        radioButtonPassWord4 = (RadioButton) rootView.findViewById(R.id.RadioButtonPassWord4);
        radioButtonPassWord5 = (RadioButton) rootView.findViewById(R.id.RadioButtonPassWord5);
        radioButtonPassWord6 = (RadioButton) rootView.findViewById(R.id.RadioButtonPassWord6);
        radioButtonPassWord7 = (RadioButton) rootView.findViewById(R.id.RadioButtonPassWord7);
        radioButtonPassWord8 = (RadioButton) rootView.findViewById(R.id.RadioButtonPassWord8);

        radioButtonPassWord1.setClickable(false);
        radioButtonPassWord2.setClickable(false);
        radioButtonPassWord3.setClickable(false);
        radioButtonPassWord4.setClickable(false);
        radioButtonPassWord5.setClickable(false);
        radioButtonPassWord6.setClickable(false);
        radioButtonPassWord7.setClickable(false);
        radioButtonPassWord8.setClickable(false);

        radioButtonPassWord1.setFocusable(false);
        radioButtonPassWord2.setFocusable(false);
        radioButtonPassWord3.setFocusable(false);
        radioButtonPassWord4.setFocusable(false);
        radioButtonPassWord5.setFocusable(false);
        radioButtonPassWord6.setFocusable(false);
        radioButtonPassWord7.setFocusable(false);
        radioButtonPassWord8.setFocusable(false);

        buttonLoginDigit0 = (Button) rootView.findViewById(R.id.ButtonLoginDigit0);
        buttonLoginDigit0.setOnClickListener(this);

        buttonLoginDigit1 = (Button) rootView.findViewById(R.id.ButtonLoginDigit1);
        buttonLoginDigit1.setOnClickListener(this);

        buttonLoginDigit2 = (Button) rootView.findViewById(R.id.ButtonLoginDigit2);
        buttonLoginDigit2.setOnClickListener(this);

        buttonLoginDigit3 = (Button) rootView.findViewById(R.id.ButtonLoginDigit3);
        buttonLoginDigit3.setOnClickListener(this);

        buttonLoginDigit4 = (Button) rootView.findViewById(R.id.ButtonLoginDigit4);
        buttonLoginDigit4.setOnClickListener(this);

        buttonLoginDigit5 = (Button) rootView.findViewById(R.id.ButtonLoginDigit5);
        buttonLoginDigit5.setOnClickListener(this);

        buttonLoginDigit6 = (Button) rootView.findViewById(R.id.ButtonLoginDigit6);
        buttonLoginDigit6.setOnClickListener(this);

        buttonLoginDigit7 = (Button) rootView.findViewById(R.id.ButtonLoginDigit7);
        buttonLoginDigit7.setOnClickListener(this);

        buttonLoginDigit8 = (Button) rootView.findViewById(R.id.ButtonLoginDigit8);
        buttonLoginDigit8.setOnClickListener(this);

        buttonLoginDigit9 = (Button) rootView.findViewById(R.id.ButtonLoginDigit9);
        buttonLoginDigit9.setOnClickListener(this);

        buttonLoginDelete = (ImageButton) rootView.findViewById(R.id.ButtonLoginDelete);
        buttonLoginDelete.setOnClickListener(this);

        buttonLoginCancel = (ImageButton) rootView.findViewById(R.id.ButtonLoginCancel);
        buttonLoginCancel.setOnClickListener(this);

        initProgressDialog();

        initPrinter();
        return rootView;
    }


    private void initProgressDialog() {
        AppChecker.getInstance().checkApkUpdate(getActivity(), PaiduiApi.BASE_URL + "version/" + ClientConfig.CLIENT_SMARTQUEUE,
                Restaurant.DEVICE_ID, Restaurant.registerInfo.salt, new CheckListener() {

                    @Override
                    public void noUpdate() {
                        Message message = new Message();
                        message.obj = POSINSTALL;
                        handler.sendMessage(message);
                    }

                    @Override
                    public void downloadFail() {
                        Message message = new Message();
                        message.obj = POSINSTALL;
                        Bundle bundle = new Bundle();
                        bundle.putString("error", "下载更新包失败");
                        message.setData(bundle);
                        handler.sendMessage(message);
                    }

                    @Override
                    public void downloadCompleted(String apkPath) {
                        fileSavePath = apkPath;
                        installAPK(apkPath);
                        Message message2 = new Message();
                        message2.obj = DOWN_FINISH;
                        handler.sendMessage(message2);
                    }

                    @Override
                    public void downloadCancel() {
                        Message message = new Message();
                        message.obj = POSINSTALL;
                        handler.sendMessage(message);
                    }

                    @Override
                    public void checkFail(String msg) {
                        Message message = new Message();
                        message.obj = POSINSTALL;
                        Bundle bundle = new Bundle();
                        bundle.putString("error", "当前已经是最新版本");
                        message.setData(bundle);
                        handler.sendMessage(message);
                    }
                });
    }

    public void digitPressed(String digit) {
        switch (password.length()) {
            case 0: {
                radioButtonPassWord1.setChecked(true);
                password = password + digit;
                break;
            }
            case 1: {
                radioButtonPassWord2.setChecked(true);
                password = password + digit;
                break;
            }
            case 2: {
                radioButtonPassWord3.setChecked(true);
                password = password + digit;
                break;
            }
            case 3: {
                radioButtonPassWord4.setChecked(true);
                password = password + digit;
                break;
            }
            case 4: {
                radioButtonPassWord5.setChecked(true);
                password = password + digit;
                break;
            }
            case 5: {
                radioButtonPassWord6.setChecked(true);
                password = password + digit;
                break;
            }
            case 6: {
                radioButtonPassWord7.setChecked(true);
                password = password + digit;
                break;
            }
            case 7: {
                radioButtonPassWord8.setChecked(true);
                password = password + digit;
                attemptLogin();
                break;
            }

        }
    }

    public void cancelPressed() {
        password = "";
        radioButtonPassWord1.setChecked(false);
        radioButtonPassWord2.setChecked(false);
        radioButtonPassWord3.setChecked(false);
        radioButtonPassWord4.setChecked(false);
        radioButtonPassWord5.setChecked(false);
        radioButtonPassWord6.setChecked(false);
        radioButtonPassWord7.setChecked(false);
        radioButtonPassWord8.setChecked(false);

    }

    public void deletePressed() {
        switch (password.length()) {
            case 1: {
                radioButtonPassWord1.setChecked(false);
                password = password.substring(0, password.length() - 1);
                break;
            }
            case 2: {
                radioButtonPassWord2.setChecked(false);
                password = password.substring(0, password.length() - 1);
                break;
            }
            case 3: {
                radioButtonPassWord3.setChecked(false);
                password = password.substring(0, password.length() - 1);
                break;
            }
            case 4: {
                radioButtonPassWord4.setChecked(false);
                password = password.substring(0, password.length() - 1);
                break;
            }
            case 5: {
                radioButtonPassWord5.setChecked(false);
                password = password.substring(0, password.length() - 1);
                break;
            }
            case 6: {
                radioButtonPassWord6.setChecked(false);
                password = password.substring(0, password.length() - 1);
                break;
            }
            case 7: {
                radioButtonPassWord7.setChecked(false);
                password = password.substring(0, password.length() - 1);
                break;
            }
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {

        if (SanyiSDK.rest == null || SanyiSDK.rest.operationData == null) {
            showLoginErrorDialog("餐厅资料不全，不能登录，检查网路是否正常");
            return;
        }
        if (SanyiSDK.rest.queues == null || SanyiSDK.rest.queues.size() == 0) {
            showLoginErrorDialog("请检查后台排队是否已经配置");
            return;
        }

        StaffRest user = Restaurant.getStaffByAccessCode(password);
        if (null == user) {
            showLoginErrorDialog("用户不存在");

        } else {
            Restaurant.STAFF_ID = user.id;
            Intent intent = new Intent(getActivity(), SanyiMainActivity.class);
            startActivity(intent);
            getActivity().finish();
            editRFID.setText("");
            editRFID.setFocusable(true);

        }

    }

    public void showLoginErrorDialog(String extraInfo) {
        Resources res = getResources();
        final CharSequence[] items = res.getStringArray(R.array.login_error_dialog);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.login_error);
        // builder.setCancelable(false);
        builder.setMessage(extraInfo);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dlg, int sumthin) {

                dlg.dismiss();
                editRFID.setText("");
                editRFID.setFocusable(true);

            }
        });

        builder.show();
    }

    public static byte[] encrypt(String x) throws Exception {

        java.security.MessageDigest digest = null;
        digest = java.security.MessageDigest.getInstance("SHA-1");
        digest.reset();
        digest.update(x.getBytes("UTF-8"));
        return digest.digest();
    }

    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ButtonLoginDigit0: {
                digitPressed("0");
                break;
            }

            case R.id.ButtonLoginDigit1: {
                digitPressed("1");
                ;
                break;
            }
            case R.id.ButtonLoginDigit2: {
                digitPressed("2");
                ;
                break;
            }
            case R.id.ButtonLoginDigit3: {
                digitPressed("3");
                ;
                break;
            }
            case R.id.ButtonLoginDigit4: {
                digitPressed("4");
                break;
            }
            case R.id.ButtonLoginDigit5: {
                digitPressed("5");
                break;
            }
            case R.id.ButtonLoginDigit6: {
                digitPressed("6");
                break;
            }
            case R.id.ButtonLoginDigit7: {
                digitPressed("7");
                break;
            }
            case R.id.ButtonLoginDigit8: {
                digitPressed("8");
                break;
            }
            case R.id.ButtonLoginDigit9: {
                digitPressed("9");
                break;
            }
            case R.id.ButtonLoginDelete: {
                deletePressed();
                break;
            }
            case R.id.ButtonLoginCancel: {
                cancelPressed();
                break;
            }

        }
    }

    public void onStart() {
        cancelPressed();
        super.onStart();
    }

    public void downloadRest() {
        Subscription subscription = PaiduiHttp.getInstance().getPaiduiService().downLoadRest()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(rest -> rest)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Rest>() {
                    @Override
                    public void onCompleted() {

                        Log.d(TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.show(getActivity(), ErrorHandler.handle(e), Toast.LENGTH_SHORT);
                    }

                    @Override
                    public void onNext(Rest r) {
                        SanyiSDK.rest = r;
                        Observable.timer(0, 2000, TimeUnit.MILLISECONDS)
                                .flatMap(aLong -> PaiduiHttp.getInstance().getPaiduiService().polling(time)
                                        .doOnError(err -> Log.d(TAG, "Error retrieving messages" + err))
                                        .onErrorResumeNext(throwable -> {
                                            Log.d(TAG, "Error retrieving messages" + throwable);
                                            return Observable.empty();
                                        }))
//                                .filter(operationData -> null)
                                .take(10000000)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Subscriber<OperationData>() {
                                    @Override
                                    public void onCompleted() {
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        ToastUtils.show(getActivity(), ErrorHandler.handle(e), ToastUtils.LENGTH_SHORT);
                                    }

                                    @Override
                                    public void onNext(OperationData operationData) {
                                        if (time < operationData.version) {
                                            time = operationData.version;
                                            SanyiSDK.rest.operationData = operationData;
                                            SanyiSDK.getSDK().parseOperation(operationData);
                                        }

                                    }
                                });
                    }
                });
    }


    private void initPrinter() {
        final String printerName = LoginActivity.settings.getString("local_printer_config", "");
        if (printerName != null && printerName.length() > 0) {
            UsbManager usbManager = (UsbManager) getActivity().getSystemService(Context.USB_SERVICE);
            IntentFilter filter = new IntentFilter(Restaurant.ACTION_USB_PERMISSION);
            BroadcastReceiver mUsbStartReceiver = new BroadcastReceiver() {

                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    if (Restaurant.ACTION_USB_PERMISSION.equals(action)) {
                        synchronized (this) {
                            UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                            if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                                if (device != null) {
                                    Restaurant.usbDriver.initPrinter(device);
                                    // String printName =
                                    // LoginActivity.settings.getString("local_usb_printer_config",
                                    // "");
                                    UsbPrinter printer = Restaurant.usbDriver.findPrinter(printerName);
                                    if (printer != null) {
                                        Restaurant.usbDriver.setUsingPrinter(printer);
                                    }
                                }
                            } else {
                                Log.d("sanyipos", "permission denied for device " + device);
                            }
                        }
                    }
                }
            };
            getActivity().registerReceiver(mUsbStartReceiver, filter);
            Restaurant.usbDriver = new SanyiUSBDriver(usbManager, getActivity(), PendingIntent.getBroadcast(getActivity(), 0, new Intent(
                    Restaurant.ACTION_USB_PERMISSION), 0));
            UsbPrinter printer = Restaurant.usbDriver.findPrinter(printerName);
            if (printer != null) {
                Restaurant.usbDriver.setUsingPrinter(printer);
            }
        }
    }


    /**
     * 更新提示框
     */
    private void showUpdateVersionDialog() {
        // 构造对话框
        AlertDialog.Builder builder = new Builder(context);
        builder.setTitle("软件更新");
        builder.setMessage("当前版本" + hashMap.get("current_version_name") + ", 检测到新版本" + hashMap.get("version") + ",是否下载更新?");
        // 更新
        builder.setPositiveButton("更新", new android.content.DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // 显示下载对话框
                showDownloadDialog();
            }
        });
        // 稍后更新
        builder.setNegativeButton("稍后更新", new android.content.DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Message message = new Message();
                message.obj = POSINSTALL;
                handler.sendMessage(message);
            }
        });
        Dialog noticeDialog = builder.create();
        noticeDialog.show();
    }

    /**
     * 下载的提示框
     */
    protected void showDownloadDialog() {
        {

            // 构造软件下载对话框
            AlertDialog.Builder builder = new Builder(context);
            builder.setTitle("正在更新");
            // 给下载对话框增加进度条
            final LayoutInflater inflater = LayoutInflater.from(context);
            View v = inflater.inflate(R.layout.apk_update, null);
            progressBar = (ProgressBar) v.findViewById(R.id.updateProgress);
            textViewProgress = (TextView) v.findViewById(R.id.textViewProgress);
            builder.setView(v);
            // 取消更新
            builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    // 设置取消状态
                    cancelUpdate = true;
                    Message message = new Message();
                    message.obj = POSINSTALL;
                    handler.sendMessage(message);
                }
            });
            downLoadDialog = builder.create();
            downLoadDialog.show();
            // 现在文件
            downloadApk();
        }

    }

    /**
     * 下载apk,不能占用主线程.所以另开的线程
     */
    private void downloadApk() {
        new downloadApkThread().start();

    }


    /**
     * 安装apk文件
     */
    private void installAPK() {
        File apkfile = new File(fileSavePath, "SmartPOS.apk");
        if (!apkfile.exists()) {
            return;
        }
        // 通过Intent安装APK文件
        Intent i = new Intent(Intent.ACTION_VIEW);
        System.out.println("filepath=" + apkfile.toString() + "  " + apkfile.getPath());
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        context.startActivity(i);
        android.os.Process.killProcess(android.os.Process.myPid());// 如果不加上这句的话在apk安装完成之后点击单开会崩溃

    }

    /**
     * 卸载应用程序(没有用到)
     */
    public void uninstallAPK() {
        Uri packageURI = Uri.parse("package:com.example.updateversion");
        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
        context.startActivity(uninstallIntent);

    }

    /**
     * 下载apk的方法
     *
     * @author rongsheng
     */
    public class downloadApkThread extends Thread {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            super.run();
            try {
                // 判断SD卡是否存在，并且是否具有读写权限
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    // 获得存储卡的路径
                    String sdpath = Environment.getExternalStorageDirectory() + "/";
                    fileSavePath = sdpath + "download";
                    URL url = new URL(hashMap.get("url"));
                    // 创建连接
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(5 * 1000);// 设置超时时间
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Charser", "GBK,utf-8;q=0.7,*;q=0.3");
                    // 获取文件大小
                    int length = conn.getContentLength();
                    // 创建输入流
                    InputStream is = conn.getInputStream();

                    File file = new File(fileSavePath);
                    // 判断文件目录是否存在
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    File apkFile = new File(fileSavePath, "SmartPOS.apk");
                    FileOutputStream fos = new FileOutputStream(apkFile);
                    int count = 0;
                    // 缓存
                    byte buf[] = new byte[1024];
                    // 写入到文件中
                    do {
                        int numread = is.read(buf);
                        count += numread;
                        // 计算进度条位置
                        progress = (int) (((float) count / length) * 100);
                        // 更新进度
                        Message message = new Message();
                        message.obj = DOWN;
                        handler.sendMessage(message);
                        if (numread <= 0) {
                            // 下载完成
                            // 取消下载对话框显示
                            downLoadDialog.dismiss();
                            Message message2 = new Message();
                            message2.obj = DOWN_FINISH;
                            handler.sendMessage(message2);
                            break;
                        }
                        // 写入文件
                        fos.write(buf, 0, numread);
                    } while (!cancelUpdate);// 点击取消就停止下载.
                    fos.close();
                    is.close();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public void onResume() {
        Log.e("DEBUG", "onResume of LoginFragment");
        super.onResume();
        editRFID.requestFocus();
    }

    @Override
    public void onDestroy() {
        // if (mUsbStartReceiver != null) {
        // if (mUsbStartReceiver != null) {
        // getActivity().unregisterReceiver(mUsbStartReceiver);
        // }
        super.onDestroy();
    }

    public void installAPK(String apkpath) {
        File apkfile = new File(apkpath);
        if (!apkfile.exists()) {
            return;
        }
        // 通过Intent安装APK文件
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        context.startActivity(i);
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
