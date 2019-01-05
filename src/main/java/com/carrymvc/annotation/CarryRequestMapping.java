package com.carrymvc.annotation;

import java.lang.annotation.*;

/**
 * @author: lhd
 * @date: 2019/1/5 9:34
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CarryRequestMapping {
    String value() default "";
}
