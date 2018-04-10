package com.go2smartphone.paidui.model;

import java.io.Serializable;

/**
 * Created by ss on 2016/7/12.
 */
public class AgentStatus implements Serializable{

//    {
//        "status": {
//        "code": 0,
//                "message": "服务器正常"
//    }
//    }

    public Status status;

    public class Status{

        public int code;

        public String message;
    }
}
