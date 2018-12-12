package com.freehand.base_component.core.utils;

import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by minhpham on 5/29/17.
 * Purpose: Parse JWT information
 * Copyright © 2017 Pham Duy Minh. All rights reserved.
 */

public class JWTUtils {
    /**
     *
     * @param token need decode
     * @return array of json decoded with 2 element:
     * - first is header decoded
     * - second is body decoded (we usually use body to application information)
     */
    public static JSONObject[] decoded(String token) {
        try {
            String[] split = token.split("\\.");
            JSONObject[] result = new JSONObject[]{new JSONObject(getJson(split[0])),new JSONObject(getJson(split[1]))};
            return result;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getJson(String strEncoded) throws UnsupportedEncodingException {
        byte[] decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE);
        return new String(decodedBytes, "UTF-8");
    }
}
