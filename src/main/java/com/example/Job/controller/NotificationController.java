package com.example.Job.controller;

import com.example.Job.entity.Company;
import com.example.Job.models.ResultObject;
import com.example.Job.models.dtos.CompanyRegister;
import com.example.Job.models.dtos.NotificationResponse;
import com.example.Job.models.dtos.NotificationUpdateRequest;
import com.example.Job.service.INotificationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final INotificationService notificationService;

    public NotificationController(INotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/user")
    public ResponseEntity<ResultObject> getNotificationByUser() {


        List<NotificationResponse> userNotifications = notificationService.getAllNotificationsForUser();

        ResultObject<List<NotificationResponse>> result = new ResultObject<>(true, "Get notifications successfully", HttpStatus.OK, userNotifications);

        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @PutMapping("/user/mark-as-read")
    public ResponseEntity<ResultObject> markAsRead(@Valid @RequestBody NotificationUpdateRequest notificationUpdateRequest) {


        notificationService.markNotificationAsRead(notificationUpdateRequest.getNotificationId());

        ResultObject<Void> result = new ResultObject<>(true, "Mark as read successfully", HttpStatus.OK, null);

        return new ResponseEntity<>(result, HttpStatus.OK);

    }
}
