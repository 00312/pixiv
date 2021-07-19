package sckdn.lisa.utils;


import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sckdn.lisa.R;
import sckdn.lisa.activities.OutWakeActivity;
import sckdn.lisa.activities.Lisa;
import sckdn.lisa.activities.VActivity;
import sckdn.lisa.core.Container;
import sckdn.lisa.core.PageData;
import sckdn.lisa.database.AppDatabase;
import sckdn.lisa.database.IllustHistoryEntity;
import sckdn.lisa.database.MuteEntity;
import sckdn.lisa.database.SearchEntity;
import sckdn.lisa.fragments.FragmentLogin;
import sckdn.lisa.http.ErrorCtrl;
import sckdn.lisa.http.NullCtrl;
import sckdn.lisa.http.Retro;
import sckdn.lisa.interfaces.Back;
import sckdn.lisa.model.ListIllust;
import sckdn.lisa.model.IllustSearchResponse;
import sckdn.lisa.model.NullResponse;
import sckdn.lisa.model.TagsBean;
import sckdn.lisa.model.UserModel;
import sckdn.lisa.model.IllustsBean;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;

import static sckdn.lisa.activities.Lisa.sUserModel;
import static com.blankj.utilcode.util.StringUtils.getString;


public class PixivOperate {

    public static void refreshUserData(UserModel userModel, Callback<UserModel> callback) {
        Call<UserModel> call = Retro.getAccountApi().newRefreshToken(
                FragmentLogin.CLIENT_ID,
                FragmentLogin.CLIENT_SECRET,
                FragmentLogin.REFRESH_TOKEN,
                userModel.getRefresh_token(),
                Boolean.TRUE);
        call.enqueue(callback);
    }

    public static void postFollowUser(int userID, String followType) {
        Retro.getAppApi().postFollow(
                sUserModel.getAccess_token(), userID, followType)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ErrorCtrl<NullResponse>() {

                    @Override
                    public void next(NullResponse nullResponse) {
                        Intent intent = new Intent(Params.LIKED_USER);
                        intent.putExtra(Params.ID, userID);
                        intent.putExtra(Params.IS_LIKED, true);
                        LocalBroadcastManager.getInstance(Lisa.getContext()).sendBroadcast(intent);

                        if (followType.equals(Params.TYPE_PUBLUC)) {
                            Common.showToast(getString(R.string.like_success_public));
                        } else {
                            Common.showToast(getString(R.string.like_success_private));
                        }
                    }
                });
    }

    public static void postUnFollowUser(int userID) {
        Retro.getAppApi().postUnFollow(
                sUserModel.getAccess_token(), userID)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ErrorCtrl<NullResponse>() {
                    @Override
                    public void next(NullResponse nullResponse) {
                        Intent intent = new Intent(Params.LIKED_USER);
                        intent.putExtra(Params.ID, userID);
                        intent.putExtra(Params.IS_LIKED, false);
                        LocalBroadcastManager.getInstance(Lisa.getContext()).sendBroadcast(intent);
                        Common.showToast(getString(R.string.cancel_like));
                    }
                });
    }

    public static void postLike(IllustsBean illustsBean, String starType) {
        postLike(illustsBean, starType, false, 0);
    }

    public static void postLike(IllustsBean illustsBean, String starType, boolean showRelated, int index) {
        if (illustsBean == null) {
            return;
        }

        if (illustsBean.isIs_bookmarked()) { //已收藏
            illustsBean.setIs_bookmarked(false);
            Retro.getAppApi().postDislike(sUserModel.getAccess_token(), illustsBean.getId())
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ErrorCtrl<NullResponse>() {
                        @Override
                        public void next(NullResponse nullResponse) {
                            Intent intent = new Intent(Params.LIKED_ILLUST);
                            intent.putExtra(Params.ID, illustsBean.getId());
                            intent.putExtra(Params.IS_LIKED, false);
                            LocalBroadcastManager.getInstance(Lisa.getContext()).sendBroadcast(intent);
                            Common.showToast(getString(R.string.cancel_like_illust));
                        }
                    });
        } else { //没有收藏
            illustsBean.setIs_bookmarked(true);
            Retro.getAppApi().postLike(sUserModel.getAccess_token(), illustsBean.getId(), starType)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ErrorCtrl<NullResponse>() {
                        @Override
                        public void next(NullResponse nullResponse) {
                            Intent intent = new Intent(Params.LIKED_ILLUST);
                            intent.putExtra(Params.ID, illustsBean.getId());
                            intent.putExtra(Params.IS_LIKED, true);
                            LocalBroadcastManager.getInstance(Lisa.getContext()).sendBroadcast(intent);

                        }
                    });

            //收藏的时候，顺便请求这个作品的相关作品
            if (showRelated) {
                Retro.getAppApi().relatedIllust(sUserModel.getAccess_token(), illustsBean.getId())
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new NullCtrl<ListIllust>() {
                            @Override
                            public void success(ListIllust listIllust) {
                                Intent intent = new Intent(Params.FRAGMENT_ADD_RELATED_DATA);
                                intent.putExtra(Params.CONTENT, listIllust);
                                intent.putExtra(Params.INDEX, index);
                                LocalBroadcastManager.getInstance(Lisa.getContext()).sendBroadcast(intent);
                            }
                        });
            }
        }
        PixivOperate.insertIllustViewHistory(illustsBean);
    }
    public static void getIllustByID(UserModel userModel, int illustID, Context context) {
        QMUITipDialog tipDialog = new QMUITipDialog.Builder(context)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在加载")
                .create();
        tipDialog.show();
        Retro.getAppApi().getIllustByID(userModel.getAccess_token(), illustID)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NullCtrl<IllustSearchResponse>() {
                    @Override
                    public void success(IllustSearchResponse illustSearchResponse) {
                        IllustsBean illust = illustSearchResponse.getIllust();
                        if (illust == null) {
                            return;
                        }

                        if (illust.getId() == 0 || !illust.isVisible()) {
                            Common.showToast(R.string.string_206);
                            return;
                        }

                        final PageData pageData = new PageData(
                                Collections.singletonList(illustSearchResponse.getIllust()));
                        Container.get().addPageToMap(pageData);

                        Intent intent = new Intent(context, VActivity.class);
                        intent.putExtra(Params.POSITION, 0);
                        intent.putExtra(Params.PAGE_UUID, pageData.getUUID());
                        context.startActivity(intent);
                    }

                    @Override
                    public void must() {
                        super.must();
                        try {
                            tipDialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public static void getIllustByID(UserModel userModel, int illustID, Context context,
                                     sckdn.lisa.interfaces.Callback<Void> callback) {
        Retro.getAppApi().getIllustByID(userModel.getAccess_token(), illustID)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NullCtrl<IllustSearchResponse>() {
                    @Override
                    public void success(IllustSearchResponse illustSearchResponse) {
                        if (illustSearchResponse.getIllust() != null) {
                            final PageData pageData = new PageData(
                                    Collections.singletonList(illustSearchResponse.getIllust()));
                            Container.get().addPageToMap(pageData);

                            Intent intent = new Intent(context, VActivity.class);
                            intent.putExtra(Params.POSITION, 0);
                            intent.putExtra(Params.PAGE_UUID, pageData.getUUID());
                            context.startActivity(intent);

                            if (callback != null) {
                                callback.doSomething(null);
                            }
                        }
                    }

                    @Override
                    public void must() {
                        super.must();
                        OutWakeActivity.isNetWorking = false;
                    }
                });
    }
    public static void muteTag(TagsBean tagsBean) {
        MuteEntity muteEntity = new MuteEntity();
        String tagName = tagsBean.getName();
        muteEntity.setType(Params.MUTE_TAG);
        muteEntity.setId(tagName.hashCode());
        muteEntity.setTagJson(Lisa.sGson.toJson(tagsBean));
        muteEntity.setSearchTime(System.currentTimeMillis());
        AppDatabase.getAppDatabase(Lisa.getContext()).searchDao().insertMuteTag(muteEntity);
    }

    public static void updateTag(TagsBean tagsBean) {
        MuteEntity muteEntity = new MuteEntity();
        String tagName = tagsBean.getName();
        muteEntity.setType(Params.MUTE_TAG);
        muteEntity.setId(tagName.hashCode());
        muteEntity.setTagJson(Lisa.sGson.toJson(tagsBean));
        muteEntity.setSearchTime(System.currentTimeMillis());
        if (tagsBean.isEffective()) {
            Lisa.getContext().getResources().getString(R.string.string_356);
        } else {
            Lisa.getContext().getResources().getString(R.string.string_357);
        }
        AppDatabase.getAppDatabase(Lisa.getContext()).searchDao().updateMuteTag(muteEntity);
    }



    public static void muteIllust(IllustsBean illust) {
        MuteEntity muteEntity = new MuteEntity();
        muteEntity.setType(Params.MUTE_ILLUST);
        muteEntity.setId(illust.getId());
        muteEntity.setTagJson(Lisa.sGson.toJson(illust));
        muteEntity.setSearchTime(System.currentTimeMillis());
        AppDatabase.getAppDatabase(Lisa.getContext()).searchDao().insertMuteTag(muteEntity);
        Common.showToast("操作成功");
    }
    public static void muteTags(List<TagsBean> tagsBeans) {
        if (tagsBeans == null || tagsBeans.size() == 0) {
            return;
        }

        for (TagsBean tagsBean : tagsBeans) {
            muteTag(tagsBean);
        }
    }

    public static void unMuteTag(TagsBean tagsBean) {
        MuteEntity muteEntity = new MuteEntity();
        String tagName = tagsBean.getName();
        muteEntity.setType(Params.MUTE_TAG);
        muteEntity.setId(tagName.hashCode());
        muteEntity.setTagJson(Lisa.sGson.toJson(tagsBean));
        muteEntity.setSearchTime(System.currentTimeMillis());
        AppDatabase.getAppDatabase(Lisa.getContext()).searchDao().unMuteTag(muteEntity);
        Common.showToast(Lisa.getContext().getString(R.string.string_135));
    }

    public static void insertIllustViewHistory(IllustsBean illust) {
        if (illust == null) {
            return;
        }

        if (illust.getId() > 0) {
            IllustHistoryEntity illustHistoryEntity = new IllustHistoryEntity();
            illustHistoryEntity.setType(0);
            illustHistoryEntity.setIllustID(illust.getId());
            illustHistoryEntity.setIllustJson(Lisa.sGson.toJson(illust));
            illustHistoryEntity.setTime(System.currentTimeMillis());
            Common.showLog("插入了 " + illustHistoryEntity.getIllustID() + " time " + illustHistoryEntity.getTime());
            AppDatabase.getAppDatabase(Lisa.getContext()).downloadDao().insert(illustHistoryEntity);
        }
    }
    public static void insertSearchHistory(String key, int searchType) {
        if(TextUtils.isEmpty(key)){
            return;
        }
        SearchEntity searchEntity = new SearchEntity();
        searchEntity.setKeyword(key);
        searchEntity.setSearchType(searchType);
        searchEntity.setSearchTime(System.currentTimeMillis());
        searchEntity.setId(searchEntity.getKeyword().hashCode() + searchEntity.getSearchType());
        Common.showLog("insertSearchHistory " + searchType + " " + searchEntity.getId());
        AppDatabase.getAppDatabase(Lisa.getContext()).searchDao().insert(searchEntity);
    }

    //筛选作品，只留下未收藏的作品
    public static List<IllustsBean> getListWithoutBooked(ListIllust response) {
        List<IllustsBean> result = new ArrayList<>();
        if (response == null) {
            return result;
        }

        if (response.getList() == null || response.getList().size() == 0) {
            return result;
        }

        for (IllustsBean illustsBean : response.getList()) {
            if (!illustsBean.isIs_bookmarked()) {
                result.add(illustsBean);
            }
        }

        return result;
    }

    //筛选作品，只留下收藏数达到标准的作品
    public static List<IllustsBean> getListWithStarSize(ListIllust response, int starSize) {
        List<IllustsBean> result = new ArrayList<>();
        if (response == null || response.getList() == null || response.getList().size() == 0) {
            return result;
        }

        for (IllustsBean illustsBean : response.getList()) {
            if (illustsBean.getTotal_bookmarks() >= starSize) {
                result.add(illustsBean);
            }
        }

        return result;
    }
    private static Back sBack = null;

    public static void setBack(Back back) {
        sBack = back;
    }
}
