package com.example.minghttp.net;

public interface IJsonDataTran<T> {
    void onSuccess(T m);
    void onFailure();
}
