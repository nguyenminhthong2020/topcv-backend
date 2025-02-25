package com.example.Job.service.Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.Job.service.ILogService; // Import interface

@Service
public class LogService implements ILogService {

    // Tạo logger cho class này
    private static final Logger logger = LoggerFactory.getLogger(LogService.class);

    // Ghi log với cấp độ INFO
    @Override
    public void logInfo(String message) {
        logger.info(message);
    }

    // Ghi log với cấp độ DEBUG
    @Override
    public void logDebug(String message) {
        logger.debug(message);
    }

    // Ghi log với cấp độ ERROR
    @Override
    public void logError(String message, Throwable throwable) {
        logger.error(message, throwable);
    }

    // Ghi log với cấp độ WARN
    @Override
    public void logWarn(String message) {
        logger.warn(message);
    }

    // Ghi log với cấp độ TRACE
    @Override
    public void logTrace(String message) {
        logger.trace(message);
    }
}
