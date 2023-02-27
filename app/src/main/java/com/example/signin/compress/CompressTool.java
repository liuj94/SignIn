package com.example.signin.compress;

import android.content.Context;
import android.text.TextUtils;

import java.io.File;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;


public class CompressTool {
    private CompressConfig compressConfig;
    private String filePath;
    private CompressCallback.CompressSuccess onSuccessCallback;
    private CompressCallback.CompressError onErrorCallback;

    private CompressTool(Builder builder) {
        this.compressConfig = new CompressConfig(builder.context);
        this.filePath = builder.filePath;
        this.onSuccessCallback = builder.onSuccessCallback;
        this.onErrorCallback = builder.onErrorCallback;
    }

    private void launch(Context context) {
        Luban.with(context)
                .load(filePath)
                .ignoreBy(compressConfig.getIgnoreBy())
                .setTargetDir(compressConfig.getTargetDir())
                .setFocusAlpha(compressConfig.isFocusAlpha())
                .filter(path -> !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif")))
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(File file) {
                        if (onSuccessCallback != null) {
                            onSuccessCallback.onSuccess(file);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (onErrorCallback != null) {
                            onErrorCallback.onError(e);
                        }
                    }
                }).launch();
    }


    public static Builder with(Context context) {
        return new Builder(context);
    }

    public static class Builder {
        private Context context;
        private String filePath;
        private CompressCallback.CompressSuccess onSuccessCallback;
        private CompressCallback.CompressError onErrorCallback;

        Builder(Context context) {
            this.context = context;
        }

        private CompressTool build() {
            return new CompressTool(this);
        }

        public void launch() {
            build().launch(context);
        }

        public Builder load(String path) {
            this.filePath = path;
            return this;
        }

        public Builder onSuccess(CompressCallback.CompressSuccess callback) {
            this.onSuccessCallback = callback;
            return this;
        }

        public Builder onError(CompressCallback.CompressError callback) {
            this.onErrorCallback = callback;
            return this;
        }
    }

}
