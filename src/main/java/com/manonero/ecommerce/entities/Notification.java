package com.manonero.ecommerce.entities;

import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private int id;

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

    public Notification() {
    }

    public Notification(int id, String content, String link, boolean hasRead, Date createdAt, String topicId,
            String image, String title) {
        this.id = id;
        this.content = content;
        this.link = link;
        this.hasRead = hasRead;
        this.createdAt = createdAt;
        this.topicId = topicId;
        this.image = image;
        this.title = title;
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

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
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

}
