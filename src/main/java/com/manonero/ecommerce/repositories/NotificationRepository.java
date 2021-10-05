package com.manonero.ecommerce.repositories;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.manonero.ecommerce.entities.Notification;
import com.manonero.ecommerce.entities.UserNotificationView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class NotificationRepository implements INotificationRepository {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Notification save(Notification notification) {
        return entityManager.merge(notification);
    }

    @Override
    public List<UserNotificationView> selectFilter(Integer userId, Boolean hasRead, Date beginDate, Date endDate) {
        String mainQueryStr = "SELECT * FROM uv_user_notification un";
        String whereQueryString = "";
        if (userId != null) {
            whereQueryString += " WHERE un.user_id=" + userId;
        }
        if(hasRead != null) {
            if (whereQueryString.equals("")) {
                whereQueryString += " WHERE un.has_read = " + (hasRead ? 1 : 0);
            }else {
                whereQueryString += " AND un.has_read = " + (hasRead ? 1 : 0);
            }
        }
        if (beginDate != null) {
            if (whereQueryString.equals("")) {
                whereQueryString += " WHERE un.created_at >= '" + new SimpleDateFormat("yyyy-MM-dd").format(beginDate)
                        + "'";
            } else {
                whereQueryString += " AND un.created_at >= '" + new SimpleDateFormat("yyyy-MM-dd").format(beginDate)
                        + "'";
            }
        }
        if (endDate != null) {
            if (whereQueryString.equals("")) {
                whereQueryString += " WHERE un.created_at <= '" + new SimpleDateFormat("yyyy-MM-dd").format(endDate)
                        + "'";
            } else {
                whereQueryString += " AND un.created_at <= '" + new SimpleDateFormat("yyyy-MM-dd").format(endDate)
                        + "'";
            }
        }
        mainQueryStr += whereQueryString + " ORDER BY un.notification_id DESC";
        Query query = entityManager.createNativeQuery(mainQueryStr, UserNotificationView.class);
        @SuppressWarnings("unchecked")
        List<UserNotificationView> notifications = query.getResultList();
        return notifications;
    }

    @Override
    public Notification selectById(int id) {
        return entityManager.find(Notification.class, id);
    }
    
}
