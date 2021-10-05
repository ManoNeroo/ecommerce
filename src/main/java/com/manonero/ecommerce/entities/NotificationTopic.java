package com.manonero.ecommerce.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "notification_topic")
public class NotificationTopic {
    @Id
    @Column(name = "topic_id")
    private String id;

    public NotificationTopic() {
    }

    public NotificationTopic(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof NotificationTopic)) {
            return false;
        }
        NotificationTopic other = (NotificationTopic) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.NotificationTopic[ id=" + id + " ]";
    }

}
