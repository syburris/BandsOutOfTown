package com.YoungMoney.entities;

import javax.persistence.*;

/**
 * Created by stevenburris on 10/30/16.
 */
@Entity
@Table(name = "going")
public class Go {

    @Id
    @GeneratedValue
    int id;

    @ManyToOne
    User user;

    @ManyToOne
    public Concert concert;

    public Go() {
    }

    public Go(User user, Concert concert) {
        this.user = user;
        this.concert = concert;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Concert getConcert() {
        return concert;
    }

    public void setConcert(Concert concert) {
        this.concert = concert;
    }
}
