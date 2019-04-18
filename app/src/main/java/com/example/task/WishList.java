package com.example.task;

public class WishList {
    String addedOn;
    int id;

    public WishList(String addedOn, int id) {
        this.addedOn = addedOn;
        this.id = id;
    }

    public WishList() {
    }

    public String getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(String addedOn) {
        this.addedOn = addedOn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
