package com.carrymvc.servlet;

import com.carrymvc.adapter.CarryHandlerAdapter;
import com.carrymvc.annotation.*;
import com.carrymvc.controller.MyController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: lhd
 * @date: 2019/1/5 10:01
 */
public class CarryDispatchServlet extends HttpServlet {

    // 存储当前加载的所有类
    List<String> classNames = new ArrayList<String>();

    // 存储IOC容器的map
    Map<String, Object> beans = new HashMap<String, Object>();

    // 存储路劲和方法的映射关系
    Map<String, Object> handlerMap = new HashMap<String, Object>();

    public CarryDispatchServlet() {
        System.out.println("CarryDispatchServlet()......");
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        System.out.println("init()......");

        // 1. 扫描需要的实例化类
        doScanPackage("com.carrymvc");
        System.out.println("com.carrymvc下的所有的class类......");
        for (String name : classNames) {
            System.out.println(name);
        }

        // 2. 实例化IOS容器

        doInstance();
        System.out.println("当前实例化的对象信息......");
        for (Map.Entry<String,Object> map : beans.entrySet()) {
            System.out.println("key: " + map.getKey() + ";value: " + map.getValue());
        }

        // 3. 将IOC容器中的service对象设置给controller层定义的field上
        doIoc();

        // 4. 建立path与method的映射关系
        doHandlerMapping();
        System.out.println("Controller层的path和方法映射......");
        for (Map.Entry<String, Object> map : handlerMap.entrySet()) {
            System.out.println("key:" + map.getKey() + ";value: " + map.getValue());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("doGet()......");
        this.doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("doPost()......");

        // 获取请求的uri ：carry/query
        String uri = req.getRequestURI();
//        String context = req.getContextPath();
//        String path = uri.replaceAll(context,",");
        // 通过当前path获取方法名
        Method method = (Method) handlerMap.get(uri);
        String iocBeanKey = "/" + uri.split("/")[1];
        // 获取beans容器的bean
        MyController instance = (MyController) beans.get(iocBeanKey);
        if (null == instance) {
            return;
        }

        // 策略模式，处理参数
        CarryHandlerAdapter handlerAdapter = (CarryHandlerAdapter) beans.get("carryHandlerAdapter");
        Object[] args = handlerAdapter.handle(req,resp,method,beans);

        try {
            // 通过反射来实现方法的调用
            method.invoke(instance,args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {
        System.out.println("destroy()......");
    }

    // 扫描包，加载类
    private void doScanPackage(String basePackage) {
        URL resource = this.getClass().getClassLoader()
                .getResource("/" + basePackage.replaceAll("\\.","/"));
        String fileStr = resource.getFile();
        System.out.println("fileStr: " + fileStr);
        File file = new File(fileStr);
        String[] listFiles = file.list();
        for (String path : listFiles) {
            File filePath = new File(fileStr + path);
            // 如果当前是目录，则递归
            if (filePath.isDirectory()) {
                doScanPackage(basePackage + "." + path);
            }else {
                classNames.add(basePackage + "." + filePath.getName());
            }
        }
    }

    /**
     * 实例化beans，ios容器
     * 通过存储的classNames的类的字符串来反射实例化对象，并存储在beans的map中
     */
    private void doInstance() {
        if (null == classNames || classNames.isEmpty()) {
            System.out.println("doScanPackage Fail......");
            return;
        }

        // 开始实例化对象，通过反射来实现
        classNames.forEach((className) -> {
            String cn = className.replaceAll(".class","");
            try {
                Class<?> clazz = Class.forName(cn);
                // 判断类是否有注解
                if (clazz.isAnnotationPresent(CarryController.class)) {
                    /** 1. 实例化CarryController的注释 */
                    // 通过CarryRequestMapping获取值，作为beans的key
                    CarryRequestMapping requestMapping = clazz.getAnnotation(CarryRequestMapping.class);
                    String key = requestMapping.value();
                    // beans的value为实例化对象
                    Object obj = clazz.newInstance();
                    beans.put(key,obj);
                }else if (clazz.isAnnotationPresent(CarryService.class)) {
                    /** 2. 实例化CarryService的注解*/
                    CarryService service = clazz.getAnnotation(CarryService.class);
                    String key = service.value();
                    Object obj = clazz.newInstance();
                    beans.put(key,obj);
                }else {
                    return;
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        });
    }

    // 将IOC容器中的service对象设置给controller层定义的filed上
    private void doIoc() {
        if (null == beans || beans.isEmpty()) {
            System.out.println("no class is instance......");
            return;
        }

        for (Map.Entry<String,Object> map : beans.entrySet()) {
            // 获取实例
            Object instance = map.getValue();
            // 获取类
            Class<?> clazz = instance.getClass();
            // 如果当前是CarryController类，则获取类中定义的field来设置其对象
            if (clazz.isAnnotationPresent(CarryController.class)) {
                // 获取全部的成员变量
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    if (field.isAnnotationPresent(CarryQualifier.class)) {
                        // 获取当前成员变量的注解值
                        CarryQualifier qualifier = field.getAnnotation(CarryQualifier.class);
                        String value = qualifier.value();
                        // 此时成员变量设置为private,需要强行设置
                        field.setAccessible(true);

                        // 将beans的实力化对象赋值给当前的变量
                        try {
                            field.set(instance, beans.get(value));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
    // 建立path与method的映射关系
    private void doHandlerMapping() {
        if (null == beans || beans.isEmpty()) {
            System.out.println("no class is instance......");
            return;
        }

        for (Map.Entry<String,Object> map : beans.entrySet()) {
            // 获取当前对象
            Object instance = map.getValue();
            // 获取当前类
            Class<?> clazz = instance.getClass();
            // 获取当前注解为CarryController的类
            if (clazz.isAnnotationPresent(CarryController.class)) {
                // 获取类上的路劲
                CarryRequestMapping clazzRm = clazz.getAnnotation(CarryRequestMapping.class);
                String clazzPath = clazzRm.value();

                // 处理方法
                Method[] methods = clazz.getMethods();
                for (Method method : methods) {
                    if (method.isAnnotationPresent(CarryRequestMapping.class)) {
                        // 获取方法上的路劲
                        CarryRequestMapping methodRm = method.getAnnotation(CarryRequestMapping.class);
                        String methodPath = methodRm.value();
                        // 将类上的路劲+方法上的路劲设置为key，方法设置为vaule
                        handlerMap.put(clazzPath + methodPath, method);
                    }
                }
            }
        }
    }
}
