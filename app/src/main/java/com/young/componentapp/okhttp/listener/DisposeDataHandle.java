package com.young.componentapp.okhttp.listener;

//处理返回数据处理器
public class DisposeDataHandle {
    public DisposeDataListener mListener = null;
    public Class<?> mClass = null;
    public String mSource = null;

    //一个参数的构造
    public DisposeDataHandle(DisposeDataListener listener)
    {
        this.mListener = listener;
    }

    //两个参数的构造，第二个参数为解析后的实体类型
    public DisposeDataHandle(DisposeDataListener listener, Class<?> clazz)
    {
        this.mListener = listener;
        this.mClass = clazz;
    }

    //两个参数的构造，第二个参数为字符串
    public DisposeDataHandle(DisposeDataListener listener, String source)
    {
        this.mListener = listener;
        this.mSource = source;
    }
}
