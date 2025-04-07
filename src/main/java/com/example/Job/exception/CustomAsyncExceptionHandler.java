package com.example.Job.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

public class CustomAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(CustomAsyncExceptionHandler.class);

    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        log.error("Exception message - " + ex.getMessage());
        log.error("Method name - " + method.getName());

        for (Object param : params) {
            log.error("Parameter value - " + param);
        }

    }
}
