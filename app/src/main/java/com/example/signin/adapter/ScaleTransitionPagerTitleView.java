package com.example.signin.adapter;

/**
 * author : LiuJie
 * date   : 2021/3/213:20
 */

import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

/**
 * 标题带颜色渐变和缩放的指示器标题
 * 博客: http://hackware.lucode.net
 * Created by hackware on 2016/6/26.
 */
public class ScaleTransitionPagerTitleView extends ColorTransitionPagerTitleView {
    //缩小的比例百分比0.1-1f
    private float mMinScale = 0.95f;
    Context context;

    public ScaleTransitionPagerTitleView(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
        super.onEnter(index, totalCount, enterPercent, leftToRight);    // 实现颜色渐变
//        int color = ArgbEvaluatorHolder.eval(enterPercent, context.getResources().getColor(R.color.green_2), context.getResources().getColor(R.color.green_3));
//        setTextColor(color);
        setScaleX(mMinScale + (1.0f - mMinScale) * enterPercent);
        setScaleY(mMinScale + (1.0f - mMinScale) * enterPercent);

    }

    @Override
    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
        super.onLeave(index, totalCount, leavePercent, leftToRight);    // 实现颜色渐变
//        int color = ArgbEvaluatorHolder.eval(leavePercent, R.color.green_2, R.color.green_3);
//        setTextColor(color);
        setScaleX(1.0f + (mMinScale - 1.0f) * leavePercent);
        setScaleY(1.0f + (mMinScale - 1.0f) * leavePercent);
    }

    @Override
    public void onSelected(int index, int totalCount) {

        LinearGradient linearGradient  = null;
        if(this.getText().length()>4){
            int[] colors = {Color.parseColor("#FF4ec4dd"), Color.parseColor("#FF52dac2")};
            float[] position = {0.1f, 0.8f};
            linearGradient = new LinearGradient(this.getMeasuredWidth(), 0f, -(this.getMeasuredWidth() / 3), 0f, colors
                    , position, Shader.TileMode.MIRROR);
        }else{
            int[] colors = {Color.parseColor("#FF52dac2"),Color.parseColor("#FF4ec4dd") };
            float[] position = {0.1f, 0.8f};
            linearGradient = new LinearGradient(0, 0f, this.getMeasuredWidth()+1 , 0f, colors
                    , position, Shader.TileMode.MIRROR);
        }

        this.getPaint().setShader(linearGradient);
        this.getPaint().setFakeBoldText(true);
        this.invalidate();
    }

    @Override
    public void onDeselected(int index, int totalCount) {

        LinearGradient linearGradient = new LinearGradient(this.getMeasuredWidth(), 0f, 0, 0f,
                Color.parseColor("#FF999999"), Color.parseColor("#FF999999"), Shader.TileMode.REPEAT);
        this.getPaint().setShader(linearGradient);
        this.getPaint().setFakeBoldText(false);
        this.invalidate();
    }

    public float getMinScale() {
        return mMinScale;
    }

    public void setMinScale(float minScale) {
        mMinScale = minScale;
    }

}
