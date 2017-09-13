package com.coolweather.app.activity;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coolweather.app.R;
import com.coolweather.app.util.HttpCallBackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;

public class WeatherActivity extends Activity implements View.OnClickListener{

    private LinearLayout weatherInfoLayout;

    private TextView cityName;
    private TextView tem1;
    private TextView tem2;
    private TextView weatherDesp;
    private TextView publish;
    private TextView currentDate;

    private Button switchCity;
    private Button refreshWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weather_layout);

        weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);

        cityName = (TextView) findViewById(R.id.city_name);
        tem1 = (TextView) findViewById(R.id.temp1);
        tem2 = (TextView) findViewById(R.id.temp2);
        publish = (TextView) findViewById(R.id.publish_text);
        currentDate = (TextView) findViewById(R.id.current_date);
        weatherDesp = (TextView) findViewById(R.id.weather_desp);
        switchCity = (Button) findViewById(R.id.switch_city);
        refreshWeather = (Button) findViewById(R.id.refresh_weather);
        String countyCode = getIntent().getStringExtra("county_code");
        if (!TextUtils.isEmpty(countyCode))
        {
            publish.setText("同步中");
            weatherInfoLayout.setVisibility(View.INVISIBLE);
            cityName.setVisibility(View.INVISIBLE);
            queryWeatherCode(countyCode);
        }
        else
        {
            showWeather();
        }
        switchCity.setOnClickListener(this);
        refreshWeather.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.switch_city:
                switchCity();
                break;
            case R.id.refresh_weather:
                refreshWeather();
                break;
            default:
                break;
        }
    }

    private void switchCity()
    {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putBoolean("city_selected", false);
        editor.commit();
        Intent intent = new Intent(this,ChooseAreaActivity.class);
        intent.putExtra("from_weather_activity", true);
        startActivity(intent);
        finish();
    }

    private void refreshWeather()
    {
        publish.setText("同步中");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherCode = sharedPreferences.getString("weather_code","");
        if (!TextUtils.isEmpty(weatherCode))
        {
            queryWeatherInfo(weatherCode);
        }
    }

    //ok
    private void queryWeatherCode(String countyCode)
    {
        String address = "http://www.weather.com.cn/data/list3/city" +
                countyCode + ".xml";
        queryFromServer(address, "countyCode");
    }

    //ok
    private void queryWeatherInfo(String weatherCode)
    {
        String address = "http://www.weather.com.cn/data/cityinfo/" +
                weatherCode + ".html";
        queryFromServer(address, "weatherCode");
    }

    //ok
    private void queryFromServer(final String address, final String type)
    {
        HttpUtil.sendHttpRequest(address, new HttpCallBackListener(){

            @Override
            public void onFinish(String response) {
                if (!TextUtils.isEmpty(response))
                {
                    if ("countyCode".equals(type))
                    {
                        String[] array = response.split("\\|");
                        if (array != null && array.length == 2)
                        {
                            String weatherCode = array[1];
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather_code", weatherCode);
                            queryWeatherInfo(weatherCode);
                        }
                    }
                    else if("weatherCode".equals(type))
                    {
                        Utility.handleWeatherResponse(WeatherActivity.this,response);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showWeather();
                            }
                        });
                    }
                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        publish.setText("同步失败");
                }
            });
            }
        });
    }

    //ok
    private void showWeather()
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        cityName.setText(prefs.getString("city", ""));
        tem1.setText(prefs.getString("tem1", ""));
        tem2.setText(prefs.getString("tem2", ""));
        weatherDesp.setText(prefs.getString("weather", ""));
        publish.setText("今天"+prefs.getString("ptime", "")+"发布");
        currentDate.setText(prefs.getString("current_date", ""));
        weatherInfoLayout.setVisibility(View.VISIBLE);
        cityName.setVisibility(View.VISIBLE);
    }
}
