package sckdn.lisa.fragments;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.footer.FalsifyFooter;
import com.scwang.smartrefresh.layout.header.FalsifyHeader;

import java.util.ArrayList;
import java.util.List;

import sckdn.lisa.R;
import sckdn.lisa.activities.MainActivity;
import sckdn.lisa.activities.Lisa;
import sckdn.lisa.activities.TemplateActivity;
import sckdn.lisa.database.AppDatabase;
import sckdn.lisa.database.UserEntity;
import sckdn.lisa.databinding.FragmentLocalUserBinding;
import sckdn.lisa.databinding.RecyLocalUserBinding;
import sckdn.lisa.http.NullCtrl;
import sckdn.lisa.model.UserModel;
import sckdn.lisa.utils.Common;
import sckdn.lisa.utils.GlideUtil;
import sckdn.lisa.utils.Local;
import sckdn.lisa.utils.Params;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static sckdn.lisa.activities.Lisa.sUserModel;

public class FragmentLocalUsers extends BaseFragment<FragmentLocalUserBinding> {

    private List<UserModel> allItems = new ArrayList<>();

    @Override
    public void initLayout() {
        mLayoutID = R.layout.fragment_local_user;
    }

    @Override
    public void initView() {
        baseBind.toolbar.toolbarTitle.setText(R.string.string_251);
        baseBind.toolbar.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.finish();
            }
        });
        baseBind.addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TemplateActivity.class);
                intent.putExtra(TemplateActivity.EXTRA_FRAGMENT, "登录注册");
                startActivity(intent);
            }
        });
        baseBind.refreshLayout.setRefreshFooter(new FalsifyFooter(mContext));
        baseBind.refreshLayout.setRefreshHeader(new FalsifyHeader(mContext));
    }

    @Override
    protected void initData() {
        Observable.create((ObservableOnSubscribe<List<UserEntity>>) emitter -> {
            List<UserEntity> temp = AppDatabase.getAppDatabase(mContext)
                    .downloadDao().getAllUser();
            emitter.onNext(temp);
        })
                .map(new Function<List<UserEntity>, List<UserModel>>() {
                    @Override
                    public List<UserModel> apply(List<UserEntity> userEntities) throws Exception {
                        allItems = new ArrayList<>();
                        for (int i = 0; i < userEntities.size(); i++) {
                            allItems.add(Lisa.sGson.fromJson(userEntities.get(i).getUserGson(), UserModel.class));
                        }
                        return allItems;
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NullCtrl<List<UserModel>>() {
                    @Override
                    public void success(List<UserModel> userModels) {
                        if (userModels.size() != 0) {
                            for (int i = 0; i < userModels.size(); i++) {
                                bindData(userModels.get(i));
                            }
                        }
                    }
                });
    }


    private void bindData(UserModel userModel) {
        RecyLocalUserBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(mContext),
                R.layout.recy_local_user, null, false);
        binding.userName.setText(String.format("%s (%s)", userModel.getUser().getName(),
                userModel.getUser().getAccount()));
        binding.loginTime.setText(TextUtils.isEmpty(userModel.getUser().getMail_address()) ?
                "未绑定邮箱" : userModel.getUser().getMail_address());
        Glide.with(mContext).load(GlideUtil.getHead(userModel.getUser())).into(binding.userHead);
        binding.currentUser.setVisibility(userModel.getUser().getId() ==
                sUserModel.getUser().getId() ? View.VISIBLE : View.GONE);
        binding.exportUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userModel.setLocal_user(Params.USER_KEY);
                String userJson = Lisa.sGson.toJson(userModel);
                Common.copy(mContext, userJson, false);
                Common.showToast("已导出到剪切板", 2);
            }
        });

        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Local.saveUser(userModel);
                //Dev.refreshUser = true;
                Lisa.sUserModel = userModel;
                Intent intent = new Intent(mContext, MainActivity.class);
                MainActivity.newInstance(intent, mContext);
                mActivity.finish();
            }
        });
        baseBind.userList.addView(binding.getRoot());
    }
}
