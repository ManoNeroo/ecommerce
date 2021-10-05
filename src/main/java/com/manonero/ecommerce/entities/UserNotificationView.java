package com.manonero.ecommerce.entities;

import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "uv_user_notification")
public class UserNotificationView {
    @Id
    @Column(name = "notification_id")
    private int id;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "notification_content")
    private String content;

    @Column(name = "notification_link")
    private String link;

    @Column(name = "has_read")
    private boolean hasRead;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "topic_id")
    private String topicId;

    @Column(name = "notification_img")
    private String image;

    @Column(name = "notification_title")
    private String title;

    public UserNotificationView() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean isHasRead() {
        return hasRead;
    }

    public void setHasRead(boolean hasRead) {
        this.hasRead = hasRead;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

}
