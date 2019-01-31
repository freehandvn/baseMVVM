package com.freehand.sample.db

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

import java.util.ArrayList

/**
 * Created by anupamchugh on 09/01/17.
 */

class MultipleResource<T> {

    @SerializedName("page")
    var page: Int? = null
    @SerializedName("per_page")
    var perPage: Int? = null
    @SerializedName("total")
    var total: Int? = null
    @SerializedName("total_pages")
    var totalPages: Int? = null
    @SerializedName("data")
    var data: List<T> = ArrayList()

    override fun toString(): String {
        return Gson().toJson(this)
    }
}
