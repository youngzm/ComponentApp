package com.young.mysdk.okhttp.response;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.young.mysdk.okhttp.exception.OkHttpException;
import com.young.mysdk.okhttp.listener.DisposeDataHandle;
import com.young.mysdk.okhttp.listener.DisposeDataListener;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.Response;

//专门处理响应的回调接口
public class CommonJsonCallback implements Callback {

    protected final String RESULT_CODE = "ecode"; // 有返回则对于http请求来说是成功的，但还有可能是业务逻辑上的错误
    protected final int RESULT_CODE_VALUE = 0;
    protected final String ERROR_MSG = "emsg";
    protected final String EMPTY_MSG = "";
    protected final String COOKIE_STORE = "Set-Cookie"; // decide the server it


    //请求错误类型
    protected final int NETWORK_ERROR = -1; // 网络错误
    protected final int JSON_ERROR = -2; // json解析错误
    protected final int OTHER_ERROR = -3; // 未知错误

    private Handler mDeliveryHandler;
    private DisposeDataListener mListener;
    private Class<?> mClass;
    private Gson mGson = new Gson();

    public CommonJsonCallback(DisposeDataHandle handle) {
        this.mListener = handle.mListener;
        this.mClass = handle.mClass;
        //将子线程的数据通过Handler转发到UI线程
        this.mDeliveryHandler = new Handler(Looper.getMainLooper());
    }

    //响应失败时的处理
    @Override
    public void onFailure(final Call call, final IOException ioexception) {
        //此时还在非UI线程，因此要转发
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                mListener.onFailure(new OkHttpException(NETWORK_ERROR, ioexception));
            }
        });
    }

    //响应成功时的处理
    @Override
    public void onResponse(final Call call, final Response response) throws IOException {
        final String result = response.body().string();
        //此时还在非UI线程，因此要转发
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                handleResponse(result);
            }
        });
    }

    private ArrayList<String> handleCookie(Headers headers) {
        ArrayList<String> tempList = new ArrayList<String>();
        for (int i = 0; i < headers.size(); i++) {
            if (headers.name(i).equalsIgnoreCase(COOKIE_STORE)) {
                tempList.add(headers.value(i));
            }
        }
        return tempList;
    }

    private void handleResponse(Object responseObj) {
        if (responseObj == null || responseObj.toString().trim().equals("")) {
            mListener.onFailure(new OkHttpException(NETWORK_ERROR, EMPTY_MSG));
            return;
        }

        try {
            /**
             * 协议确定后看这里如何修改
             */
            JSONObject result = new JSONObject(responseObj.toString());
            if (result.has(RESULT_CODE)) {
                if (result.getInt("RESULT_CODE") == RESULT_CODE_VALUE) {
                    if (mClass == null) {
                        mListener.onSuccess(result);//result是JsonObject对象
                    }
                    //通常情况下我们都会将实体类型传进来
                    else {
                        Object obj = mGson.fromJson(responseObj.toString(), mClass);//这里先使用Gson解析
                        //Object obj = ResponseEntityToModule.parseJsonObjectToModule(result, mClass);//也可以用自己封装的解析类
                        if (obj != null) {
                            mListener.onSuccess(obj);//Object是一个实体类
                        } else {
                            //说明json解析时出现异常
                            mListener.onFailure(new OkHttpException(JSON_ERROR, EMPTY_MSG));
                        }
                    }
                } else {
                    //code不是正常code时的处理，可以抛异常也可以做其他处理
                    mListener.onFailure(new OkHttpException(OTHER_ERROR, EMPTY_MSG));
                }
            } else {
                mListener.onFailure(new OkHttpException(OTHER_ERROR, EMPTY_MSG));
            }
        } catch (Exception e) {
            mListener.onFailure(new OkHttpException(OTHER_ERROR, e.getMessage()));
            e.printStackTrace();
        }
    }
}
