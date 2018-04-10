package com.go2smartphone.paidui.Api;

import com.go2smartphone.paidui.Paidui;
import com.go2smartphone.paidui.model.Restaurant;
import com.go2smartphone.paidui.utils.rx.NetUtil;
import com.go2smartphone.smartpos.gson.CollectionDeserializer;
import com.go2smartphone.smartpos.gson.DateTimeDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * Created by ss on 2016/7/12.
 */
public class PaiduiHttp {

    private static PaiduiHttp ourInstance;

    private static PaiduiService paiduiService;

    public static PaiduiHttp getInstance() {
        if (ourInstance == null) ourInstance = new PaiduiHttp();
        return ourInstance;
    }

    public static void clearInstance() {
        ourInstance = null;
        paiduiService = null;
    }

    private PaiduiHttp() {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.interceptors().add(new HttpHeaderInterceptor());
        okHttpClient.setReadTimeout(10, TimeUnit.SECONDS);
         /*
         * 查看网络请求发送状况
         */
        if (Paidui.getInstance().log) {
            okHttpClient.interceptors().add(chain -> {
                Response response = chain.proceed(chain.request());
                return response;
            });
        }
        if (PaiduiApi.BASE_URL == "")
            return;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(PaiduiApi.BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
        this.paiduiService = retrofit.create(PaiduiService.class);
    }

    Gson gson = new GsonBuilder().serializeNulls().registerTypeAdapter(Collection.class, new CollectionDeserializer())
            .registerTypeAdapter(Date.class, new DateTimeDeserializer()).create();

    public PaiduiService getPaiduiService() {
        return paiduiService;
    }

    class HttpHeaderInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Request r;
            if (Restaurant.registerInfo != null) {

                r = request.newBuilder()
                        .addHeader("sanyi-device-id", Restaurant.DEVICE_ID)
                        .addHeader("sanyi-staff-id", Long.toString(Restaurant.STAFF_ID))
                        .addHeader("sanyi-shop-id", Restaurant.SHOP_ID)
//                        .addHeader("sanyi-device-name", Restaurant.DEVICE_NAME)
                        .addHeader("sanyi-version-name", Restaurant.VERSION_NAME)
                        .addHeader("sanyi-client-ip", NetUtil.getLocalIpAddress())
                        .addHeader("sanyi-version-code", Restaurant.VERSION_CODE)
                        .addHeader("sanyi-product-code", Restaurant.PRODUCT_CODE)
                        .build();
            } else {
                r = request.newBuilder()
//                        .addHeader("sanyi-device-id", Restaurant.DEVICE_ID)
//                        .addHeader("sanyi-staff-id", Integer.toString(Restaurant.STAFF_ID))
//                        .addHeader("sanyi-shop-id", Restaurant.SHOP_ID)
//                        .addHeader("sanyi-device-name", Restaurant.DEVICE_NAME)
                        .addHeader("sanyi-version-name", Restaurant.VERSION_NAME)
                        .addHeader("sanyi-client-ip", NetUtil.getLocalIpAddress())
                        .addHeader("sanyi-version-code", Restaurant.VERSION_CODE)
                        .addHeader("sanyi-product-code", Restaurant.PRODUCT_CODE)
                        .build();
            }


            return chain.proceed(r);
        }
    }
}
