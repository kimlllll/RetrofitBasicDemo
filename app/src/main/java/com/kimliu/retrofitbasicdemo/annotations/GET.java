package com.kimliu.retrofitbasicdemo.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解在GET方法上，用来：使用反射，获取注解的内容，也就是path
 */
@Target(ElementType.METHOD) // 作用在方法上
@Retention(RetentionPolicy.RUNTIME) //运行期注解
public @interface GET {

    String value() default "";

}
