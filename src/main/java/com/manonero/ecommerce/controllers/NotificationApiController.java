package com.manonero.ecommerce.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.manonero.ecommerce.entities.Notification;
import com.manonero.ecommerce.entities.UserNotificationView;
import com.manonero.ecommerce.models.PaginationResponse;
import com.manonero.ecommerce.models.Response;
import com.manonero.ecommerce.services.INotificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notification")
public class NotificationApiController {

    @Autowired
    private INotificationService notificationService;

    @PostMapping
    public Response save(@RequestBody Notification notification) {
        return new Response(notificationService.save(notification), true);
    }

    @GetMapping("/filter")
    public PaginationResponse filter(@RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer limit, @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) Boolean hasRead, @RequestParam(required = false) String beginDate,
            @RequestParam(required = false) String endDate) {
        if (limit == null) {
            limit = 1;
        }
        if (page == null) {
            page = 1;
        }
        if (page < 1) {
            page = 1;
        }
        int offset = (page * limit) - limit + 1;
        Date bDate = null;
        Date eDate = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            bDate = dateFormat.parse(beginDate);
            eDate = dateFormat.parse(endDate);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        List<UserNotificationView> notifications = notificationService.filter(offset, limit, userId, hasRead, bDate,
                eDate);
        int totalItem = notificationService.getNumberNotification();
        return new PaginationResponse(notifications, true, limit, page, totalItem);
    }

    @GetMapping("/numberunread") 
    public Response getNumberUnread(@RequestParam(required = false) Integer userId) {
        return new Response(notificationService.getNumberUnread(userId), true);
    }
}
