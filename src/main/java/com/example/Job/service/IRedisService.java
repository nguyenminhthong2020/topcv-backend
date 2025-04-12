package com.example.Job.service;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public interface IRedisService {

    // Store an object as JSON
    <T> void set(String key, T value, Long timeout, TimeUnit unit);

    <T> void set(String key, T value);

    // when no TTL is specified, default Time to live will be 7 days
    <T> void set(String key, T value, Duration duration);

    // Get an object (deserialize from JSON)
    <T> T get(String key, Class<T> clazz);

    // Delete a key
    boolean delete(String key);

    // List all keys matching a pattern
    Set<String> listKeys(String pattern);

    // Add to a list
    void addToList(String key, String value);

    // Get all elements in a list
    List<String> getList(String key);

    // Set expiration for a key
    void setExpiration(String key, long timeout, TimeUnit unit);

    // Check if a key exists
    boolean exists(String key);
}
