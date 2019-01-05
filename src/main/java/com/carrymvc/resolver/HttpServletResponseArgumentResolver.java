package com.carrymvc.resolver;

import com.carrymvc.annotation.CarryService;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author: lhd
 * @date: 2019/1/5 15:00
 */
@CarryService("httpServletResponseArgumentResolver")
public class HttpServletResponseArgumentResolver implements ArgumentResolver {

    @Override
    public boolean support(Class<?> type, int paramIndex, Method method) {
        return ServletResponse.class.isAssignableFrom(type);
    }

    @Override
    public Object argumentResolver(HttpServletRequest request, HttpServletResponse response, Class<?> type, int paramIndex, Method method) {
        return response;
    }
}
