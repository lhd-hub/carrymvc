package com.carrymvc.resolver;

import com.carrymvc.annotation.CarryRequestParam;
import com.carrymvc.annotation.CarryService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author: lhd
 * @date: 2019/1/5 15:04
 */
@CarryService("requestParamArgumentResolver")
public class RequestParamArgumentResolver implements ArgumentResolver {

    @Override
    public boolean support(Class<?> type, int paramIndex, Method method) {
        // 获取当前方法的所有参数
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        Annotation[] annotations = parameterAnnotations[paramIndex];
        for (Annotation annotation : annotations) {
            if (CarryRequestParam.class.isAssignableFrom(annotation.getClass())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object argumentResolver(HttpServletRequest request, HttpServletResponse response, Class<?> type, int paramIndex, Method method) {
        // 获取当前方法的所有参数
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        Annotation[] annotations = parameterAnnotations[paramIndex];
        for (Annotation annotation : annotations) {
            // 判断传进的annotation.getClass()是不是 CarryRequestParam 类型
            if (CarryRequestParam.class.isAssignableFrom(annotation.getClass())) {
                CarryRequestParam param = (CarryRequestParam) annotation;
                return request.getParameter(param.value());
            }
        }
        return null;
    }
}
