package com.carrymvc.service;

import com.carrymvc.annotation.CarryService;

/**
 * @author: lhd
 * @date: 2019/1/5 9:54
 */
@CarryService("myService")
public class MyServiceImpl implements MyService {

    public String query(String name) {
        return name;
    }
}
