package com.kimliu.retrofitbasicdemo.retrofit;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

/**
 * Retrofit 客户端
 *
 * 使用动态代理创建Retrofit客户端
 */
public class DemoRetrofit {

    final Call.Factory mFactory;
    final HttpUrl mHttpUrl;
    final Map<Method, ServiceMethod> serviceMethodCache = new ConcurrentHashMap<>();

    public DemoRetrofit(Call.Factory factory,HttpUrl httpUrl) {
        this.mFactory = factory;
        this.mHttpUrl = httpUrl;
    }

    /**
     *  使用动态代理创建 SeviceMethod
     * @param service
     * @param <T>
     * @return
     */
    public <T> T create(final Class<T> service){
        return (T) Proxy.newProxyInstance(service.getClassLoader(),new Class[]{service},new InvocationHandler(){

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                ServiceMethod serviceMethod = loadServiceMethod(method);
                // 这个args是调用serviceMethod中的方法时，传入的参数
                return serviceMethod.invoke(args);
            }
        });
    }



    private ServiceMethod loadServiceMethod(Method method){
        // 从缓存中获取 ServiceMethod
        ServiceMethod serviceMethod = serviceMethodCache.get(method);
        if(serviceMethod != null){
            return serviceMethod;
        }

        synchronized (serviceMethodCache){
           serviceMethod = serviceMethodCache.get(method);
           if(serviceMethod == null){
               // 使用Builder方式 构建ServiceMethod
               serviceMethod = new ServiceMethod.Builder(this,method).bulid();
               serviceMethodCache.put(method,serviceMethod);
           }
        }
        return serviceMethod;
    }





    public static final class Builder{
        private HttpUrl httpUrl;
        private Call.Factory factory;

        public Builder callFactory(Call.Factory factory) {
            this.factory = factory;
            return this;
        }

        public Builder baseUrl(String baseUrl){
           this.httpUrl = HttpUrl.get(baseUrl);
           return this;
        }

        public DemoRetrofit build(){
            if(httpUrl == null){
                throw new IllegalStateException("Base Url Required");
            }
            Call.Factory factory = this.factory;
            if(factory == null){
                factory = new OkHttpClient();
            }
            return new DemoRetrofit(factory,httpUrl);
        }
    }




}
