package com.example.minghttp.net;

public interface IHttpRequest {
    void setUrl(String url);

    void setData(byte[] data);

    void setListener(CallbackListener callbackListener);

    void execute();
}
