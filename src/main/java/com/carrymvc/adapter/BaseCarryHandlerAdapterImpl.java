package com.carrymvc.adapter;

import com.carrymvc.annotation.CarryService;
import com.carrymvc.resolver.ArgumentResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.HashMap;
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

        // 拿到参数解析器
        Map<String, Object> argumentResolvers = getBeansOfType(beans, ArgumentResolver.class);

        // 定义参数索引
        int paramIndex = 0;
        // 定义数组下标索引
        int i = 0;
        // 开始处理参数
        for (Class<?> clazz : paramClazz) {
            // 使用策略模式，找到参数对应的自己的解析器
            for (Map.Entry<String, Object> entry : argumentResolvers.entrySet()) {
                ArgumentResolver argumentResolver = (ArgumentResolver) entry.getValue();
                if (argumentResolver.support(clazz,paramIndex,method)) {
                    args[i++] = argumentResolver.argumentResolver(request,response,clazz,paramIndex,method);
                }
            }
            paramIndex++;
        }

        return args;
    }

    private Map<String, Object> getBeansOfType(Map<String, Object> beans, Class<ArgumentResolver> initType) {
        Map<String, Object> resultBeans = new HashMap<>();
        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            // 获取满足ArgumentResolver接口的bean
            Class<?>[] intFs = entry.getValue().getClass().getInterfaces();
            if (null != intFs && intFs.length > 0) {
                for (Class<?> intf : intFs) {
                    if (intf.isAssignableFrom(initType)) {
                        resultBeans.put(entry.getKey(),entry.getValue());
                    }
                }
            }
        }

        return resultBeans;
    }
}
