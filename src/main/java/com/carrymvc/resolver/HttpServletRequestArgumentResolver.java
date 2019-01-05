package com.carrymvc.resolver;

import com.carrymvc.annotation.CarryService;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author: lhd
 * @date: 2019/1/5 15:00
 */
@CarryService("httpServletRequestArgumentResolver")
public class HttpServletRequestArgumentResolver implements ArgumentResolver {

    @Override
    public boolean support(Class<?> type, int paramIndex, Method method) {
        return ServletRequest.class.isAssignableFrom(type);
    }

    @Override
    public Object argumentResolver(HttpServletRequest request, HttpServletResponse response, Class<?> type, int paramIndex, Method method) {
        return request;
    }
}
