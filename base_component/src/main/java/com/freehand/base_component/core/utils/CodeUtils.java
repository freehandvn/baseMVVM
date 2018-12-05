package com.freehand.base_component.core.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.annotation.RequiresPermission;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;

import com.github.pwittchen.reactivenetwork.library.rx2.Connectivity;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.github.pwittchen.reactivenetwork.library.rx2.internet.observing.InternetObservingSettings;
import com.github.pwittchen.reactivenetwork.library.rx2.internet.observing.error.ErrorHandler;
import com.github.pwittchen.reactivenetwork.library.rx2.internet.observing.strategy.SocketInternetObservingStrategy;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;

/**
 * Created by minhpham on 7/13/17.
 * Purpose:utility for quick code
 * Copyright © 2017 Pham Duy Minh. All rights reserved.
 */
public class CodeUtils {
    public static final String IGNORE_VAL = "null";

    public static boolean set(Object object, String fieldName, Object fieldValue) {
        if (object == null) return false;
        Class<?> clazz = object.getClass();
        while (clazz != null) {
            Field field;
            try {
                field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                if ((Double.class.isAssignableFrom(field.getType()) || double.class.isAssignableFrom(field.getType())) && fieldValue != null) {
                    if (fieldValue instanceof Number) {
                        field.set(object, ((Number) fieldValue).doubleValue());
                        return true;
                    }

                }
                if ((float.class.isAssignableFrom(field.getType()) || Float.class.isAssignableFrom(field.getType())) && fieldValue != null) {
                    if (fieldValue instanceof Number) {
                        field.set(object, ((Number) fieldValue).floatValue());
                        return true;
                    }
                }
                if ((long.class.isAssignableFrom(field.getType()) || Long.class.isAssignableFrom(field.getType())) && fieldValue != null) {
                    if (fieldValue instanceof Number) {
                        field.set(object, ((Number) fieldValue).longValue());
                        return true;
                    }
                }
                if ((int.class.isAssignableFrom(field.getType()) || Integer.class.isAssignableFrom(field.getType())) && fieldValue != null) {
                    if (fieldValue instanceof Number) {
                        field.set(object, ((Number) fieldValue).intValue());
                        return true;
                    }

                }
                field.set(object, fieldValue);
                return true;
            } catch (NoSuchFieldException e) {
//                e.printStackTrace();
                clazz = clazz.getSuperclass();
            } catch (Exception e) {
//                e.printStackTrace();
                return false;
            }finally {
                field = null;
            }
        }
        return false;
    }

    public static boolean fillValue(Object object, JSONObject json) {
        if (json == null || object == null) return false;
        Iterator<String> keys = json.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            String value = json.optString(key);
            if (TextUtils.isEmpty(value) || IGNORE_VAL.equals(value.trim().toLowerCase())) {
                CodeUtils.set(object, key, null);
                continue;
            }
            CodeUtils.set(object, key, json.opt(key));
        }

        return true;
    }

    public static Integer parseInt(String value) {
        try {
            if (TextUtils.isEmpty(value) || IGNORE_VAL.equals(value.toLowerCase())) return null;
            return Integer.parseInt(value);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T, E> T getKeyByValueMap(Map<T, E> map, E value) {
        if (value == null || map == null) return null;
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static Object get(Object object, String fieldName) {
        Class<?> clazz = object.getClass();
        while (clazz != null) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(object);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    @SuppressLint("LongLogTag")
    public static Object getFieldThroughGetterAsStringTransform(Object target, String property) {
        try {
            Method method = target.getClass().getMethod("get" + property.substring(0, 1).toUpperCase() + property.substring(1));
            Object getResult = method.invoke(target);
            return getResult;
        } catch (Exception e) {
            Log.e("getFieldThroughGetterAsStringTransform", "Failed to map property [" + property + "] on object [" + target + "]");
            e.printStackTrace();
            return null;
        }
    }

    public static Map<String, Object> exportProperties(Object object, String... properties) {
        if (object == null || properties.length == 0) return null;
        Map<String, Object> result = new HashMap<>();
        for (String prop : properties) {
            result.put(prop, get(object, prop));
        }

        return result;
    }

    public static Map<String, Object> exportAllProperties(Object object) {
        if (object == null) return null;
        Map<String, Object> result = new HashMap<>();
        Field[] fields = object.getClass().getDeclaredFields();
        if (fields != null) {
            for (Field field : fields) {
                field.setAccessible(true);
                Object value = get(object, field.getName());
                if (value != null) {
                    result.put(field.getName(), value);
                }
            }
        }
        return result;
    }

    public static JSONObject exportAllProperties2Json(Object object) {
        if (object == null) return null;
        JSONObject result = new JSONObject();
        Field[] fields = object.getClass().getDeclaredFields();
        if (fields != null) {
            for (Field field : fields) {
                field.setAccessible(true);
                Object value = get(object, field.getName());
                if (value != null) {
                    try {
                        result.put(field.getName(), value);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return result;
    }

    public static JSONObject exportJsonProperties(Object object, String... properties) {
        if (object == null || properties.length == 0) return null;
        JSONObject result = new JSONObject();
        for (String prop : properties) {
            try {
                result.putOpt(prop, get(object, prop) + "");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public static ViewGroup findParentById(View child, int parentID) {
        ViewParent parent = child.getParent();
        if (parent == null) return null;
        if (!(parent instanceof ViewGroup)) return null;
        if (((View) parent).getId() == parentID) return (ViewGroup) parent;
        return findParentById((View) parent, parentID);
    }

    public static Observable<String> createTextChangeObservable(final EditText mQueryEditText) {
        Observable<String> textChangeObservable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> emitter) throws Exception {
                final TextWatcher watcher = new SimpleTextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        emitter.onNext(s.toString());
                    }
                };

                mQueryEditText.addTextChangedListener(watcher);

                emitter.setCancellable(new io.reactivex.functions.Cancellable() {
                    @Override
                    public void cancel() throws Exception {
                        mQueryEditText.removeTextChangedListener(watcher);
                    }
                });
            }
        });

        return textChangeObservable;
    }

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    public static void hideKeyboard(Activity activity) {
        if (activity == null) return;
        InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager == null) return;
        if (activity.getCurrentFocus() != null) {
            inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    public static Observable<Boolean> getConnectionChannel(Context context) {
        return ReactiveNetwork.observeNetworkConnectivity(context)
                .flatMap(new Function<Connectivity, Observable<Boolean>>() {
                    @Override
                    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
                    public Observable<Boolean> apply(@NonNull Connectivity connectivity) throws Exception {
                        return Observable.just(connectivity.state() == NetworkInfo.State.CONNECTED);
                    }
                });
    }
    @RequiresPermission(Manifest.permission.INTERNET)
    public static Observable<Boolean> getInternetChannel(Context context){
        InternetObservingSettings settings = InternetObservingSettings.builder()
                .strategy(new SocketInternetObservingStrategy())
                .timeout(45*1000)
                .errorHandler(new ErrorHandler() {
                    @Override
                    public void handleError(Exception e, String s) {
                        e.printStackTrace();
                    }
                })
                .initialInterval(100)
                .interval(4*1000)
                .build();

        return ReactiveNetwork
                .observeInternetConnectivity(settings);
    }

    public final static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static void setStatusBarColor(Activity activity, int color) {
        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintColor(color);
    }

    public static void runOnMainThread(Action action) {
        Completable.fromAction(action).subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread()).subscribe();
    }
}
