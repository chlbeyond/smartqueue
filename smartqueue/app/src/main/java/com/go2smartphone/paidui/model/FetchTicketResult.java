package com.go2smartphone.paidui.model;

import java.util.Date;

/**
 * Created by ss on 2016/7/22.
 */
public class FetchTicketResult {
//    {
//        "detail": 5,
//            "queue": 4,
//            "queueName": "A",
//            "tick": 1,
//            "count": 2,
//            "phone": "12345678",
//            "state": 3,
//            "createon": "2016-07-22 17:35:48"
//    }

    public Long detail;
    public long queue;
    public String queueName;
    public String number;
    public int tick;
    public int count;
    public String phone;
    public int state;
    public Date createon;


}
