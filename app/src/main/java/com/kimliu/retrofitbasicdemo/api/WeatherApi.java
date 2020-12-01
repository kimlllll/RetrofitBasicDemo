package com.kimliu.retrofitbasicdemo.api;

import com.kimliu.retrofitbasicdemo.annotations.Field;
import com.kimliu.retrofitbasicdemo.annotations.GET;
import com.kimliu.retrofitbasicdemo.annotations.POST;
import com.kimliu.retrofitbasicdemo.annotations.Query;

import okhttp3.Call;

public interface WeatherApi {

    @POST("/v3/weather/weatherInfo")
    Call postWeather(@Field("city") String city, @Field("key") String key);


    @GET("/v3/weather/weatherInfo")
    Call getWeather(@Query("city") String city, @Query("key") String key);



}
