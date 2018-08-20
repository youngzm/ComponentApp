package com.young.mysdk.okhttp.exception;

public class OkHttpException extends Exception{

    //服务器返回的code和msg
    private int ecode;
    private Object emsg;

    //带两个参数的构造函数
    public OkHttpException(int ecode, Object emsg) {
        this.ecode = ecode;
        this.emsg = emsg;
    }

    public int getEcode() {
        return ecode;
    }

    public Object getEmsg() {
        return emsg;
    }
}
