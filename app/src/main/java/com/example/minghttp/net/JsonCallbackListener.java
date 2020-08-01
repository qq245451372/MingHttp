package com.example.minghttp.net;

import android.os.Handler;
import android.os.Looper;

import com.alibaba.fastjson.JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JsonCallbackListener<T> implements CallbackListener{
    private Class<T> responseClass;
    private IJsonDataTran<T> iJsonDataTran;
    private Handler handler = new Handler(Looper.getMainLooper());

    public JsonCallbackListener(Class<T> responseClass,IJsonDataTran iJsonDataTran)
    {
        this.responseClass = responseClass;
        this.iJsonDataTran = iJsonDataTran;
    }
    @Override
    public void onSuccess(InputStream is) {
        //将流转换成制定打responseClass
        String rsponse = getContent(is);

        final T clazz = JSON.parseObject(rsponse,responseClass);

        handler.post(new Runnable() {
            @Override
            public void run() {
                iJsonDataTran.onSuccess(clazz);
            }
        });
    }

    private String getContent(InputStream is) {
//        String content = null;

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line = null;

            try {
                while (((line = reader.readLine()) != null)) {
                    sb.append(line + "\n");
                }
            } catch (IOException e)
            {
                System.out.println("Error=" + e.toString());
            }
            finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void onFailure() {

    }
}
