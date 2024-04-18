package Entities;

import java.time.LocalDateTime;

public class Reservation {
    int id;
    private LocalDateTime createdAt;

    private User user;
    private Event event;

    public Reservation() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Reservation(int id, LocalDateTime createdAt, User user, Event event) {
        this.id = id;
        this.createdAt = createdAt;
        this.user = user;
        this.event = event;
    }

    public Reservation(LocalDateTime createdAt, User user, Event event) {
        this.createdAt = createdAt;
        this.user = user;
        this.event = event;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", user=" + user +
                ", event=" + event +
                '}';
    }
}



