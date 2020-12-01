package com.kimliu.retrofitbasicdemo.retrofit;

import com.kimliu.retrofitbasicdemo.annotations.Field;
import com.kimliu.retrofitbasicdemo.annotations.GET;
import com.kimliu.retrofitbasicdemo.annotations.POST;
import com.kimliu.retrofitbasicdemo.annotations.Query;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Request;

/**
 * 记录请求的类型，参数，完整地址
 */
public class ServiceMethod {

    private final String mHttpMethod;
    private final Call.Factory mFactory;
    private final HttpUrl mHttpUrl;
    private final String mRelativeUrl;
    private final ParameterHandler[] mParameterHandler;
    private final boolean mIsHaveBody;
    private FormBody.Builder mFormBuilder;
    private HttpUrl.Builder mHttpUrlBuilder;


    public ServiceMethod(Builder builder) {
        mHttpMethod = builder.mHttpMethod;
        mFactory = builder.retrofit.mFactory;
        mHttpUrl = builder.retrofit.mHttpUrl;
        mRelativeUrl = builder.mRelativeUrl;
        mParameterHandler = builder.mParameterHandler;
        mIsHaveBody = builder.mIsHaveBody;

        if(mIsHaveBody){
            mFormBuilder = new FormBody.Builder();
        }
    }


    /**
     *
     * @param args 调用ServiceMethod中的方法时，传入的参数
     * @return
     */
    public Object invoke(Object[] args){
        /**
         * 1. 处理请求的地址与参数
         */
        for (int i = 0; i < mParameterHandler.length; i++) {
            ParameterHandler parameterHandler = mParameterHandler[i];
            // 将parameterHandler中的key值 给到对应的value
            parameterHandler.apply(this,args[i].toString());
        }

        // 获取最终的请求地址
        HttpUrl httpUrl;
        if(mHttpUrlBuilder == null){
            mHttpUrlBuilder = mHttpUrl.newBuilder(mRelativeUrl);
        }

        httpUrl = mHttpUrlBuilder.build();

        //请求体
        FormBody formBody = null;
        if(mFormBuilder != null){
            formBody = mFormBuilder.build();
        }

        // 请求网络
        Request request = new Request.Builder().url(httpUrl).method(mHttpMethod,formBody).build();
        return mFactory.newCall(request);

    }


    // get请求 把k-v拼接到url中
    public void addQueryParameter(String key,String value){
        if(mHttpUrlBuilder == null){
            mHttpUrlBuilder = mHttpUrl.newBuilder(mRelativeUrl);
        }
        mHttpUrlBuilder.addQueryParameter(key,value);
    }

    // POST请求，把k-v放到请求体中
    public void addFieldParameter(String key,String value){
        mFormBuilder.add(key,value);
    }


    public static class Builder{

        private DemoRetrofit retrofit;
        private final Annotation[] mMethodAnnotations;
        private final Annotation[][] mParameterAnnotations;
        String mHttpMethod;
        String mRelativeUrl;
        boolean mIsHaveBody;
        private ParameterHandler[] mParameterHandler;

        public Builder(DemoRetrofit retrofit, Method method) {
            this.retrofit = retrofit;
            mMethodAnnotations = method.getAnnotations(); // 获取方法上的所有注释
            mParameterAnnotations = method.getParameterAnnotations(); //获取方法参数上的所有注释（一个参数可以有多个注解,一个方法又会有多个参数）
        }

        /**
         * 在这里获取 注释中的内容
         * @return
         */
        public ServiceMethod bulid(){
            /**
             * 1. 解析方法上的注解，处理POST和GET
             */
            for (Annotation methodAnnotation : mMethodAnnotations) {
                if(methodAnnotation instanceof POST){
                    // 如果是POST类型的注解
                    this.mHttpMethod = "POST";
                    // 记录请求URL的path
                    this.mRelativeUrl = ((POST) methodAnnotation).value();
                    // 是否有请求体
                    this.mIsHaveBody = true;
                }else if(methodAnnotation instanceof GET){
                    this.mHttpMethod ="GET";
                    this.mRelativeUrl = ((GET) methodAnnotation).value();
                    this.mIsHaveBody = false;
                }
            }

            /**
             * 2. 解析方法参数的注解
             */
            int length = mParameterAnnotations.length;
            mParameterHandler = new ParameterHandler[length];
            for (int i = 0; i < mParameterAnnotations.length; i++) {
                Annotation[] parameterAnnotation = mParameterAnnotations[i];
                for (Annotation annotation : parameterAnnotation) {
                    // 添加一个判断 POST只能使用Field注解 GET只能使用Query注解
                    if(annotation instanceof Field){
                        if(this.mHttpMethod.equals("GET")){
                            throw new RuntimeException("@Field 只可用于POST请求中");
                        }
                        String key = ((Field) annotation).value();// 获取到Field的key值
                        // 获取Key值 放入ParameterHandler中
                        mParameterHandler[i] = new ParameterHandler.FieldParameterHandler(key);
                    }else if(annotation instanceof Query){
                        if(this.mHttpMethod.equals("POST")){
                            throw new RuntimeException("@Query 只可用于GET请求中");
                        }
                        String key = ((Query) annotation).value();
                        // 获取Key值 放入ParameterHandler中
                        mParameterHandler[i] = new ParameterHandler.QueryParameterHandler(key);
                    }
                }
            }
            return new ServiceMethod(this);
        }

    }



}
