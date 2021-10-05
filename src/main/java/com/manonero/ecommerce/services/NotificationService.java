package com.manonero.ecommerce.services;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import com.manonero.ecommerce.entities.Notification;
import com.manonero.ecommerce.entities.UserNotificationView;
import com.manonero.ecommerce.repositories.INotificationRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService implements INotificationService {

    @Autowired
    private INotificationRepository notificationRepository;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private int numberNotification;

    @Override
    @Transactional
    public Notification save(Notification notification) {
        if (notification.getId() == 0) {
            notification.setCreatedAt(new Date());
            notification.setHasRead(false);
        }else {
            boolean hasRead = notification.isHasRead();
            notification = getById(notification.getId());
            notification.setHasRead(hasRead);
        }
        Notification noti = notificationRepository.save(notification);
        if(notification.getId() == 0) {
            messagingTemplate.convertAndSend("/topic/notification/" + noti.getTopicId(), noti);
        }
        return noti;
    }

    @Override
    @Transactional
    public List<UserNotificationView> filter(Integer offset, Integer limit, Integer userId, Boolean hasRead,
            Date beginDate, Date endDate) {
        List<UserNotificationView> notifications = notificationRepository.selectFilter(userId, hasRead, beginDate,
                endDate);
        this.numberNotification = notifications.size();
        if (offset != null && limit != null) {
            int ix1 = offset - 1;
            int ix2 = offset + limit - 1;
            ix1 = ix1 >= 0 ? ix1 : 0;
            ix2 = ix2 >= 1 ? ix2 : 1;
            ix1 = this.numberNotification > ix1 ? ix1 : 0;
            ix2 = this.numberNotification > ix2 ? ix2 : this.numberNotification;
            return notifications.subList(ix1, ix2);
        }
        return notifications;
    }

    public int getNumberNotification() {
        return numberNotification;
    }

    @Override
    @Transactional
    public int getNumberUnread(Integer userId) {
        List<UserNotificationView> notifications = notificationRepository.selectFilter(userId, false, null, null);
        return notifications.size();
    }

    @Override
    @Transactional
    public Notification getById(int id) {
        return notificationRepository.selectById(id);
    }

}
