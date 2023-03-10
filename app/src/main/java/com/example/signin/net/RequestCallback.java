package com.example.signin.net;

import android.app.ProgressDialog;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;

import com.example.signin.App;
import com.example.signin.AppManager;
import com.example.signin.LoginActivity;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.request.base.Request;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Response;

public abstract class RequestCallback<T> extends AbsCallback<T> {

    public Type mType;

    private ProgressDialog progressDialog;

    public RequestCallback(ProgressDialog progressDialog) {
        this();
        this.progressDialog = progressDialog;
    }

    public RequestCallback() {
        Type type = getClass().getGenericSuperclass();
        Type[] types = ((ParameterizedType) type).getActualTypeArguments();
        mType = new ParameterizedTypeImpl(BaseJson.class, new Type[]{types[0]});
    }
    public RequestCallback(Type type) {
        //Type type = this.getClass().getGenericSuperclass();
        //Type[] types = ((ParameterizedType) type).getActualTypeArguments();
        //mType = new ParameterizedTypeImpl(BaseJson.class, new Type[]{types[0]});
       this.mType = type;
    }
    @Override
    public T convertResponse(Response response) throws Throwable {

        String bodyStr = null;
        if (response.body() != null) {
            bodyStr = response.body().string();
        }
        Log.d("convertResponse", "========================");
        Log.d("convertResponse", "========================");
        Log.d("convertResponse", "===========║  url : " + response.request().url()+"  =============");
        Log.d("convertResponse", "bodyStr=="+bodyStr);
        Log.d("convertResponse", "========================");



        if (response.code() == 200) {
            if (bodyStr == null) {
                throw new RequestException(response.code(), "请求数据为空");
            }

            BaseJson<T> data = null;
            try {
                data = JSONObject.parseObject(bodyStr, mType);
            } catch (Exception e) {
                Log.e("OkGo", "e==,+"+e.getMessage());
                BaseEJson  d = JSONObject.parseObject(bodyStr, BaseEJson.class);
//                onErrorBusiness("服务器错误");
                throw new RequestException(d.getCode(), d.getMsg());

            }
            if (data == null) {
                throw new RequestException(response.code(), "解析错误:数据解析出错");
            }

            if (data.isSuccess()) {
                return data.getData();
            }
            else {
//                onErrorBusiness(data.getMessage());
                throw new RequestException(response.code(), data.getMsg());
            }
        } else {
            if (bodyStr != null) {
                JSONObject errorJson = null;
                try {
                    errorJson = JSONObject.parseObject(bodyStr);
                } catch (Exception e) {
                }
                if (errorJson != null) {
                    throw new RequestException(response.code(), errorJson.toJSONString());
                } else {
                    throw new RequestException(response.code(), bodyStr);
                }
            }
            throw new RequestException(response.code(), "服务器错误");
        }
    }


    @Override
    public void onSuccess(com.lzy.okgo.model.Response<T> response) {
        if (response.body() != null) {
            onMySuccess(response.body());
        } else {
            onSuccessNullData();
        }
    }

    public void onMySuccess(T data) {

    }
    public void onTotal(int total) {

    }
    public void onSuccessNullData() {

    }

    public void onErrorBusiness(String msg) {
        //toast(msg);
//        Toast.makeText(FLApplication.Companion.getInstance(),msg,Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onError(com.lzy.okgo.model.Response<T> response) {
        super.onError(response);

        if (response.getException() instanceof RequestException) {

            if (((RequestException) response.getException()).getStatusCode() == 401) {
                Toast.makeText(App.Companion.getInstance(),"登录过期",Toast.LENGTH_SHORT).show();
                AppManager.getAppManager().startActivity(LoginActivity.class);
                return;
            }

//            if (response.code() == 500) {
//                onErrorBusiness(((RequestException) response.getException()).getMsg());
//                return;
//            }
//            onErrorBusiness("服务器错误");
            Toast.makeText(App.Companion.getInstance(),((RequestException) response.getException()).getMsg(),Toast.LENGTH_SHORT).show();
//            toast(((RequestException) response.getException()).getMsg());
        } else {
            //toast(response.getException().getMessage());
//            Toast.makeText(FLApplication.Companion.getInstance(),response.getException().getMessage(),Toast.LENGTH_SHORT).show();
//            onErrorBusiness("服务器错误");
        }

    }

    @Override
    public void onStart(Request<T, ? extends Request> request) {
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
        }
//        LogPrintUtils.INSTANCE.printLog(request);
    }

    @Override
    public void onFinish() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.cancel();
        }
    }
}
