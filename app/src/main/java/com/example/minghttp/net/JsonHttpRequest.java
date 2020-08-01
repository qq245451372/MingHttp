package com.example.minghttp.net;

import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class JsonHttpRequest implements IHttpRequest{
    private String url;
    private byte[] data;
    private CallbackListener mCallbackListener;


    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public void setListener(CallbackListener callbackListener) {
        this.mCallbackListener = callbackListener;
    }

    private HttpURLConnection urlConnection;
    @Override
    public void execute() {
        //访问网络打具体实现
        URL url = null;
        try {
            url = new URL(this.url);
            urlConnection = (HttpURLConnection)url.openConnection();//打开http链接
            urlConnection.setConnectTimeout(6000);//链接的超时时间
            urlConnection.setUseCaches(false);//不使用缓存
            urlConnection.setInstanceFollowRedirects(true);//是成员函数，仅作用于当前函数，设置这个链接是否可以被重定向
            urlConnection.setReadTimeout(3000);//响应的超时时间
            urlConnection.setDoInput(true);//设置这个链接是否可以写入数据
            urlConnection.setDoOutput(true);//设置这个链接是否可以输出数据

            urlConnection.setRequestMethod("POST");//设置请求的方式
            urlConnection.setRequestProperty("Content-Type","application/json;charset=UTF-8");//设置消息打类型
            urlConnection.connect();//链接，从上述至此的配置必须在connect之前完成，实际上它只是建立了一个与服务器的TCP链接
            //------------------使用字节流发送数据--------------
            OutputStream out = urlConnection.getOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(out);//缓冲字节数据包装字节流
            bos.write(data); //把这个字节数组打数据写入缓冲区中
            bos.flush();
            out.close();
            bos.close();

            //-----------------------字符流写入数据--------------------
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK){//得到服务器的返回码是否链接成功
                InputStream in = urlConnection.getInputStream();
                mCallbackListener.onSuccess(in);
                //这里是不是就把我们打返回数据同步到我们的框架了？
            }
            else{
                throw new RuntimeException("请求失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("请求失败");
        }finally {
            urlConnection.disconnect();
        }

    }
}
