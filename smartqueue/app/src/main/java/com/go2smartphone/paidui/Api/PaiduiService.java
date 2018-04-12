package com.go2smartphone.paidui.Api;

import com.go2smartphone.paidui.model.AgentStatus;
import com.go2smartphone.paidui.model.DeviceregisterParams;
import com.go2smartphone.paidui.model.FetchTicketParam;
import com.go2smartphone.paidui.model.FetchTicketResult;
import com.go2smartphone.paidui.model.RegisterResult;
import com.go2smartphone.paidui.model.Ticket;
import com.go2smartphone.paidui.model.UpdateStateParam;
import com.go2smartphone.paidui.model.UpdateStateResult;
import com.sanyipos.sdk.model.OperationData;
import com.sanyipos.sdk.model.rest.Rest;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import rx.Observable;

/**
 * Created by ss on 2016/7/12.
 */
public interface PaiduiService {


    @GET("health")
    Observable<AgentStatus> connectHost();

    @POST("register/pos")
    Observable<RegisterResult> register(@Body DeviceregisterParams params);

    @GET("rest")
    Observable<Rest> downLoadRest();//获取整个餐厅数据Rest

    @POST("queue")
    Observable<FetchTicketResult> fetchTick(@Body FetchTicketParam params);//取号

    @GET("realtime/{time}")
    Observable<OperationData> polling(@Path("time") Long time);

    @GET("queue")
    Observable<Ticket> queryTick();

    @POST("queue/{detail}")
    Observable<UpdateStateResult> updateTick(@Path("detail") Long detial, @Body UpdateStateParam params);//过号或就餐
}
