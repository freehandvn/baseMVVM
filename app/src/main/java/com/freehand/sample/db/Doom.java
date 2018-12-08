package com.freehand.sample.db;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by minhpham on 11/29/18.
 * Purpose: .
 * Copyright © 2018 Pham Duy Minh. All rights reserved.
 */
public class Doom extends RealmObject {
    @PrimaryKey
    @SerializedName("id")
    public Integer id;
    @SerializedName("name")
    public String name;
    @SerializedName("year")
    public Integer year;
    @SerializedName("pantone_value")
    public String pantoneValue;

}
