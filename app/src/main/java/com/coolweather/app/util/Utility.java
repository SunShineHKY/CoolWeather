package com.coolweather.app.util;

import android.text.TextUtils;

import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;
/**
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
}