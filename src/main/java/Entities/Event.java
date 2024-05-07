package Entities;

import java.time.LocalDateTime;

public class Event {
    private int id;
    private String title;
    private String description;
    private String location;
    private LocalDateTime date;
    private String imageUrl;
    private int nbP;
    private User user; // Foreign key referencing User table
    private Category category; // Foreign key referencing Category table

    public Event() {}

    public Event(int id, String title, String description, String location, LocalDateTime date, int nbP, User user, Category category, String imageUrl) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.date = date;
        this.nbP = nbP;
        this.user = user;
        this.category = category;
        this.imageUrl = imageUrl;
    }

    public Event(String title, String description, String location, LocalDateTime date, int nbP, User user, Category category, String imageUrl) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.date = date;
        this.nbP = nbP;
        this.user = user;
        this.category = category;
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getNbP() {
        return nbP;
    }

    public void setNbP(int nbP) {
        this.nbP = nbP;
    }

    public User getUserId() {
        return user;
    }

    public void setUserId(User user) {
        this.user = user;
    }

    public Category getCategoryId() {
        return category;
    }

    public void setCategoryId(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", date=" + date +
                ", nbP=" + nbP +
                ", user=" + user +
                ", categoryId=" + category +
                '}';
    }
}
