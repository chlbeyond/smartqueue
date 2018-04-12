package com.go2smartphone.paidui.model;

/**
 * Created by ss on 2016/7/22.
 */
//取号要传给后台的数据
public class FetchTicketParam {

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int count;

    public String phone;
}
