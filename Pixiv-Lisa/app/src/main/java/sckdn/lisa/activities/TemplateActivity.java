package sckdn.lisa.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;

import sckdn.lisa.R;
import sckdn.lisa.databinding.ActivityFragmentBinding;
import sckdn.lisa.fragments.FragmentCollection;
import sckdn.lisa.fragments.FragmentColors;
import sckdn.lisa.fragments.FragmentComment;
import sckdn.lisa.fragments.FragmentDoing;
import sckdn.lisa.fragments.FragmentDownload;
import sckdn.lisa.fragments.FragmentEditAccount;
import sckdn.lisa.fragments.FragmentEditFile;
import sckdn.lisa.fragments.FragmentFollowUser;
import sckdn.lisa.fragments.FragmentHistory;
import sckdn.lisa.fragments.FragmentImageDetail;
import sckdn.lisa.fragments.FragmentLikeIllust;
import sckdn.lisa.fragments.FragmentListSimpleUser;
import sckdn.lisa.fragments.FragmentLocalUsers;
import sckdn.lisa.fragments.FragmentLogin;
import sckdn.lisa.fragments.FragmentMangaSeries;
import sckdn.lisa.fragments.FragmentMangaSeriesDetail;
import sckdn.lisa.fragments.FragmentRecmdIllust;
import sckdn.lisa.fragments.FragmentRecmdUser;
import sckdn.lisa.fragments.FragmentRelatedIllust;
import sckdn.lisa.fragments.FragmentSearch;
import sckdn.lisa.fragments.FragmentSearchUser;
import sckdn.lisa.fragments.FragmentSettings;
import sckdn.lisa.fragments.FragmentUserIllust;
import sckdn.lisa.fragments.FragmentUserInfo;
import sckdn.lisa.fragments.FragmentUserManga;
import sckdn.lisa.fragments.FragmentViewPager;
import sckdn.lisa.fragments.FragmentWalkThrough;
import sckdn.lisa.fragments.FragmentWebView;
import sckdn.lisa.fragments.FragmentWorkSpace;
import sckdn.lisa.helper.BackHandlerHelper;
import sckdn.lisa.model.IllustsBean;
import sckdn.lisa.utils.Params;

public class TemplateActivity extends BaseActivity<ActivityFragmentBinding> implements ColorPickerDialogListener {

    public static final String EXTRA_FRAGMENT = "dataType";
    public static final String EXTRA_KEYWORD = "keyword";
    protected Fragment childFragment;
    private String dataType;

    @Override
    protected void initBundle(Bundle bundle) {
        dataType = bundle.getString(EXTRA_FRAGMENT);
    }

    protected Fragment createNewFragment() {
        Intent intent = getIntent();
        if (!TextUtils.isEmpty(dataType)) {
            switch (dataType) {
                case "登录注册":
                    return new FragmentLogin();
                case "相关作品": {
                    int id = intent.getIntExtra(Params.ILLUST_ID, 0);
                    String title = intent.getStringExtra(Params.ILLUST_TITLE);
                    return FragmentRelatedIllust.newInstance(id, title);
                }
                case "浏览记录":
                    return new FragmentHistory();
                case "网页链接": {
                    String url = intent.getStringExtra(Params.URL);
                    String title = intent.getStringExtra(Params.TITLE);
                    Boolean preferPreserve = intent.getBooleanExtra(Params.PREFER_PRESERVE, false);
                    return FragmentWebView.newInstance(title, url, preferPreserve);
                }
                case "设置":
                    return new FragmentSettings();
                case "推荐用户":
                    return new FragmentRecmdUser();
                case "搜索用户": {
                    String keyword = intent.getStringExtra(EXTRA_KEYWORD);
                    return FragmentSearchUser.newInstance(keyword);
                }
                case "相关评论": {
                    int id = intent.getIntExtra(Params.ILLUST_ID, 0);
                    String title = intent.getStringExtra(Params.ILLUST_TITLE);
                    return FragmentComment.newInstance(id, title);
                }
                case "账号管理":
                    return new FragmentLocalUsers();
                case "画廊":
                    return new FragmentWalkThrough();
                case "正在关注":
                    return FragmentFollowUser.newInstance(
                            getIntent().getIntExtra(Params.USER_ID, 0),
                            Params.TYPE_PUBLUC, true);
                case "搜索":
                    return new FragmentSearch();
                case "详细信息":
                    return new FragmentUserInfo();
                case "喜欢这个作品的用户":
                    return FragmentListSimpleUser.newInstance((IllustsBean) intent.getSerializableExtra(Params.CONTENT));
                case "插画作品":
                    return FragmentUserIllust.newInstance(intent.getIntExtra(Params.USER_ID, 0),
                            true);
                case "漫画作品":
                    return FragmentUserManga.newInstance(intent.getIntExtra(Params.USER_ID, 0),
                            true);
                case "插画/漫画收藏":
                    return FragmentLikeIllust.newInstance(intent.getIntExtra(Params.USER_ID, 0),
                            Params.TYPE_PUBLUC, true);
                case "下载管理":
                    return new FragmentDownload();
                case "推荐漫画":
                    return FragmentRecmdIllust.newInstance("漫画");
                case "图片详情":
                    return FragmentImageDetail.newInstance(intent.getStringExtra(Params.URL));
                case "绑定邮箱":
                    return new FragmentEditAccount();
                case "编辑个人资料":
                    return new FragmentEditFile();
                case "标签屏蔽记录":
                    return FragmentViewPager.newInstance(Params.VIEW_PAGER_MUTED);
                case "漫画系列作品":
                    return FragmentMangaSeries.newInstance(intent.getIntExtra(Params.USER_ID, 0));
                case "漫画系列详情":
                    return FragmentMangaSeriesDetail.newInstance(intent.getIntExtra(Params.ID, 0));
                case "我的作业环境":
                    return new FragmentWorkSpace();
                case "任务中心":
                    return new FragmentDoing();
                case "我的插画收藏":
                    return FragmentCollection.newInstance(0);
                case "我的关注":
                    return FragmentCollection.newInstance(2);
                case "主题颜色":
                    return new FragmentColors();
                default:
                    return new Fragment();
            }
        }
        return null;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (childFragment instanceof FragmentWebView) {
            return ((FragmentWebView) childFragment).getAgentWeb().handleKeyEvent(keyCode, event) ||
                    super.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_fragment;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = createNewFragment();
            if (fragment != null) {
                fragmentManager.beginTransaction()
                        .add(R.id.fragment_container, fragment)
                        .commit();
                childFragment = fragment;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (childFragment != null) {
            childFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean hideStatusBar() {
        if ("相关评论".equals(dataType)) {
            return false;
        } else {
            return getIntent().getBooleanExtra("hideStatusBar", true);
        }
    }

    @Override
    public void onColorSelected(int dialogId, int color) {

    }


    @Override
    public void onDialogDismissed(int dialogId) {

    }

    @Override
    public void onBackPressed() {
        if (!BackHandlerHelper.handleBackPress(this)) {
            super.onBackPressed();
        }
    }
}
