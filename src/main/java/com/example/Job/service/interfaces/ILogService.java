package com.example.Job.service.interfaces;

public interface ILogService {
    // Ghi log với cấp độ INFO
    public void logInfo(String message);

    // Ghi log với cấp độ DEBUG
    public void logDebug(String message);

    // Ghi log với cấp độ ERROR
    public void logError(String message, Throwable throwable);

    // Ghi log với cấp độ WARN
    public void logWarn(String message);

    // Ghi log với cấp độ TRACE
    public void logTrace(String message);
}
