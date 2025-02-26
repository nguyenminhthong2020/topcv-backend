package com.example.Job.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE) // Annotation này áp dụng cho class (Controller)
@Retention(RetentionPolicy.RUNTIME) // Tồn tại trong runtime để AOP có thể xử lý
public @interface LogRequest {
}