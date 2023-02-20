package com.example.signin.net;

import static com.dylanc.longan.ToastKt.toast;

import com.alibaba.fastjson.JSONObject;
;
import com.lzy.okgo.callback.AbsCallback;
;
import com.lzy.okgo.request.base.Request;


import java.lang.reflect.Type;

import okhttp3.Response;

public abstract class JsonCallback<T> extends AbsCallback<T> {
    private Type type;
    private Class<T> clz;

    public JsonCallback(Type type) {
        this.type = type;
    }

    public JsonCallback(Class<T> clz) {
        this.clz = clz;
    }

    @Override
    public T convertResponse(Response response) throws Throwable {
        String bodyStr = null;
        if (response.body() != null) {
            bodyStr = response.body().string();
        }


        if (response.code() != 200) {
            throw new RequestException(response.code(), "code:" + response.code());
        }

        if (response.body() == null) {
            throw new RequestException(response.code(), "body is null");
        }

        T data = null;

        if (type != null) {
            data = JSONObject.parseObject(bodyStr, type);
        }
        if (clz != null) {
            data = JSONObject.parseObject(bodyStr, clz);
        }
        if (data == null) {
            throw new RequestException(response.code(), "trans error");
        }
        return data;
    }

    @Override
    public void onError(com.lzy.okgo.model.Response<T> response) {
        super.onError(response);
//        toast(response.getException().getMessage());
//        Toast.makeText(FLApplication.Companion.getInstance(),response.getException().getMessage(),Toast.LENGTH_SHORT).show();
//        toast("");
    }

    @Override
    public void onStart(Request<T, ? extends Request> request) {
//        LogPrintUtils.INSTANCE.printLog(request);
    }
}
