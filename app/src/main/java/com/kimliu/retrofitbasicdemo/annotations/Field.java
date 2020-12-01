package com.kimliu.retrofitbasicdemo.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *  POST请求中的 参数注解
 *  用来：通过反射 获取到 key 值
 */
@Target(ElementType.PARAMETER) // 作用于参数中
@Retention(RetentionPolicy.RUNTIME) // 运行时注解
public @interface Field {

    String value();

}
