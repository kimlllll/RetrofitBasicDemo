package com.kimliu.retrofitbasicdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.kimliu.retrofitbasicdemo.api.WeatherApi;
import com.kimliu.retrofitbasicdemo.retrofit.DemoRetrofit;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * https://restapi.amap.com/v3/weather/weatherInfo?city=110101&key=ae6c53e2186f33bbf240a12d80672d1b
 *
 * http://co-api.51wnl.com/calendar/vacations?token=A2E0C3CDEA081D3BFC34F8FE23A15886&type=1&timestamp=1462377600&client=ceshi
 *
 */
public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 使用Builder方式 构建 DemoRetrofit 客户端
        DemoRetrofit retrofit = new DemoRetrofit.Builder().baseUrl("https://restapi.amap.com").build();
        WeatherApi weatherApi = retrofit.create(WeatherApi.class);
        // GET 方式
        Call getCall = weatherApi.getWeather("110101", "ae6c53e2186f33bbf240a12d80672d1b");
        getCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //... 处理 请求失败的情况
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // ....处理 请求成功的情况
            }
        });

        // POST方式
        Call postCall = weatherApi.postWeather("110101", "ae6c53e2186f33bbf240a12d80672d1b");
        postCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //... 处理 请求失败的情况
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // ....处理 请求成功的情况
            }
        });


    }
}