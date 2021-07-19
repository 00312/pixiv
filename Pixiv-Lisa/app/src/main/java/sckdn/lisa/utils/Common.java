package sckdn.lisa.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.Utils;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringChain;
import com.hjq.toast.ToastUtils;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.List;
import java.util.Random;

import sckdn.lisa.R;
import sckdn.lisa.activities.MainActivity;
import sckdn.lisa.activities.Lisa;
import sckdn.lisa.activities.TemplateActivity;
import sckdn.lisa.activities.UserActivity;
import sckdn.lisa.interfaces.UserContainer;
import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

public class Common {

    private static final String[][] safeReplacer = new String[][]{{"|", "%7c"}, {"\\", "%5c"}, {"?", "%3f"},
            {"*", "\u22c6"}, {"<", "%3c"}, {"\"", "%22"}, {":", "%3a"}, {">", "%3e"}, {"/", "%2f"}};

    public static boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isEmpty(List<?> list) {
        return list == null || list.size() == 0;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        if (imm.isActive() && activity.getCurrentFocus() != null) {
            if (activity.getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    public static void logOut(Context context) {
        if (Lisa.sUserModel != null) {
            Intent intent = new Intent(context, TemplateActivity.class);
            intent.putExtra(TemplateActivity.EXTRA_FRAGMENT, "登录注册");
            context.startActivity(intent);
        }
    }

    public static <T> void showLog(T t) {
        Log.d("==lisa== log ==> ", String.valueOf(t));
    }

    public static <T> void showToast(T t) {
        ToastUtils.show(t);
    }

    public static void showToast(int id) {
        ToastUtils.show(id);
    }

    //2成功， 3失败， 4info
    public static <T> void showToast(T t, int type) {
        ToastUtils.show(t);
    }

    public static <T> void showToast(T t, boolean isLong) {
        ToastUtils.show(t);
    }

    public static void copy(Context context, String s) {
        ClipBoardUtils.putTextIntoClipboard(context, s, true);
    }

    public static void copy(Context context, String s, boolean hasHint) {
        ClipBoardUtils.putTextIntoClipboard(context, s, hasHint);
    }

    public static String checkEmpty(String before) {
        return TextUtils.isEmpty(before) ? Lisa.getContext().getString(R.string.no_info) : before;
    }

    public static String checkEmpty(EditText before) {
        if (before != null && before.getText() != null && !TextUtils.isEmpty(before.getText().toString())) {
            return before.getText().toString();
        } else {
            return "";
        }
    }

    public static void animate(LinearLayout linearLayout) {
        SpringChain springChain = SpringChain.create(40, 8, 60, 10);

        int childCount = linearLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View view = linearLayout.getChildAt(i);

            springChain.addSpring(new SimpleSpringListener() {
                @Override
                public void onSpringUpdate(Spring spring) {
                    view.setTranslationX((float) spring.getCurrentValue());
                }
            });
        }

        List<Spring> springs = springChain.getAllSprings();
        for (int i = 0; i < springs.size(); i++) {
            springs.get(i).setCurrentValue(400);
        }
        springChain.setControlSpringIndex(0).getControlSpring().setEndValue(0);
    }

    public static void createDialog(Context context){
        QMUIDialog qmuiDialog = new QMUIDialog.MessageDialogBuilder(context)
                .setTitle(context.getString(R.string.string_188))
                .setSkinManager(QMUISkinManager.defaultInstance(context))
                .create();
        Window window = qmuiDialog.getWindow();
        if (window != null) {
            window.setWindowAnimations(R.style.dialog_animation_scale);
        }
        qmuiDialog.show();
    }



    public static String getResponseBody(Response response) {

        Charset UTF8 = Charset.forName("UTF-8");
        ResponseBody responseBody = response.body();
        BufferedSource source = responseBody.source();
        try {
            source.request(Long.MAX_VALUE); // Buffer the entire body.
        } catch (IOException e) {
            e.printStackTrace();
        }
        Buffer buffer = source.buffer();

        Charset charset = UTF8;
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
            try {
                charset = contentType.charset(UTF8);
            } catch (UnsupportedCharsetException e) {
                e.printStackTrace();
            }
        }
        return buffer.clone().readString(charset);
    }

    public static void showUser(Context context, UserContainer userContainer) {
        Intent intent = new Intent(context, UserActivity.class);
        intent.putExtra(Params.USER_ID, userContainer.getUserId());
        context.startActivity(intent);
    }

    public static boolean isAndroidQ() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;
    }

    public static void restart() {
        Intent intent = new Intent();
        String realActivityClassName = MainActivity.class.getName();
        intent.setComponent(new ComponentName(Utils.getApp(), realActivityClassName));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Utils.getApp().startActivity(intent);
    }

    /**
     * left 0, right 5
     *
     * 结果只有 0 1 2 3 4
     *
     *
     * @param left
     * @param right
     * @return
     */
    public static int flatRandom(int left, int right) {
        Random r = new Random();
        return r.nextInt(right - left) + left;
    }

    public static int flatRandom(int right) {
        return flatRandom(0, right);
    }

    /**
     * 解析主题相关的 attribute 的当前值
     */
    public static int resolveThemeAttribute(Context context, int resId){
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(resId, typedValue, true);
        return typedValue.data;
    }
}
