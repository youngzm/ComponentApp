package com.young.mysdk.okhttp.listener;

//处理数据的接口
public interface DisposeDataListener {

    //请求成功的回调处理
    public void onSuccess(Object responseObj);

    //请求失败的回调处理
    public void onFailure(Object reasonObj);

}
