package com.example.signin.bean;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.example.signin.PageRoutes;
import com.xuexiang.xupdate.entity.UpdateEntity;
import com.xuexiang.xupdate.listener.IUpdateParseCallback;
import com.xuexiang.xupdate.proxy.IUpdateParser;

public class CustomUpdateParser implements IUpdateParser {
    Context context;

    public CustomUpdateParser(Context context) {
        this.context = context;
    }

    @Override
    public UpdateEntity parseJson(String json) throws Exception {
        Log.d("getPackageVersionCode()","json="+json);
        if(json!=null){
            CustomData customResult = JSON.parseObject(json, CustomData.class);
            if (customResult != null) {
                CustomResult result = customResult.data;
                if(result!=null){
                    String versionCodeStr = result.versionName.replace(".", "").replace(".", "").trim();
                    Log.d("getPackageVersionCode()","versionCodeStr="+versionCodeStr);
                    int versionCode = Integer.parseInt(versionCodeStr);
                    boolean hasUpdate = false;
                    if (getPackageVersionCode() < versionCode) {
                        hasUpdate = true;
                    }
                    Log.d("getPackageVersionCode()","getPackageVersionCode()="+getPackageVersionCode());
                    return new UpdateEntity()
                            .setHasUpdate(hasUpdate)
                            .setIsIgnorable(false)
                            .setVersionCode(versionCode)
                            .setVersionName(result.versionName)
                            .setUpdateContent("")
                            .setDownloadUrl(PageRoutes.Companion.getBaseUrl()+result.url)
                            .setSize(72);
                }

            }
        }

        return null;
    }

    @Override
    public void parseJson(String json, IUpdateParseCallback callback) throws Exception {

    }

    @Override
    public boolean isAsyncParser() {
        return false;
    }

    public int getPackageVersionCode() {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo("com.example.signin", 0);
            return info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

}
