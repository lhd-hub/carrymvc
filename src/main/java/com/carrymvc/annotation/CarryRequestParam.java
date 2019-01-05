package com.carrymvc.annotation;

import java.lang.annotation.*;

/**
 * @author: lhd
 * @date: 2019/1/5 9:35
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CarryRequestParam {
    String value() default "";
}
