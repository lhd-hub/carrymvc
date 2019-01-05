package com.carrymvc.adapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author: lhd
 * @date: 2019/1/5 10:21
 */
public interface CarryHandlerAdapter {

    Object[] handle(HttpServletRequest request, HttpServletResponse response, Method method, Map<String,Object> beans);
}
