package com.example.Job.annotations;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.example.Job.service.ILogService;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Aspect
@Component
public class LogRequestAspect {

    private final HttpServletRequest request;
    private ILogService _logService;

    public LogRequestAspect(HttpServletRequest request, ILogService logServivce) {
        this.request = request;
        this._logService = logServivce;
    }

    // Áp dụng cho toàn bộ method của Controller có annotation @LogRequest
    @Pointcut("within(@com.example.Job.annotations.LogRequest *)")
    public void loggableControllers() {
    }

    @Before("loggableControllers()")
    public void logRequestDetails(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // Lấy tên Controller & Method
        String controllerName = joinPoint.getTarget().getClass().getSimpleName();
        String actionName = method.getName();

        // Lấy Endpoint (URL được gọi vào)
        String requestURL = request.getRequestURI();

        // Lấy Query Parameters
        Map<String, String[]> parameterMap = request.getParameterMap();
        String queryParams = parameterMap.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + Arrays.toString(entry.getValue()))
                .collect(Collectors.joining(", "));

        // Lấy Request Body (nếu có)
        String requestBody = getRequestBody(joinPoint);

        // Lấy địa chỉ IP
        String ipAddress = request.getRemoteAddr();

        // Log thông tin
        // System.out.println(String.format(
        // "[IP]: %s, [Controller]: %s, [Action]: %s, [URL]: %s, [Params]: %s, [Body]:
        // %s",
        // ipAddress, controllerName, actionName, requestURL, queryParams, requestBody
        // ));

        _logService.logInfo(String.format(
                "[IP]: %s, [Controller]: %s, [Action]: %s, [URL]: %s, [Params]: %s, [Body]: %s",
                ipAddress, controllerName, actionName, requestURL, queryParams, requestBody));
    }

    private String getRequestBody(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args.length == 0)
            return "N/A";

        for (Object arg : args) {
            if (!(arg instanceof HttpServletRequest)) { // Bỏ qua HttpServletRequest
                return arg.toString();
            }
        }
        return "N/A";
    }
}