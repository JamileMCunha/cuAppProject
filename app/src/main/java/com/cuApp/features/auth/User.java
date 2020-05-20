package com.cuApp.features.auth;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class User implements Serializable {

    private String uid;
    private String name;
    private String email;
    private String college;
    private String course;
    private String about;
    private String intsub;
    private String image;

    public User() {
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getCollege() {
        return college;
    }

    public String getCourse() {
        return course;
    }

    public String getAbout() {
        return about;
    }

    public String getIntsub() {
        return intsub;
    }

    public String getImage() {
        return image;
    }


    public User withImage(String image) {
        return User.of(uid, name, email, college, course, about, intsub, image);
    }

    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", college='" + college + '\'' +
                ", course='" + course + '\'' +
                ", about='" + about + '\'' +
                ", intsub='" + intsub + '\'' +
                ", image='" + image + '\'' +
                '}';
    }

    public static User of(String uid, String name, String email) {
        User user = new User();
        user.uid = uid;
        user.name = name;
        user.email = email;
        return user;
    }

    public static User of(String uid, String name, String email, String college, String course, String about, String intsub, String image) {
        User user = new User();
        user.uid = uid;
        user.name = name;
        user.email = email;
        user.college = college;
        user.course = course;
        user.about = about;
        user.intsub = intsub;
        user.image = image;
        return user;
    }
}
