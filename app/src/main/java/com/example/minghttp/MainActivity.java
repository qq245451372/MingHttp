package com.example.minghttp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.minghttp.net.IJsonDataTran;
import com.example.minghttp.net.MingHttp;
import com.example.minghttp.net.ResponseClass;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    private String url = "https://v.juhe.cn/historyWeather/citys?province_id=2";
//    private String url = "https://v";

    private void sendRequest() {
        MingHttp.sendJsonRequest(url, null, ResponseClass.class, new IJsonDataTran<ResponseClass>() {
            @Override
            public void onSuccess(ResponseClass m) {
                System.out.println(m);
            }

            @Override
            public void onFailure() {

            }
        });
    }

    public void onClick(View view) {
        System.out.println("onClick");
        sendRequest();
    }
}