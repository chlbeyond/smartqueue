package com.smartpos.login;

import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.go2smartphone.paidui.Api.ErrorHandler;
import com.go2smartphone.paidui.Api.PaiduiApi;
import com.go2smartphone.paidui.Api.PaiduiHttp;
import com.go2smartphone.paidui.R;
import com.go2smartphone.paidui.model.DeviceregisterParams;
import com.go2smartphone.paidui.model.RegisterResult;
import com.go2smartphone.paidui.model.Restaurant;
import com.go2smartphone.paidui.utils.rx.RegisteDataUtils;
import com.go2smartphone.paidui.utils.rx.ToastUtils;
import com.go2smartphone.sdk.androidUtil.SharePreferenceUtil;
import com.go2smartphone.sdk.androidUtil.SmartPosPrivateKey;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DeviceRegistrationFragment extends Fragment {
    private static final int SUCESS = 1;
    private static final int FAIL = 0;

    static final String TAG = "DeviceRegistration";
    View mainView;
    private boolean isRegistered = false;
    static TextView prompt;
    static TextView promptValue;
    private String value = "";
//    TextView textViewStatus;

    ProgressDialog progressDialog;

    Button btn1;
    Button btn2;
    Button btn3;
    Button btn4;
    Button btn5;
    Button btn6;
    Button btn7;
    Button btn8;
    Button btn9;
    Button btn0;
    Button btnC;
    Button btnBack;
    Button btnOK;
    Button btnChangeAdress;
    public LoginActivity loginActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deviceregistration, null);
        mainView = view;
        loginActivity = (LoginActivity) getActivity();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle(null);
        progressDialog.setMessage("请等候...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setIndeterminateDrawable(getActivity().getResources().getDrawable(R.drawable.spinner));
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        // Defaults
        value = "";

        promptValue = (TextView) mainView.findViewById(R.id.promptValue);
//        textViewStatus = (TextView) mainView.findViewById(R.id.textViewStatus);

        btn1 = (Button) mainView.findViewById(R.id.buttonNumPad1);
        btn2 = (Button) mainView.findViewById(R.id.buttonNumPad2);
        btn3 = (Button) mainView.findViewById(R.id.buttonNumPad3);
        btn4 = (Button) mainView.findViewById(R.id.buttonNumPad4);
        btn5 = (Button) mainView.findViewById(R.id.buttonNumPad5);
        btn6 = (Button) mainView.findViewById(R.id.buttonNumPad6);
        btn7 = (Button) mainView.findViewById(R.id.buttonNumPad7);
        btn8 = (Button) mainView.findViewById(R.id.buttonNumPad8);
        btn9 = (Button) mainView.findViewById(R.id.buttonNumPad9);
        btn0 = (Button) mainView.findViewById(R.id.buttonNumPad0);
        btnC = (Button) mainView.findViewById(R.id.buttonNumPadC);
        btnBack = (Button) mainView.findViewById(R.id.buttonNumPadBack);
        btnOK = (Button) mainView.findViewById(R.id.buttonNumPadOk);
        mainView.findViewById(R.id.buttonChange).
                setOnClickListener(v -> {
                    SharePreferenceUtil.saveStringPreference(loginActivity, SmartPosPrivateKey.SP_RD_ANGET_ADDRESS, null);
                    PaiduiApi.BASE_URL = null;
                    PaiduiHttp.clearInstance();
                    loginActivity.showHostFragment();
                });
        btnC.setOnClickListener(v -> {
            value = "";
            promptValue.setText("");
        });
        btnBack.setOnClickListener(v -> {
            if (value.length() > 0) {
                value = value.substring(0, value.length() - 1);
                promptValue.setText(value);
            }
        });
        btn1.setOnClickListener(v -> appendNumber("1"));
        btn2.setOnClickListener(v -> appendNumber("2"));
        btn3.setOnClickListener(v -> appendNumber("3"));
        btn4.setOnClickListener(v -> appendNumber("4"));
        btn5.setOnClickListener(v -> appendNumber("5"));
        btn6.setOnClickListener(v -> appendNumber("6"));
        btn7.setOnClickListener(v -> appendNumber("7"));
        btn8.setOnClickListener(v -> appendNumber("8"));
        btn9.setOnClickListener(v -> appendNumber("9"));
        btn0.setOnClickListener(v -> appendNumber("0"));
        btnOK.setOnClickListener(v -> {
            if (!promptValue.getText().toString().isEmpty()) {
                DeviceregisterParams params = new DeviceregisterParams();
                params.setAccess_code(promptValue.getText().toString());
                String uuid = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
                params.setUuid(uuid);
                Subscription subscription = PaiduiHttp.getInstance().getPaiduiService().register(params)
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .map(registeData -> registeData)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<RegisterResult>() {
                            @Override
                            public void onCompleted() {
                                Log.d(TAG, "onCompleted: ");
                            }

                            @Override
                            public void onError(Throwable e) {
                                ToastUtils.show(getActivity(), ErrorHandler.handle(e), Toast.LENGTH_SHORT);
                            }

                            @Override
                            public void onNext(RegisterResult registeData) {
                                registeData.device_register = true;
                                Restaurant.registerInfo = registeData;
                                Restaurant.SHOP_ID = Long.toString(registeData.getShop_id());
                                Restaurant.DEVICE_ID = Long.toString(registeData.getId());
                                RegisteDataUtils.savePosRegisteData(getActivity(), registeData);
                                PaiduiHttp.clearInstance();
                                Log.d(TAG, "RegisterResult: " + registeData);
                                loginActivity.showFragment();
                            }
                        });
            }
        });

        return view;
    }

    public void register() {

    }


    private void appendNumber(String inNumb) {
        value = value + inNumb;
        promptValue.setText(promptValue.getText() + inNumb);
    }


}