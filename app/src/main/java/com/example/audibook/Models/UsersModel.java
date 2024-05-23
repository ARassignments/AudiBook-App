package com.example.audibook.Models;

public class UsersModel {
    String id, name, email, image, role, created_on, status;

    public UsersModel(){

    }

    public UsersModel(String id, String name, String email, String image, String role, String created_on, String status) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.image = image;
        this.role = role;
        this.created_on = created_on;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getImage() {
        return image;
    }

    public String getRole() {
        return role;
    }

    public String getCreated_on() {
        return created_on;
    }

    public String getStatus() {
        return status;
    }
}
