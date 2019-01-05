package com.carrymvc.controller;

import com.carrymvc.annotation.CarryController;
import com.carrymvc.annotation.CarryQualifier;
import com.carrymvc.annotation.CarryRequestMapping;
import com.carrymvc.annotation.CarryRequestParam;
import com.carrymvc.service.MyService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author: lhd
 * @date: 2019/1/5 9:53
 */
@CarryController
@CarryRequestMapping("/carry")
public class MyController {

    @CarryQualifier("myService")
    private MyService myService;

    @CarryRequestMapping("/query")
    public void query(HttpServletRequest request, HttpServletResponse response,
                      @CarryRequestParam("name") String name, @CarryRequestParam("age") Integer age) {

//        try {
//            PrintWriter printWriter = response.getWriter();
            String res = myService.query(name);
            System.out.println(res);
//            printWriter.write(name);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
