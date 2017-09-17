package com.coolweather.app.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 网络操作工具类
 * Created by HeKongyang on 2017/9/10.
 */
public class HttpUtil {

    public static void sendHttpRequest(final String address, final HttpCallBackListener listener) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                try {
                    URL url = new URL(address);
                    conn = (HttpURLConnection)url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setReadTimeout(8000);
                    conn.setConnectTimeout(8000);
                    InputStream in = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuffer response = new StringBuffer();
                    String line;
                    while ((line = reader.readLine()) != null)
                    {
                        response.append(line);
                    }

                    /**
                     * 调用接口处理从服务器返回的数据
                     */
                    if (listener != null)
                    {
                        listener.onFinish(response.toString());
                    }
                } catch (Exception e) {
                    if (listener != null) {
                        listener.onError(e);
                    }
                } finally {
                    if (conn != null)
                    {
                        conn.disconnect();
                    }
                }
            }
        }).start();

    }
}
