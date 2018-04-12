package com.smartpos.login;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.go2smartphone.paidui.Api.ErrorHandler;
import com.go2smartphone.paidui.Api.PaiduiApi;
import com.go2smartphone.paidui.Api.PaiduiHttp;
import com.go2smartphone.paidui.R;
import com.go2smartphone.paidui.model.AgentStatus;
import com.go2smartphone.paidui.utils.rx.SharePreferenceUtil;
import com.go2smartphone.paidui.utils.rx.SmartPosPrivateKey;
import com.go2smartphone.paidui.utils.rx.ToastUtils;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ConnectHostFragment extends Fragment {
    private static final String TAG = "ConnectHostFragment";
    private TextView inputConnectHost;
    private EditText editText_ConnectHost;
    private LoginActivity loginActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_connecthost, null);
        loginActivity = (LoginActivity) getActivity();
        inputConnectHost = (TextView) view.findViewById(R.id.inputConnectHost);
        editText_ConnectHost = (EditText) view.findViewById(R.id.editText_ConnectHost);
        Button button_ConnectHost = (Button) view.findViewById(R.id.button_ConnectHost);
        PaiduiApi.BASE_URL = SharePreferenceUtil.getPreference(loginActivity, SmartPosPrivateKey.SP_RD_ANGET_ADDRESS, null);
        if (PaiduiApi.BASE_URL != null) checkAgent();

        button_ConnectHost.setOnClickListener(arg0 -> {
            // TODO Auto-generated method stub
            if (editText_ConnectHost.getText().toString().length() == 0) return;
            PaiduiApi.BASE_URL = "http://" + editText_ConnectHost.getText().toString().trim() + ":9090/smartpos/";
            if (PaiduiHttp.getInstance() != null) PaiduiHttp.clearInstance();
            checkAgent();

        });
        return view;
    }

    public void checkAgent() {
        PaiduiHttp.getInstance().getPaiduiService().connectHost()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(agentStatus -> agentStatus)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AgentStatus>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.show(getActivity(), ErrorHandler.handle(e), ToastUtils.LENGTH_SHORT);
                    }

                    @Override
                    public void onNext(AgentStatus agentStatuses) {
                        Log.d(TAG, "onNext" + agentStatuses);
                        SharePreferenceUtil.saveStringPreference(loginActivity, SmartPosPrivateKey.SP_RD_ANGET_ADDRESS, PaiduiApi.BASE_URL);
                        if (agentStatuses.status.code == 0) {
                            loginActivity.showFragment();
                        } else {
                            ToastUtils.show(getActivity(), "主机连接失败，请重试", ToastUtils.LENGTH_SHORT);
                        }
                    }
                });
    }
}