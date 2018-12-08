package com.freehand.sample.api;

import com.freehand.sample.db.Datum;
import com.freehand.sample.db.MultipleResource;
import com.freehand.sample.db.User;
import com.freehand.sample.db.UserList;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by anupamchugh on 09/01/17.
 */

public interface APIInterface {

    @GET("/api/unknown")
    Observable<UserList> doGetListResources();

    @POST("/api/users")
    Observable<User> createUser(@Body User user);

    @GET("/api/users?")
    Observable<MultipleResource<Datum>> doGetUserList(@Query("page") String page);

    @FormUrlEncoded
    @POST("/api/users?")
    Observable<MultipleResource<Datum>> doCreateUserWithField(@Field("name") String name, @Field("job") String job);
}
