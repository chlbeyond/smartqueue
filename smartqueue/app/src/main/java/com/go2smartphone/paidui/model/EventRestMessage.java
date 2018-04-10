package com.go2smartphone.paidui.model;


import com.sanyipos.sdk.model.ShopQueue;

import java.util.List;
/**
 * Created by ss on 2016/7/26.
 */
public class EventRestMessage {


    public String agentUrl;

    public List<ShopQueue> mQueueList;

    public String shopName;

    public FetchTicketResult currentCall;

    public String getAgentUrl() {
        return agentUrl;
    }

    public void setAgentUrl(String agentUrl) {
        this.agentUrl = agentUrl;
    }

    public List<ShopQueue> getmQueueList() {
        return mQueueList;
    }

    public void setmQueueList(List<ShopQueue> mQueueList) {
        this.mQueueList = mQueueList;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShop(String shop) {
        this.shopName = shop;
    }

    public FetchTicketResult getCurrentCall() {
        return currentCall;
    }

    public void setCurrentCall(FetchTicketResult currentCall) {
        this.currentCall = currentCall;
    }
}
