package com.ngl.websocketdemo.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.websocket.server.ServerEndpointConfig;

/**
 * @author NGL18
 */
public class MySpringConfigurator extends ServerEndpointConfig.Configurator implements ApplicationContextAware {

    private static volatile ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        MySpringConfigurator.context = applicationContext;
    }

    @Override
    public <T> T getEndpointInstance(Class<T> clazz) {
        return context.getBean(clazz);
    }
}
