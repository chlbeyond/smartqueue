package com.go2smartphone.paidui.model;

/**
 * Created by ss on 2016/7/25.
 */
public class UpdateStateParam {
    public enum State {
        DELETE, REPAST, PASS, BEGIN
    }

    public static final int DELETE = 0;
    public static final int REPAST = 1;
    public static final int PASS = 2;
    public static final int BEGIN = 3;

    public UpdateStateParam(State state) {
        switch (state) {
            case DELETE:
                this.state = "0";
                break;
            case REPAST:
                this.state = "1";
                break;
            case PASS:
                this.state = "2";
                break;
            case BEGIN:
                this.state = "3";
        }
    }

    public String state;

}
