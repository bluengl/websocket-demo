package com.ngl.websocketdemo.handler;

import java.util.List;
import java.util.Map;

public interface Test1 {

    List<Map<String, Object>> queryData(Map<String, Object> record);

    List<Map<String, Object>> save(Map<String, Object> record);

}
