package com.carrymvc.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author: lhd
 * @date: 2019/1/5 14:54
 */
public interface ArgumentResolver {

    /**
     * 判断当前类是否继承与此接口
     * @param type       当前参数注解的类对象
     * @param paramIndex 参数下标
     * @param method     当前方法
     * @return
     */
    boolean support(Class<?> type, int paramIndex, Method method);

    // 解析内容
    Object argumentResolver(HttpServletRequest request, HttpServletResponse response,
                                   Class<?> type, int paramIndex, Method method);
}
