package com.coolweather.app.model;
/**
 * 天气信息
 * Created by HeKongyang on 2017/9/12.
 */
public class WeatherInfo {

    //城市(包括县城)名称
    private String city;

    //城市对应的天气代号
    private String cityId;

    //最低温度
    private String tem1;

    //最高温度
    private String tem2;

    //天气描述
    private String weather;

    //今日天气对应的图片1
    private String img1;

    //今日天气对应的图片2
    private String img2;

    //发布时间
    private String ptime;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getTem1() {
        return tem1;
    }

    public void setTem1(String tem1) {
        this.tem1 = tem1;
    }

    public String getTem2() {
        return tem2;
    }

    public void setTem2(String tem2) {
        this.tem2 = tem2;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getImg1() {
        return img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }

    public String getImg2() {
        return img2;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }

    public String getPtime() {
        return ptime;
    }

    public void setPtime(String ptime) {
        this.ptime = ptime;
    }
}
