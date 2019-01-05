package com.carrymvc.annotation;

import java.lang.annotation.*;

/**
 * @author: lhd
 * @date: 2019/1/5 9:35
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CarryQualifier {
    String value() default "";
}
