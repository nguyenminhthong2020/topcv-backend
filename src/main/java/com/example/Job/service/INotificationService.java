package com.example.Job.service;

import com.example.Job.entity.Notification;
import com.example.Job.models.dtos.NotificationRequest;
import com.example.Job.models.dtos.NotificationResponse;

import java.util.List;

public interface INotificationService {

    Notification createNotification(String recipientId,NotificationRequest notificationRequest);

    void sendNotificationToUser(String recipientId, NotificationRequest notificationRequest);

    void sendNotificationToMultipleUser(List<String> recipientIDs, NotificationRequest notificationRequest);

    List<NotificationResponse> getAllNotificationsForUser();

    void markNotificationAsRead(long id);
}
