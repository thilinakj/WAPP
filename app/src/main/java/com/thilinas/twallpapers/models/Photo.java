package com.thilinas.twallpapers.models;

import java.util.Date;

/**
 * Created by Thilina on 20-Feb-17.
 */

public class Photo {

    int id;
    String pId;
    int likes;
    int disLikes;
    Date addedDate;
    String sDate;
    String name;
    String url;
    Boolean isFav;

    public Photo(){

    }

    public Photo(int id, String pId, String name, String url, int likes, int disLikes, String sDate ) {
        this.id = id;
        this.pId = pId;
        this.likes = likes;
        this.disLikes = disLikes;
        this.addedDate = addedDate;
        this.sDate = sDate;
        this.name = name;
        this.url = url;
    }

    public Photo(String pId, String name, String url, int likes, int disLikes, Date addedDate, String sDate ) {
        this.pId = pId;
        this.likes = likes;
        this.disLikes = disLikes;
        this.addedDate = addedDate;
        this.sDate = sDate;
        this.name = name;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDisLikes() {
        return disLikes;
    }

    public void setDisLikes(int disLikes) {
        this.disLikes = disLikes;
    }

    public Date getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(Date addedDate) {
        this.addedDate = addedDate;
    }

    public String getsDate() {
        return sDate;
    }

    public void setsDate(String sDate) {
        this.sDate = sDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getFav() {
        return isFav;
    }

    public void setFav(Boolean fav) {
        isFav = fav;
    }
}
