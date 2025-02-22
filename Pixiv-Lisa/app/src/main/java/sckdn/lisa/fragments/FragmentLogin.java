package sckdn.lisa.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.blankj.utilcode.util.DeviceUtils;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import java.util.Locale;

import sckdn.lisa.R;
import sckdn.lisa.activities.MainActivity;
import sckdn.lisa.activities.Lisa;
import sckdn.lisa.activities.TemplateActivity;
import sckdn.lisa.databinding.ActivityLoginBinding;
import sckdn.lisa.http.HostManager;
import sckdn.lisa.http.WeissUtil;
import sckdn.lisa.interfaces.FeedBack;
import sckdn.lisa.model.UserModel;
import sckdn.lisa.utils.ClipBoardUtils;
import sckdn.lisa.utils.Common;
import sckdn.lisa.utils.Local;
import sckdn.lisa.utils.Params;

public class FragmentLogin extends BaseFragment<ActivityLoginBinding> {

    public static final String CLIENT_ID = "MOBrBDS8blbauoSck0ZfDbtuzpyT";
    public static final String CLIENT_SECRET = "lsACyCD94FhDUtGTXi3QzcFE2uU1hqtDaKeqrdwj";
    public static final String DEVICE_TOKEN = "pixiv";
    public static final String TYPE_PASSWORD = "password";
    public static final String REFRESH_TOKEN = "refresh_token";
    public static final String AUTH_CODE = "authorization_code";
    public static final String CALL_BACK = "https://app-api.pixiv.net/web/v1/users/auth/pixiv/callback";
    private static final String SIGN_TOKEN = "Bearer l-f9qZ0ZyqSwRyZs8-MymbtWBbSxmCu1pmbOlyisou8";
    private static final String SIGN_REF = "pixiv_android_app_provisional_account";

    private static final String LOGIN_HEAD = "https://app-api.pixiv.net/web/v1/login?code_challenge=";
    private static final String LOGIN_END = "&code_challenge_method=S256&client=pixiv-android";

    private static final String SIGN_HEAD = "https://app-api.pixiv.net/web/v1/provisional-accounts/create?code_challenge=";
    private static final String SIGN_END = "&code_challenge_method=S256&client=pixiv-android";
    private static final int TAPS_TO_BE_A_DEVELOPER = 7;
    private SpringSystem springSystem = SpringSystem.create();
    private Spring rotate;
    private int mHitCountDown;//
    private Toast mHitToast;

    @Override
    public void onResume() {
        super.onResume();
        mHitCountDown = TAPS_TO_BE_A_DEVELOPER;
    }

    @Override
    public void initLayout() {
        mLayoutID = R.layout.activity_login;
    }

    @Override
    public void initView() {
        baseBind.toolbar.setPadding(0, Lisa.statusHeight, 0, 0);
        baseBind.toolbar.inflateMenu(R.menu.login_menu);
        baseBind.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_settings) {
                    Intent intent = new Intent(mContext, TemplateActivity.class);
                    intent.putExtra(TemplateActivity.EXTRA_FRAGMENT, "设置");
                    startActivity(intent);
                    return true;
                } else if (item.getItemId() == R.id.action_import) {
                    String userJson = ClipBoardUtils.getClipboardContent(mContext);
                    if (userJson != null
                            && !TextUtils.isEmpty(userJson)
                            && userJson.contains(Params.USER_KEY)) {
                        Common.showToast("导入成功", 2);
                        UserModel exportUser = Lisa.sGson.fromJson(userJson, UserModel.class);
                        Local.saveUser(exportUser);
                        Lisa.sUserModel = exportUser;
                        Intent intent = new Intent(mContext, MainActivity.class);
                        MainActivity.newInstance(intent, mContext);
                        mActivity.finish();
                    } else {
                        Common.showToast("剪贴板无用户信息", 3);
                    }
                    return true;
                }
                return false;
            }
        });
        setTitle();
        baseBind.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mHitCountDown > 0) {
                    mHitCountDown--;
                    if (mHitCountDown == 0) {
                        showDialog();
                    } else if (mHitCountDown > 0 && mHitCountDown < TAPS_TO_BE_A_DEVELOPER - 2) {
                        if (mHitToast != null) {
                            mHitToast.cancel();
                        }
                        mHitToast = Toast.makeText(mActivity, String.format(Locale.getDefault(),
                                "点击%d次切换版本", mHitCountDown), Toast.LENGTH_SHORT);
                        mHitToast.show();
                    }
                } else {
                    showDialog();
                }
            }
        });
        baseBind.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProxyHint(() -> {
                    String url = LOGIN_HEAD + HostManager.get().getPkce().getChallenge() + LOGIN_END;
                    if (DeviceUtils.isTablet()) {
                        Uri uri = Uri.parse(url);
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(uri);
                        startActivity(intent);
                    } else {
                        if (Lisa.sSettings.wallChina()) {
                            WeissUtil.start();
                            WeissUtil.proxy();
                        }
                        Intent intent = new Intent(mContext, TemplateActivity.class);
                        intent.putExtra(TemplateActivity.EXTRA_FRAGMENT, "网页链接");
                        intent.putExtra(Params.URL, url);
                        intent.putExtra(Params.TITLE, getString(R.string.now_login));
                        intent.putExtra(Params.PREFER_PRESERVE, true);
                        startActivity(intent);
                    }
                });
            }
        });

        baseBind.sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProxyHint(() -> {
                    String url = SIGN_HEAD + HostManager.get().getPkce().getChallenge() + SIGN_END;
                    if (DeviceUtils.isTablet()) {
                        Uri uri = Uri.parse(url);
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(uri);
                        startActivity(intent);
                    } else {
                        if (Lisa.sSettings.wallChina()) {
                            WeissUtil.start();
                            WeissUtil.proxy();
                        }
                        Intent intent = new Intent(mContext, TemplateActivity.class);
                        intent.putExtra(TemplateActivity.EXTRA_FRAGMENT, "网页链接");
                        intent.putExtra(Params.URL, url);
                        intent.putExtra(Params.TITLE, getString(R.string.now_sign));
                        intent.putExtra(Params.PREFER_PRESERVE, true);
                        startActivity(intent);
                    }
                });
            }
        });
        baseBind.hasNoAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignCard();
            }
        });
        baseBind.goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoginCard();
            }
        });
    }

    private void openProxyHint(FeedBack feedBack) {
        QMUIDialog qmuiDialog = new QMUIDialog.MessageDialogBuilder(mContext)
                .setTitle(mContext.getString(R.string.string_143))
                .setMessage(mContext.getString(R.string.string_360))
                .setSkinManager(QMUISkinManager.defaultInstance(mContext))
                .addAction(mContext.getString(R.string.cancel), new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction(mContext.getString(R.string.string_361), new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        feedBack.doSomething();
                        dialog.dismiss();
                    }
                })
                .create();
        Window window = qmuiDialog.getWindow();
        if (window != null) {
            window.setWindowAnimations(R.style.dialog_animation_scale);
        }
        qmuiDialog.show();
    }

    private void setTitle() {
        if (Lisa.getMMKV().decodeBool(Params.USE_DEBUG, false)) {
            baseBind.title.setText("Lisa(测试版)");
        } else {
            baseBind.title.setText("Lisa");
        }
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        String[] titles = new String[]{"使用正式版", "使用测试版"};
        builder.setItems(titles, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    Lisa.getMMKV().encode(Params.USE_DEBUG, false);
                } else if (which == 1) {
                    Lisa.getMMKV().encode(Params.USE_DEBUG, true);

                }
                mHitCountDown = TAPS_TO_BE_A_DEVELOPER;
                setTitle();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void initData() {
        if (Lisa.getMMKV().decodeBool(Params.SHOW_DIALOG, true)) {
            Common.createDialog(mContext);
        }
        rotate = springSystem.createSpring();
        rotate.setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(15, 8));
    }

    public void showSignCard() {
        baseBind.fragmentLogin.setVisibility(View.INVISIBLE);
        baseBind.fragmentSign.setVisibility(View.VISIBLE);
        rotate.setCurrentValue(0);
        baseBind.fragmentSign.setCameraDistance(80000.0f);
        rotate.addListener(new SimpleSpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                baseBind.fragmentSign.setRotationY((float) spring.getCurrentValue());
            }

            @Override
            public void onSpringAtRest(Spring spring) {

            }
        });
        rotate.setEndValue(360.0f);
    }

    public void showLoginCard() {
        baseBind.fragmentSign.setVisibility(View.INVISIBLE);
        baseBind.fragmentLogin.setVisibility(View.VISIBLE);
        rotate.setCurrentValue(0);
        baseBind.fragmentLogin.setCameraDistance(80000.0f);
        rotate.addListener(new SimpleSpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                baseBind.fragmentLogin.setRotationY((float) spring.getCurrentValue());
            }

            @Override
            public void onSpringAtRest(Spring spring) {

            }
        });
        rotate.setEndValue(360.0f);
    }
}
