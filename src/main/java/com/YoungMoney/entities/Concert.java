package com.YoungMoney.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Created by stevenburris on 10/28/16.
 */
@Entity
@Table(name = "concerts")
public class Concert {

    @Id
    @GeneratedValue
    int id;

    @Column(nullable = false)
    public String name;

    @Column(nullable = false)
    public LocalDate date;

    @Column(nullable = false)
    public String venue;

    @Column(nullable = false)
    public String city;

    @Column(nullable = false)
    public String state;

    @ManyToOne
    public User user;

    @JsonIgnore
    @Transient
    public boolean isMe;

    @JsonIgnore
    @Transient
    public boolean isGoing;

    public Concert() {
    }

    public Concert(String name, LocalDate date, String venue, String city, String state, User user) {
        this.name = name;
        this.date = date;
        this.venue = venue;
        this.city = city;
        this.state = state;
        this.user = user;
    }

    public Concert(String name, LocalDate date, String venue, User user) {
        this.name = name;
        this.date = date;
        this.venue = venue;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
