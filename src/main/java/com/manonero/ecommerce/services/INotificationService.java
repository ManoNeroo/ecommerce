package com.manonero.ecommerce.services;

import java.util.Date;
import java.util.List;

import com.manonero.ecommerce.entities.Notification;
import com.manonero.ecommerce.entities.UserNotificationView;

public interface INotificationService {
    List<UserNotificationView> filter(Integer offset, Integer limit, Integer userId, Boolean hasRead, Date beginDate, Date endDate);
    Notification save(Notification notification);
    int getNumberNotification();
    int getNumberUnread(Integer userId);
    Notification getById(int id);
}
