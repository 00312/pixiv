package sckdn.lisa.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.material.appbar.AppBarLayout;

import sckdn.lisa.R;
import sckdn.lisa.databinding.ActivityNewUserBinding;
import sckdn.lisa.fragments.FragmentHolder;
import sckdn.lisa.http.NullCtrl;
import sckdn.lisa.http.Retro;
import sckdn.lisa.interfaces.Display;
import sckdn.lisa.model.UserDetailResponse;
import sckdn.lisa.utils.Common;
import sckdn.lisa.utils.GlideUtil;
import sckdn.lisa.utils.Params;
import sckdn.lisa.utils.PixivOperate;
import sckdn.lisa.viewmodel.UserViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class UActivity extends BaseActivity<ActivityNewUserBinding> implements Display<UserDetailResponse> {

    private int userID;
    private UserViewModel mUserViewModel;

    @Override
    protected int initLayout() {
        return R.layout.activity_new_user;
    }

    @Override
    protected void initView() {
        Wave wave = new Wave();
        baseBind.progress.setIndeterminateDrawable(wave);
        baseBind.toolbar.setPadding(0, Lisa.statusHeight, 0, 0);
        baseBind.toolbar.setNavigationOnClickListener(v -> finish());
        baseBind.toolbarLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                final int offset = baseBind.toolbarLayout.getHeight() - Lisa.statusHeight - Lisa.toolbarHeight;
                baseBind.appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                    @Override
                    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                        if (Math.abs(verticalOffset) < 15) {
                            baseBind.centerHeader.setAlpha(1.0f);
                            baseBind.toolbarTitle.setAlpha(0.0f);
                        } else if ((offset - Math.abs(verticalOffset)) < 15) {
                            baseBind.centerHeader.setAlpha(0.0f);
                            baseBind.toolbarTitle.setAlpha(1.0f);
                        } else {
                            baseBind.centerHeader.setAlpha(1 + (float) verticalOffset / offset);
                            baseBind.toolbarTitle.setAlpha(-(float) verticalOffset / offset);
                        }
                        Common.showLog(className + verticalOffset);
                    }
                });
                baseBind.toolbarLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    @Override
    protected void initBundle(Bundle bundle) {
        userID = bundle.getInt(Params.USER_ID);
    }

    @Override
    public void initModel() {
        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        mUserViewModel.getUser().observe(this, new Observer<UserDetailResponse>() {
            @Override
            public void onChanged(UserDetailResponse userDetailResponse) {
                invoke(userDetailResponse);
            }
        });
    }

    @Override
    protected void initData() {
        baseBind.progress.setVisibility(View.VISIBLE);
        Retro.getAppApi().getUserDetail(Lisa.sUserModel.getAccess_token(), userID)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NullCtrl<UserDetailResponse>() {
                    @Override
                    public void success(UserDetailResponse user) {
                        mUserViewModel.getUser().setValue(user);
                    }

                    @Override
                    public void must() {
                        baseBind.progress.setVisibility(View.INVISIBLE);
                    }
                });
    }

    @Override
    public boolean hideStatusBar() {
        return true;
    }

    @Override
    public void invoke(UserDetailResponse data) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, FragmentHolder.newInstance())
                .commitNowAllowingStateLoss();

        if (userID == Lisa.sUserModel.getUserId()) {
            baseBind.starUser.setVisibility(View.INVISIBLE);
        } else {
            baseBind.starUser.setVisibility(View.VISIBLE);
            if (data.getUser().isIs_followed()) {
                baseBind.starUser.setText(R.string.string_177);
            } else {
                baseBind.starUser.setText(R.string.string_178);
            }
            baseBind.starUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (data.getUser().isIs_followed()) {
                        baseBind.starUser.setText(R.string.string_178);
                        PixivOperate.postUnFollowUser(data.getUser().getId());
                        data.getUser().setIs_followed(false);
                    } else if(Lisa.sSettings.isPrivateFollow()){
                        baseBind.starUser.setText(R.string.string_177);
                        PixivOperate.postFollowUser(data.getUser().getId(), Params.TYPE_PRIVATE);
                        data.getUser().setIs_followed(true);}
                     else{
                        baseBind.follow.setText(R.string.string_177);
                        PixivOperate.postFollowUser(data.getUser().getId(), Params.TYPE_PUBLUC);
                        data.getUser().setIs_followed(true);
                    }
                }
            });
            baseBind.starUser.setOnLongClickListener(v1 -> {
                if (!data.getUser().isIs_followed()) {
                    baseBind.starUser.setText(R.string.string_177);
                    data.getUser().setIs_followed(true);
                    PixivOperate.postFollowUser(data.getUser().getId(), Params.TYPE_PRIVATE);
                    return true;
                } else {
                    return false;
                }
            });
        }

        baseBind.centerHeader.setVisibility(View.VISIBLE);
        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(800L);
        baseBind.centerHeader.startAnimation(animation);
        if (data.getUser().isIs_premium()) {
            baseBind.vipImage.setVisibility(View.VISIBLE);
        } else {
            baseBind.vipImage.setVisibility(View.GONE);
        }
        Glide.with(mContext).load(GlideUtil.getHead(data.getUser())).into(baseBind.userHead);
        baseBind.userName.setText(data.getUser().getName());
        baseBind.follow.setText(String.valueOf(data.getProfile().getTotal_follow_users()));


        View.OnClickListener follow = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TemplateActivity.class);
                intent.putExtra(Params.USER_ID, data.getUser().getId());
                intent.putExtra(TemplateActivity.EXTRA_FRAGMENT, "正在关注");
                startActivity(intent);
            }
        };
        baseBind.follow.setOnClickListener(follow);
        baseBind.followS.setOnClickListener(follow);

    }
}
