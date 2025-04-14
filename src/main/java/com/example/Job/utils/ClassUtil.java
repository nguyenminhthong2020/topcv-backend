package com.example.Job.utils;

public class ClassUtil {
    // Check if the class is a primitive wrapper or simple type
    public static boolean isSimpleType(Class<?> clazz) {
        return clazz.isPrimitive() || clazz.equals(String.class) ||
                clazz.equals(Integer.class) || clazz.equals(Long.class) ||
                clazz.equals(Double.class) || clazz.equals(Boolean.class) ||
                clazz.equals(Float.class) || clazz.equals(Character.class)
                || clazz.equals(Byte.class) || clazz.equals(Short.class);

    }
}
