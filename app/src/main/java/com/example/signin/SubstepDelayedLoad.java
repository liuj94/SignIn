package com.example.signin;


import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.Queue;


public class SubstepDelayedLoad {

    //默认延迟
    private final long DEF_DELAYED = 100;

    //设置的延迟
    private long setDelayed = DEF_DELAYED;

    //队列保存每一个任务
    private Queue<RunableBean> queue = new LinkedList<>();

    private MyHandler handler;

    //静态Handler加弱引用 防止内存泄漏
    private static class MyHandler extends Handler{
        private WeakReference<Queue<RunableBean>> mTargetRef;

        public MyHandler(Queue<RunableBean> queue){
            mTargetRef=new WeakReference(queue);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            if(mTargetRef==null){
                return;
            }
            Queue<RunableBean> queue = mTargetRef.get();
            if(queue==null){
                return;
            }
            //执行当前任务
            RunableBean run=queue.poll();
            run.runnable.run();

            //延时执行下一个任务
            RunableBean runNext=queue.peek();
            if(runNext==null){
                return;
            }
            sendEmptyMessageDelayed(1,runNext.delayed);
        }

        /**
         * 清除所有任务
         */
        public void clearAllRunable(){
            mTargetRef.clear();
            mTargetRef=null;
        }
    }

    /**
     * 设置下一个任务的延迟时间
     * 注：调用delayed()后，再调用run()
     * @param millisecond
     * @return
     */
    public SubstepDelayedLoad delayed(long millisecond) {
        this.setDelayed = millisecond;
        return this;
    }

    /**
     * 设置一个任务
     * 注：调用run()之前，先调用delayed()。不调用就是默认延迟 DEF_DELAYED
     * @param runnable
     * @return
     */
    public SubstepDelayedLoad run(Runnable runnable) {
        //任务添加进队列
        RunableBean newRun=new RunableBean();
        newRun.delayed=this.setDelayed;
        newRun.runnable=runnable;
        queue.offer(newRun);

        //延迟时间恢复默认
        this.setDelayed=DEF_DELAYED;
        return this;
    }

    public void start() {
        if(handler!=null){
            throw new IllegalStateException("It can only be started once");
        }
        handler = new MyHandler(queue);

        RunableBean run=queue.peek();
        handler.sendEmptyMessageDelayed(1,run.delayed);
    }

    /**
     * 清掉所有任务
     */
    public void clearAllRunable(){
        handler.clearAllRunable();
        handler=null;
        queue.clear();
        queue=null;
    }

    private static class RunableBean {
        public long delayed;
        public Runnable runnable;
    }
}

