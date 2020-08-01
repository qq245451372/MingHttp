package com.example.minghttp.net;

import java.io.InputStream;

public interface CallbackListener {
    void onSuccess(InputStream is);

    void onFailure();
}
