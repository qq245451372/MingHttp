package com.example.minghttp.net;

import android.util.Log;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolManager {
    private static  ThreadPoolManager threadPoolManager = new ThreadPoolManager();

    public static ThreadPoolManager getInstance(){
        return threadPoolManager;
    }

    //先进先出 线程安全
    private LinkedBlockingDeque<Runnable> mQueue = new LinkedBlockingDeque();

    // 创建延迟队列
    private DelayQueue<HttpTask> mDelayQueue = new DelayQueue<>();

    public void addDelayTask(HttpTask t)
    {
        if (t != null)
        {
            t.setDelayTime(3000);
            mDelayQueue.offer(t);
        }
    }
    // 将请求添加到队列中
    public void addTask(Runnable runnable)
    {
        if (runnable != null){
            try {
                mQueue.put(runnable);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //创建线程池
    private ThreadPoolExecutor mThreadPoolExecutor;

    //创建线程池
    private ThreadPoolManager() {
        mThreadPoolExecutor = new ThreadPoolExecutor(3, 10, 15, TimeUnit.SECONDS
                , new ArrayBlockingQueue<Runnable>(4), new RejectedExecutionHandler() {

            @Override
            public void rejectedExecution(Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {
                addTask(runnable);
            }
        });

        mThreadPoolExecutor.execute(coreThread);
        mThreadPoolExecutor.execute(delayThread);
    }

    //创建核心线程
    public Runnable coreThread = new Thread(){
        Runnable runn = null;

        @Override
        public void run() {
            while (true)
            {
                try {
                    runn = mQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mThreadPoolExecutor.execute(runn);
            }
        }
    };

    //创建专门处理延迟队列的线程
    public Runnable delayThread = new Thread(){
        Runnable runn = null;

        @Override
        public void run() {
            while (true)
            {
                try {
                    HttpTask ht = mDelayQueue.take();
                    if (ht.getRetryCount() < 3 )
                    {
                        mThreadPoolExecutor.execute(ht);
                        ht.setRetryCount(ht.getRetryCount() + 1);

//                        System.out.println("===重试机制");
                        Log.e("===>机制",ht.getRetryCount() + "次数");
                    }

                    else
                    {
                        Log.e("===>机制","失败太多不处理了");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    };

}
