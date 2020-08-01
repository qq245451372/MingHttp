package com.example.minghttp.net;

public class MingHttp {
    public static<T,M> void sendJsonRequest(String url,T requestData,Class<M> response,IJsonDataTran listener)
    {
        IHttpRequest httpRequest = new JsonHttpRequest();
        CallbackListener callbackListener = new JsonCallbackListener<>(response,listener);

        HttpTask httpTask = new HttpTask(url,requestData,httpRequest,callbackListener);
        ThreadPoolManager.getInstance().addTask(httpTask);
    }
}
