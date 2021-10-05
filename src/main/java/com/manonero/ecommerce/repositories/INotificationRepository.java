package com.manonero.ecommerce.repositories;

import java.util.Date;
import java.util.List;

import com.manonero.ecommerce.entities.Notification;
import com.manonero.ecommerce.entities.UserNotificationView;

public interface INotificationRepository {
    List<UserNotificationView> selectFilter(Integer userId, Boolean hasRead, Date beginDate, Date endDate);
    Notification save(Notification notification);
    Notification selectById(int id);
}
