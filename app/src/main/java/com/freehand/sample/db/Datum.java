package com.freehand.sample.db;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by minhpham on 11/29/18.
 * Purpose: .
 * Copyright © 2018 Pham Duy Minh. All rights reserved.
 */

public class Datum extends RealmObject {
    @PrimaryKey
    @SerializedName("id")
    public Integer id;
    @SerializedName("first_name")
    public String first_name;
    @SerializedName("last_name")
    public String last_name;
    @SerializedName("avatar")
    public String avatar;

}
