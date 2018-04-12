package com.go2smartphone.paidui.model;

import java.util.Date;

/**
 * Created by ss on 2016/7/22.
 */
public class FetchTicketResult {
//    {
//        "detail": 5,
//            "queue": 4,  //队伍的id
//            "queueName": "A",//队伍名字
//            "tick": 1,//队伍中的序号  queueName + tick = A1
//            "count": 2, //人数
//            "phone": "12345678",
//            "state": 3,  //状态，叫号，就餐，过号
//            "createon": "2016-07-22 17:35:48"
//    }

    public Long detail;
    public long queue;
    public String queueName;
    public String number; // = queueName + tick
    public int tick;
    public int count;//人数
    public String phone;
    public int state;
    public Date createon;


}
