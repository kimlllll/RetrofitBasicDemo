package com.kimliu.retrofitbasicdemo.retrofit;

/**
 * 记录参数对应请求中的name
 */
public abstract class ParameterHandler {

    abstract void apply(ServiceMethod serviceMethod,String value);


    static class QueryParameterHandler extends ParameterHandler{

        String key;
        public QueryParameterHandler(String key) {
            this.key = key;
        }

        @Override
        void apply(ServiceMethod serviceMethod, String value) {
            serviceMethod.addQueryParameter(key,value);
        }
    }

    static class FieldParameterHandler extends ParameterHandler{

        String key;
        public FieldParameterHandler(String key) {
            this.key = key;
        }

        @Override
        void apply(ServiceMethod serviceMethod, String value) {
            serviceMethod.addFieldParameter(key,value);
        }
    }

}
