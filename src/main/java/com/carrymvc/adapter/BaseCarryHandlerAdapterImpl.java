package com.carrymvc.adapter;

import com.carrymvc.annotation.CarryService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author: lhd
 * @date: 2019/1/5 11:46
 */
@CarryService("carryHandlerAdapter")
public class BaseCarryHandlerAdapterImpl implements CarryHandlerAdapter {

    @Override
    public Object[] handle(HttpServletRequest request,
                           HttpServletResponse response,
                           Method method,
                           Map<String, Object> beans) {

        // 获取方法中定义的参数
        Class<?>[] paramClazz = method.getParameterTypes();
        System.out.println("======当前需要解析的参数对应的类=========");
        for (Class<?> clazz : paramClazz) {
            System.out.println(clazz);
        }

        // 返回参数的结果集
        Object[] args = new Object[paramClazz.length];

        return args;
    }
}
