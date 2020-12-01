package com.kimliu.retrofitbasicdemo.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解在POST方法上，用来： 通过反射 获取到注释里的Value值 也就是path
 */
@Target(ElementType.METHOD) // 作用在方法上
@Retention(RetentionPolicy.RUNTIME) // 运行时注解
public @interface POST {

    String value() default "";

}
