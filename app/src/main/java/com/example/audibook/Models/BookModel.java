package com.example.audibook.Models;

public class BookModel {
    String id, name, image, auther, rating, summary, bookcontent, status;
    public BookModel(){

    }

    public BookModel(String id, String name, String image, String auther, String rating, String summary, String bookcontent, String status) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.auther = auther;
        this.rating = rating;
        this.summary = summary;
        this.bookcontent = bookcontent;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getAuther() {
        return auther;
    }

    public String getRating() {
        return rating;
    }

    public String getSummary() {
        return summary;
    }

    public String getBookcontent() {
        return bookcontent;
    }

    public String getStatus() {
        return status;
    }
}
