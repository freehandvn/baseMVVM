package com.freehand.base_component.core.utils;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.ArrayRes;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntegerRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.PluralsRes;
import android.support.annotation.StringRes;
import android.support.annotation.StyleableRes;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.concurrent.Callable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Cancellable;
import io.reactivex.functions.Function;

import static com.freehand.base_component.core.ApplicationProvider.application;

/**
 * Created by namhoainguyen on 8/9/17.
 */

public final class ViewUtils {

    public static String getString(@StringRes int stringId) {
        if (stringId == -1)
            return "";
        return application().getResources().getString(stringId);
    }

    public static String[] getStringArray(@ArrayRes int stringId) {
        return application().getResources().getStringArray(stringId);
    }

    public static String getQuantityString(@PluralsRes int stringId, int quantity, Object... objects) {
        return application().getResources().getQuantityString(stringId, quantity, objects);
    }

    public static int getColor(@ColorRes int colorId) {
        return application().getResources().getColor(colorId);
    }

    public static int getDimen(@DimenRes int dimenId) {
        return application().getResources().getDimensionPixelSize(dimenId);
    }

    public static void setFont(@StringRes int stringId, TextView... textViews) {
        setFont(getString(stringId),textViews);
    }

    public static void setFont(String fontPath, TextView... textViews) {
        for (TextView textView : textViews) {
            Typeface type = Typeface.createFromAsset(application().getAssets(),fontPath);
            textView.setTypeface(type);
        }
    }

    public static int getInteger(@IntegerRes int integerId) {
        return application().getResources().getInteger(integerId);
    }

    public static TypedArray getTypedArray(AttributeSet attributeSet, @StyleableRes int[] styleableResId) {
        return application().getTheme().obtainStyledAttributes(attributeSet, styleableResId, 0, 0);

    }

    public static View inflate(@LayoutRes int layoutId) {
        return LayoutInflater.from(application()).inflate(layoutId, null);
    }

    public static View inflate(@LayoutRes int layoutId, ViewGroup parentView, boolean attachToRoot) {
        return LayoutInflater.from(parentView.getContext()).inflate(layoutId, parentView, attachToRoot);
    }

    public static String convertSize(float kb) {
        return convertSize(kb, Unit.KB);
    }

    public static String convertSize(float kb, ViewUtils.Unit unit) {
        if (kb < 1000) return String.format("%.2f " + unit.name().toLowerCase(), (double) kb);
        if (unit == Unit.TB) return "over size";
        return convertSize(kb / 1024f, Unit.values()[unit.ordinal() + 1]);
    }


    public static ViewGroup getRootParent(View view, int id) {
        return CodeUtils.findParentById(view, id);
    }

    public static Point measureViewSize(View view, int maxHeight) {
        view.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int width = view.getMeasuredWidth();
        int height = view.getMeasuredHeight();
        if (maxHeight > 0) {
            height = Math.min(height, maxHeight);
        }
        return new Point(width, height);
    }

    public static void hideKeyboard(Context context) {
        CodeUtils.hideKeyboard(CodeUtils.fromContext(context));
    }

    public static void hideKeyboard(EditText input) {
        input.setInputType(0);
        InputMethodManager imm = (InputMethodManager) application().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
    }

    public static void showKeyboard() {
        InputMethodManager imm = (InputMethodManager)application().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public static void showKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) application().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, 0);
    }

    public static void encreaseTouch(final View button, final int addSize) {
        final View parent = (View) button.getParent();  // button: the view you want to enlarge hit area
        parent.post(new Runnable() {
            public void run() {
                final Rect rect = new Rect();
                button.getHitRect(rect);
                rect.top -= addSize;    // increase top hit area
                rect.left -= addSize;   // increase left hit area
                rect.bottom += addSize; // increase bottom hit area
                rect.right += addSize;  // increase right hit area
                parent.setTouchDelegate(new TouchDelegate(rect, button));
            }
        });
    }

    public static void clickableEffect(View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                    case MotionEvent.ACTION_HOVER_ENTER:
                    case MotionEvent.ACTION_BUTTON_PRESS:
                    case MotionEvent.ACTION_HOVER_MOVE:
                        view.setAlpha(0.5f);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_BUTTON_RELEASE:
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_OUTSIDE:
                    case MotionEvent.ACTION_HOVER_EXIT:
                        view.setAlpha(1);
                        break;
                }
                return view.onTouchEvent(motionEvent);
            }
        });
    }

    /**
     * Post an action to execute on main thread immediately.
     *
     * @param runnable
     */
    public static void post(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }

    /**
     * Post an action to execute on main thread after a period time.
     *
     * @param runnable
     * @param delayMillis
     */
    public static void postDelay(Runnable runnable, long delayMillis) {
        new Handler(Looper.getMainLooper()).postDelayed(runnable, delayMillis);
    }

    /**
     * Gets drawable resource id based on name.
     *
     * @param name
     *
     * @return the drawable res id.
     */
    public static int getDrawableId(String name) {
        Context context = application();
        return context.getResources().getIdentifier(name, "drawable", context.getPackageName());
    }

    /**
     * Gets drawable based on name.
     *
     * @param name
     *
     * @return the drawable object.
     */
    public static Drawable getDrawable(String name) {
        return application().getResources().getDrawable(getDrawableId(name));
    }

    public static Drawable getDrawable(@DrawableRes int resId) {
        return application().getResources().getDrawable(resId);
    }

    public static Observable<View> observeLayoutChange(final View view) {
        return Observable.create(new ObservableOnSubscribe<View>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<View> e) throws Exception {
                view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                            view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        } else {
                            view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                        e.onNext(view);
                    }
                });
            }
        });
    }

    public static void viewFile(String path) {
        if (TextUtils.isEmpty(path)) return;
        File file = new File(path);
        if (!file.exists()) return;
        MimeTypeMap map = MimeTypeMap.getSingleton();
        String ext = MimeTypeMap.getFileExtensionFromUrl(file.getName());
        String type = map.getMimeTypeFromExtension(ext);

        if (type == null)
            type = "*/*";

        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data = FileProvider.getUriForFile(application(),
                application().getPackageName() + ".provider", file);

        intent.setDataAndType(data, type);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        application().startActivity(intent);
    }

    public static void enableFocus(EditText... editTexts) {
        for (EditText editText : editTexts) {
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
            editText.setCursorVisible(true);
            editText.requestFocus();
        }
    }

    public static void disableFocus(EditText... editTexts) {
        for (EditText editText : editTexts) {
            editText.setFocusable(false);
            editText.setFocusableInTouchMode(false);
            editText.setCursorVisible(false);
            editText.clearFocus();
        }
    }

    public static Spanned fromHtml(String html) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(html);
        }
    }

    public static Observable<Spanned> fromHtmlObservable(String html) {
        if (TextUtils.isEmpty(html)) return Observable.empty();
        return Observable.just(html).map(new Function<String, Spanned>() {
            @Override
            public Spanned apply(@NonNull String s) throws Exception {
                return fromHtml(s);
            }
        });
    }

    public static void disableViewTemporarily(final View view, long duration) {
        if (view == null) return;
        view.setEnabled(false);
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setEnabled(true);
            }
        }, duration);
    }

    public static void disableViewWithTransparentTemporarily(final View view, long duration) {
        if (view == null) return;
        view.setEnabled(false);
        view.setAlpha(0.3f);
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setEnabled(true);
                view.setAlpha(1f);
            }
        }, duration);
    }

    public static Observable<Integer> viewPagerPositionChange(final ViewPager viewPager) {
        return Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<Integer> e) throws Exception {
                final ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        e.onNext(position);
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                };
                viewPager.addOnPageChangeListener(pageChangeListener);
                e.setCancellable(new Cancellable() {
                    @Override
                    public void cancel() throws Exception {
                        viewPager.removeOnPageChangeListener(pageChangeListener);
                    }
                });
            }
        });
    }

    public static Single<View> startAnim(final View view, final Animation animation) {
        return Single.create(new SingleOnSubscribe<View>() {
            @Override
            public void subscribe(@NonNull final SingleEmitter<View> e) throws Exception {
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        view.setEnabled(false);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        view.setEnabled(true);
                        e.onSuccess(view);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                view.startAnimation(animation);
            }
        });
    }

    public static Single<ViewPropertyAnimator> startAnimator(final View view, final ViewPropertyAnimator propertyAnimator) {
        return Single.create(new SingleOnSubscribe<ViewPropertyAnimator>() {
            @Override
            public void subscribe(@NonNull final SingleEmitter<ViewPropertyAnimator> e) throws Exception {
                propertyAnimator.setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                        view.setEnabled(false);
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        view.setEnabled(true);
                        e.onSuccess(propertyAnimator);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {
                        view.setEnabled(true);
                        e.onSuccess(propertyAnimator);
                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
                propertyAnimator.start();
            }
        });
    }

    public static Bitmap getBitmapFromView(View view) {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.draw(canvas);
        return bitmap;
    }

    public static void showToast(final String message, @ToastLength final int length) {
        post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(application(), message, length).show();
            }
        });
    }

    public static void showToast(final @StringRes int message, final @ToastLength int length) {
        showToast(getString(message), length);
    }

    public static void changeStatusBarColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }

    public enum Unit {
        B, KB, MB, GB, TB
    }

    public @interface ToastLength {
        int TOAST_LONG = Toast.LENGTH_LONG;
        int TOAST_SHORT = Toast.LENGTH_SHORT;
    }

}
