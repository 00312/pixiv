package sckdn.lisa.fragments;

import android.text.TextUtils;

import sckdn.lisa.R;
import sckdn.lisa.activities.Lisa;
import sckdn.lisa.database.AppDatabase;
import sckdn.lisa.database.UserEntity;
import sckdn.lisa.databinding.FragmentEditAccountBinding;
import sckdn.lisa.http.NullCtrl;
import sckdn.lisa.http.Retro;
import sckdn.lisa.model.AccountEditResponse;
import sckdn.lisa.model.UserState;
import sckdn.lisa.utils.Common;
import sckdn.lisa.utils.Local;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static sckdn.lisa.activities.Lisa.sUserModel;

public class FragmentEditAccount extends BaseFragment<FragmentEditAccountBinding> {

    private boolean canChangePixivID = false;

    @Override
    public void initLayout() {
        mLayoutID = R.layout.fragment_edit_account;
    }

    @Override
    protected void initData() {
        if (sUserModel == null) {
            Common.showToast("你还没有登录");
            mActivity.finish();
            return;
        }
        baseBind.toolbar.toolbarTitle.setText(R.string.string_250);
        baseBind.toolbar.toolbar.setNavigationOnClickListener(v -> finish());
        Retro.getAppApi().getAccountState(Lisa.sUserModel.getAccess_token())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NullCtrl<UserState>() {
                    @Override
                    public void success(UserState novelDetail) {
                        if (novelDetail.getUser_state() != null) {
                            canChangePixivID = novelDetail.getUser_state().isCan_change_pixiv_id();
                            baseBind.pixivId.setEnabled(canChangePixivID);
                        }
                    }
                });
        if (!TextUtils.isEmpty(Lisa.sUserModel.getUser().getMail_address())) {
            baseBind.emailAddress.setText(Lisa.sUserModel.getUser().getMail_address());
        }
        baseBind.userPassword.setText(Lisa.sUserModel.getUser().getPassword());
        baseBind.pixivId.setText(Lisa.sUserModel.getUser().getAccount());
        baseBind.pixivId.setEnabled(false);
        baseBind.submit.setOnClickListener(v -> submit());
    }

    private void submit() {
        if (canChangePixivID) {
            //可以修改pixivID
            if (TextUtils.isEmpty(baseBind.pixivId.getText().toString())) {
                //pixiv ID为空
                Common.showToast("pixiv ID不能为空");
                return;
            }
            if (TextUtils.isEmpty(baseBind.userPassword.getText().toString())) {
                //新密码为空
                Common.showToast("新密码不能为空");
                return;
            }
            if (TextUtils.isEmpty(baseBind.emailAddress.getText().toString())) {
                //邮箱地址为空
                if (baseBind.pixivId.getText().toString().equals(sUserModel.getUser().getAccount()) &&
                        baseBind.userPassword.getText().toString().equals(sUserModel.getUser().getPassword())) {
                    Common.showToast("你还没有做任何修改");
                } else if (baseBind.pixivId.getText().toString().equals(sUserModel.getUser().getAccount()) &&
                        !baseBind.userPassword.getText().toString().equals(sUserModel.getUser().getPassword())) {
                    Common.showToast("正在修改密码");
                    Retro.getSignApi().changePassword(sUserModel.getAccess_token(),
                            sUserModel.getUser().getPassword(),
                            baseBind.userPassword.getText().toString())
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new NullCtrl<AccountEditResponse>() {
                                @Override
                                public void success(AccountEditResponse accountEditResponse) {
                                    sUserModel.getUser().setPassword(baseBind.userPassword.getText().toString());
                                    saveUser();
                                    mActivity.finish();
                                    Common.showToast("密码修改成功");
                                }
                            });
                } else if (!baseBind.pixivId.getText().toString().equals(sUserModel.getUser().getAccount()) &&
                        baseBind.userPassword.getText().toString().equals(sUserModel.getUser().getPassword())) {
                    Common.showToast("正在修改PixivID");
                    Retro.getSignApi().changePixivID(sUserModel.getAccess_token(),
                            baseBind.pixivId.getText().toString(),
                            sUserModel.getUser().getPassword())
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new NullCtrl<AccountEditResponse>() {
                                @Override
                                public void success(AccountEditResponse accountEditResponse) {
                                    sUserModel.getUser().setAccount(baseBind.pixivId.getText().toString());
                                    saveUser();
                                    mActivity.finish();
                                    Common.showToast("PixivID修改成功");
                                }
                            });
                } else if (!baseBind.pixivId.getText().toString().equals(sUserModel.getUser().getAccount()) &&
                        !baseBind.userPassword.getText().toString().equals(sUserModel.getUser().getPassword())) {
                    Common.showToast("正在修改PixivID 和密码");
                    Retro.getSignApi().changePasswordPixivID(sUserModel.getAccess_token(),
                            baseBind.pixivId.getText().toString(),
                            sUserModel.getUser().getPassword(),
                            baseBind.userPassword.getText().toString())
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new NullCtrl<AccountEditResponse>() {
                                @Override
                                public void success(AccountEditResponse accountEditResponse) {
                                    sUserModel.getUser().setAccount(baseBind.pixivId.getText().toString());
                                    sUserModel.getUser().setPassword(baseBind.userPassword.getText().toString());
                                    saveUser();
                                    mActivity.finish();
                                    Common.showToast("PixivID 和密码修改成功");
                                }
                            });
                }
            } else {
                if (TextUtils.isEmpty(baseBind.pixivId.getText().toString())) {
                    //pixiv ID为空
                    Common.showToast("pixiv ID不能为空");
                    return;
                }
                if (TextUtils.isEmpty(baseBind.userPassword.getText().toString())) {
                    //新密码为空
                    Common.showToast("新密码不能为空");
                    return;
                }

                if (baseBind.pixivId.getText().toString().equals(sUserModel.getUser().getAccount()) &&
                        baseBind.userPassword.getText().toString().equals(sUserModel.getUser().getPassword())) {
                    Retro.getSignApi().changeEmail(sUserModel.getAccess_token(),
                            baseBind.emailAddress.getText().toString(),
                            sUserModel.getUser().getPassword())
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new NullCtrl<AccountEditResponse>() {
                                @Override
                                public void success(AccountEditResponse accountEditResponse) {
                                    mActivity.finish();
                                    Common.showToast("验证邮件发送成功！", true);
                                }
                            });
                } else if (!baseBind.pixivId.getText().toString().equals(sUserModel.getUser().getAccount()) &&
                        baseBind.userPassword.getText().toString().equals(sUserModel.getUser().getPassword())) {
                    Retro.getSignApi().changeEmailAndPixivID(sUserModel.getAccess_token(),
                            baseBind.emailAddress.getText().toString(),
                            baseBind.pixivId.getText().toString(),
                            sUserModel.getUser().getPassword())
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new NullCtrl<AccountEditResponse>() {
                                @Override
                                public void success(AccountEditResponse accountEditResponse) {
                                    sUserModel.getUser().setAccount(baseBind.pixivId.getText().toString());
                                    saveUser();
                                    mActivity.finish();
                                    Common.showToast("验证邮件发送成功！", true);
                                }
                            });
                } else if (baseBind.pixivId.getText().toString().equals(sUserModel.getUser().getAccount()) &&
                        !baseBind.userPassword.getText().toString().equals(sUserModel.getUser().getPassword())) {
                    Retro.getSignApi().changeEmailAndPassword(sUserModel.getAccess_token(),
                            baseBind.emailAddress.getText().toString(),
                            sUserModel.getUser().getPassword(),
                            baseBind.userPassword.getText().toString())
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new NullCtrl<AccountEditResponse>() {
                                @Override
                                public void success(AccountEditResponse accountEditResponse) {
                                    sUserModel.getUser().setPassword(baseBind.userPassword.getText().toString());
                                    saveUser();
                                    mActivity.finish();
                                    Common.showToast("验证邮件发送成功！", true);
                                }
                            });
                } else if (!baseBind.pixivId.getText().toString().equals(sUserModel.getUser().getAccount()) &&
                        !baseBind.userPassword.getText().toString().equals(sUserModel.getUser().getPassword())) {
                    Retro.getSignApi().edit(
                            sUserModel.getAccess_token(),
                            baseBind.emailAddress.getText().toString(),
                            baseBind.pixivId.getText().toString(),
                            sUserModel.getUser().getPassword(),
                            baseBind.userPassword.getText().toString())
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new NullCtrl<AccountEditResponse>() {
                                @Override
                                public void success(AccountEditResponse accountEditResponse) {
                                    sUserModel.getUser().setPassword(baseBind.userPassword.getText().toString());
                                    sUserModel.getUser().setAccount(baseBind.pixivId.getText().toString());
                                    saveUser();
                                    mActivity.finish();
                                    Common.showToast("验证邮件发送成功！", true);
                                }
                            });
                }
            }
        } else {
            //不可以修改pixivID
            if (TextUtils.isEmpty(baseBind.userPassword.getText().toString())) {
                //新密码为空
                Common.showToast("新密码不能为空");
                return;
            }
            if (TextUtils.isEmpty(baseBind.emailAddress.getText().toString())) {
                //邮箱地址为空
                if (baseBind.userPassword.getText().toString().equals(sUserModel.getUser().getPassword())) {
                    Common.showToast("你还没有做任何修改");
                } else {
                    Common.showToast("正在修改密码");
                    Retro.getSignApi().changePassword(sUserModel.getAccess_token(),
                            sUserModel.getUser().getPassword(),
                            baseBind.userPassword.getText().toString())
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new NullCtrl<AccountEditResponse>() {
                                @Override
                                public void success(AccountEditResponse accountEditResponse) {
                                    sUserModel.getUser().setPassword(baseBind.userPassword.getText().toString());
                                    saveUser();
                                    mActivity.finish();
                                    Common.showToast("密码修改成功");
                                }
                            });
                }
            } else {
                //邮箱地址不为空
                if (baseBind.emailAddress.getText().toString().equals(sUserModel.getUser().getMail_address())) {
                    if (baseBind.userPassword.getText().toString().equals(sUserModel.getUser().getPassword())) {
                        Common.showToast("你还没有做任何修改");
                    } else {
                        Common.showToast("正在修改密码");
                        Retro.getSignApi().changePassword(sUserModel.getAccess_token(),
                                sUserModel.getUser().getPassword(),
                                baseBind.userPassword.getText().toString())
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new NullCtrl<AccountEditResponse>() {
                                    @Override
                                    public void success(AccountEditResponse accountEditResponse) {
                                        sUserModel.getUser().setPassword(baseBind.userPassword.getText().toString());
                                        saveUser();
                                        mActivity.finish();
                                        Common.showToast("密码修改成功");
                                    }
                                });
                    }
                } else {
                    if (baseBind.userPassword.getText().toString().equals(sUserModel.getUser().getPassword())) {
                        Retro.getSignApi().changeEmail(sUserModel.getAccess_token(),
                                baseBind.emailAddress.getText().toString(),
                                sUserModel.getUser().getPassword())
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new NullCtrl<AccountEditResponse>() {
                                    @Override
                                    public void success(AccountEditResponse accountEditResponse) {
                                        mActivity.finish();
                                        Common.showToast("验证邮件发送成功！", true);
                                    }
                                });
                    } else {
                        Retro.getSignApi().changeEmailAndPassword(
                                sUserModel.getAccess_token(),
                                baseBind.emailAddress.getText().toString(),
                                sUserModel.getUser().getPassword(),
                                baseBind.userPassword.getText().toString())
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new NullCtrl<AccountEditResponse>() {
                                    @Override
                                    public void success(AccountEditResponse accountEditResponse) {
                                        sUserModel.getUser().setPassword(baseBind.userPassword.getText().toString());
                                        saveUser();
                                        mActivity.finish();
                                        Common.showToast("验证邮件发送成功！", true);
                                    }
                                });
                    }
                }
            }
        }
    }

    private void saveUser() {
        Local.saveUser(sUserModel);
        UserEntity userEntity = new UserEntity();
        userEntity.setLoginTime(System.currentTimeMillis());
        userEntity.setUserID(sUserModel.getUser().getId());
        userEntity.setUserGson(Lisa.sGson.toJson(sUserModel));
        AppDatabase.getAppDatabase(mContext).downloadDao().insertUser(userEntity);
    }
}
