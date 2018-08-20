package com.young.mysdk.okhttp.request;

import java.io.FileNotFoundException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//请求参数的封装
public class RequestParams {

    //两个线程安全的hashmap
    public ConcurrentHashMap<String, String> urlParams = new ConcurrentHashMap<String, String>();
    public ConcurrentHashMap<String, Object> fileParams = new ConcurrentHashMap<String, Object>();


    //空参构造函数
    public RequestParams() {
        this((Map<String, String>) null);
    }


    //参数为一个map的构造函数
    public RequestParams(Map<String, String> source) {
        if (source != null) {
            for (Map.Entry<String, String> entry : source.entrySet()) {
                put(entry.getKey(), entry.getValue());
            }
        }
    }

    //参数为一个key,一个value的构造函数
    public RequestParams(final String key, final String value) {
        put(key, value);
    }

    //将String类型的value存到hashMap中
    public void put(String key, String value) {
        if (key != null && value != null) {
            urlParams.put(key, value);
        }
    }

    //将object类型的value值存到hashmap中
    public void put(String key, Object object) throws FileNotFoundException {
        if (key != null) {
            fileParams.put(key, object);
        }
    }

    //判定参数是否为空
    public boolean hasParams() {
        if (urlParams.size() > 0 || fileParams.size() > 0) {
            return true;
        }
        return false;
    }
}
