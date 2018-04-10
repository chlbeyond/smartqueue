package com.go2smartphone.paidui.Api;

import com.go2smartphone.paidui.model.ErrorStatus;
import com.google.gson.Gson;

import retrofit.HttpException;

/**
 * Created by ss on 2016/7/14.
 */
public class ErrorHandler {
    public static String handle(Throwable throwable) {
        if (throwable instanceof HttpException) {
            HttpException error = (HttpException) throwable;
            try {
                ErrorStatus status = new Gson().fromJson(new String(error.response().errorBody().bytes()),
                        ErrorStatus.class);
                return String.format("[错误码：%d]%s", status.error_code, status.message + (status.extra_message == null ? "" : "," + status.extra_message));
            } catch (Exception e) {
                e.printStackTrace();
                return "请求异常" + throwable.getMessage();
            }
        } else {
            throwable.printStackTrace();
        }
        return null;
    }
}