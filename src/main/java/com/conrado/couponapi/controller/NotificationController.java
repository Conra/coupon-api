package com.conrado.couponapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {

    @PostMapping("/notifications")
    public ResponseEntity<String> receiveNotification(@RequestBody String body) {
        return ResponseEntity.ok("Notification received: ");
    }
}
