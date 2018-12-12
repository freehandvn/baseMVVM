package com.pureprojects.oauth2;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by minhpham on 4/24/17.
 * Purpose: .
 * Copyright © 2017 Pham Duy Minh. All rights reserved.
 */

public class Oauth2Utils {

    public static Observable<Boolean> makeRequest(final OAuth2Parameter link, final Context context) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Boolean> e) throws Exception {
                try {
                    e.onNext(doMainJob(link, context));
                    e.onComplete();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    e.onError(ex);
                }
            }
        });
    }

    private static Boolean doMainJob(OAuth2Parameter link, Context context) {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (OAuth2Manager.getInstance().getSslContext() != null) {
            Log.d("OAuth2Manager", "doMainJob: added SSL Context");
            builder.sslSocketFactory(OAuth2Manager.getInstance().getSslContext().getSocketFactory());
        }
        OkHttpClient client = builder.build();
        Request request = new Request.Builder()
                .url(link.getSite())
                .post(link.getRequestBody())
                .build();


        try {

            Response response = client.newCall(request).execute();
            String body = response.body().string();
            Log.d("Authorize",body );
            JSONObject resultJson = new JSONObject(body);
            //Extract data from JSON Response
            int expiresIn = resultJson.has("expires_in") ? resultJson.optInt("expires_in") : 0;

            String refreshToken = resultJson.has("refresh_token") ? resultJson.optString("refresh_token") : null;
            String tokenType = resultJson.has("token_type") ? resultJson.optString("token_type") : null;
            String accessToken = resultJson.has("access_token") ? resultJson.optString("access_token") : null;

            Log.e("Tokenm", "" + accessToken);
            if (expiresIn > 0 && accessToken != null) {
                Log.i("Authorize", "This is the access Token: " + accessToken + ". It will expires in " + expiresIn + " secs");

                //Calculate date of expiration
                long expireDate = System.currentTimeMillis() + expiresIn;

                ////Store both expires in and access token in shared preferences
                SharedPreferences preferences = context.getSharedPreferences("user_info", 0);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putLong(OAuth2Manager.EXPIRES, expireDate);
                editor.putString(OAuth2Manager.ACCESS_TOKEN, accessToken);
                editor.putString(OAuth2Manager.REFRESH_TOKEN, refreshToken);
                editor.putString(OAuth2Manager.TOKEN_TYPE, tokenType);
                editor.commit();
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

}
