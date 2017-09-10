package com.coolweather.app.util;

/**
 * Created by HeKongyang on 2017/9/10.
 */
public interface HttpCallBackListener {

    void onFinish(String response);
    void onError(Exception e);
}
