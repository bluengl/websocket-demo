package com.ngl.websocketdemo.handler;

import java.util.List;
import java.util.Map;

public class MyTest1 implements Test1 {
    @Override
    public List<Map<String, Object>> queryData(Map<String, Object> record) {
        System.out.println("test1 queryData");
        return null;
    }

    @Override
    public List<Map<String, Object>> save(Map<String, Object> record) {
        System.out.println("test1 save");
        return null;
    }
}
