package com.freehand.sample.db;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by anupamchugh on 09/01/17.
 */

public class User extends RealmObject {


    @SerializedName("name")
    @PrimaryKey
    public String Name;
    @SerializedName("job")
    public String Job;
    @SerializedName("id")
    public String id;
    @SerializedName("createdAt")
    public String createdAt;

    public User(String name, String job) {
        this.Name = name;
        this.Job = job;
    }

    public User() {
    }
}
