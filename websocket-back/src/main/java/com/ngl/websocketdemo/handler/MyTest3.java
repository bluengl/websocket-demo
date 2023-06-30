package com.ngl.websocketdemo.handler;

import java.util.List;
import java.util.Map;

public class MyTest3 implements Test1 {
    @Override
    public List<Map<String, Object>> queryData(Map<String, Object> record) {
        System.out.println("test3 queryData");
        return null;
    }

    @Override
    public List<Map<String, Object>> save(Map<String, Object> record) {
        System.out.println("test3 save");
        return null;
    }
}
