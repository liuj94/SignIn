package com.example.signin.compress;

import android.content.Context;

public class CompressConfig {

    private final int ignoreBy = 250;
    private final boolean focusAlpha = true;
    private final String targetDir;

    public CompressConfig(Context context) {
        this.targetDir = context.getCacheDir().getAbsolutePath();
    }

    public int getIgnoreBy() {
        return ignoreBy;
    }

    public boolean isFocusAlpha() {
        return focusAlpha;
    }

    public String getTargetDir() {
        return targetDir;
    }
}
