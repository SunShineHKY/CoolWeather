package com.coolweather.app.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;
import com.coolweather.app.model.WeatherInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 数据解析处理
 * Created by HeKongyang on 2017/9/10.
 */
public class Utility {

    /**
     * 解析和处理服务器返回的省级数据
     */
    public synchronized static boolean handleProvinceResponse(CoolWeatherDB coolWeatherDB, String response)
    {
        if (!TextUtils.isEmpty(response))
        {
            String[] provinces = response.split(",");
            if (provinces != null && provinces.length > 0)
            {
                for (String p : provinces)
                {
                    String array[] = p.split("\\|");
                    Province province = new Province();
                    province.setProvinceCode(array[0]);
                    province.setProvinceName(array[1]);
                    coolWeatherDB.saveProvince(province);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的市级数据
     */
    public synchronized static boolean handleCityResponse(CoolWeatherDB coolWeatherDB, String response, int provinceId)
    {
        if (!TextUtils.isEmpty(response))
        {
            String[] cities = response.split(",");
            if (cities != null && cities.length > 0)
            {
                for (String c : cities)
                {
                    String array[] = c.split("\\|");
                    City city = new City();
                    city.setCityCode(array[0]);
                    city.setCityName(array[1]);
                    city.setProvinceId(provinceId);
                    coolWeatherDB.saveCity(city);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的县级数据
     */
    public synchronized static boolean handleCountyResponse(CoolWeatherDB coolWeatherDB, String response, int cityId)
    {
        if (!TextUtils.isEmpty(response))
        {
            String[] counties = response.split(",");
            if (counties != null && counties.length > 0)
            {
                for (String c : counties)
                {
                    String array[] = c.split("\\|");
                    County county = new County();
                    county.setCountyCode(array[0]);
                    county.setCountyName(array[1]);
                    county.setCityId(cityId);
                    coolWeatherDB.saveCounty(county);
                }
                return true;
            }
        }
        return false;
    }

    public static void handleWeatherResponse(Context context, String response)
    {
        try {
            JSONObject json = new JSONObject(response);
            JSONObject jsonWeatherInfo = json.getJSONObject("weatherinfo");
            WeatherInfo weatherInfo = new WeatherInfo();
            weatherInfo.setCity(jsonWeatherInfo.getString("city"));
            weatherInfo.setCityId(jsonWeatherInfo.getString("cityid"));
            weatherInfo.setTem1(jsonWeatherInfo.getString("temp1"));
            weatherInfo.setTem2(jsonWeatherInfo.getString("temp2"));
            weatherInfo.setWeather(jsonWeatherInfo.getString("weather"));
            weatherInfo.setImg1(jsonWeatherInfo.getString("img1"));
            weatherInfo.setImg2(jsonWeatherInfo.getString("img2"));
            weatherInfo.setPtime(jsonWeatherInfo.getString("ptime"));
            saveWeatherInfo(context, weatherInfo);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }

    }

    /**
     * 保存天气数据到Preference中
     * @param context
     * @param weatherInfo
     */
    private static void saveWeatherInfo(Context context, WeatherInfo weatherInfo)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("city_selected", true);
        editor.putString("city", weatherInfo.getCity());
        editor.putString("city_id", weatherInfo.getCityId());
        editor.putString("tem1", weatherInfo.getTem1());
        editor.putString("tem2", weatherInfo.getTem2());
        editor.putString("weather", weatherInfo.getWeather());
        editor.putString("ptime", weatherInfo.getPtime());
        editor.putString("current_date", dateFormat.format(new Date()));
        editor.commit();
    }
}
