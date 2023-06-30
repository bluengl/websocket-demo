package com.ngl.websocketdemo.handler;

import java.util.HashMap;
import java.util.Map;

public class MyTest2 {

    static Map<String, Object> test1(Test1 test1){
        System.out.println("test1");
        test1.queryData(new HashMap<>());
        return null;
    }

    static Map<String, Object> test2(Test1 test1){
        System.out.println("test2");
        test1.queryData(new HashMap<>());
        return null;
    }

    public static void main(String[] args) {
        test1(new MyTest1());
        test2(new MyTest3());
    }

}
