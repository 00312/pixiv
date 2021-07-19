package sckdn.lisa.fragments;

import android.content.Intent;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import sckdn.lisa.R;
import sckdn.lisa.activities.Lisa;
import sckdn.lisa.activities.UserActivity;
import sckdn.lisa.adapters.BaseAdapter;
import sckdn.lisa.adapters.UserHAdapter;
import sckdn.lisa.repo.BaseRepo;
import sckdn.lisa.database.AppDatabase;
import sckdn.lisa.database.IllustRecmdEntity;
import sckdn.lisa.databinding.FragmentUserHorizontalBinding;
import sckdn.lisa.databinding.RecyUserPreviewHorizontalBinding;
import sckdn.lisa.http.NullCtrl;
import sckdn.lisa.interfaces.OnItemClickListener;
import sckdn.lisa.model.ListUser;
import sckdn.lisa.model.IllustsBean;
import sckdn.lisa.model.UserPreviewsBean;
import sckdn.lisa.repo.RecmdUserRepo;
import sckdn.lisa.utils.Common;
import sckdn.lisa.utils.DensityUtil;
import sckdn.lisa.utils.Params;
import sckdn.lisa.view.LinearItemHorizontalDecoration;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import jp.wasabeef.recyclerview.animators.BaseItemAnimator;
import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;


public class FragmentRecmdUserHorizontal extends NetListFragment<FragmentUserHorizontalBinding,
        ListUser, UserPreviewsBean> {

    @Override
    public void initLayout() {
        mLayoutID = R.layout.fragment_user_horizontal;
    }

    @Override
    public BaseAdapter<UserPreviewsBean, RecyUserPreviewHorizontalBinding> adapter() {
        return new UserHAdapter(allItems, mContext).setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position, int viewType) {
                Intent intent = new Intent(mContext, UserActivity.class);
                intent.putExtra(Params.USER_ID, allItems.get(position).getUser().getId());
                startActivity(intent);
            }
        });
    }

    @Override
    public BaseRepo repository() {
        return new RecmdUserRepo(true);
    }

    @Override
    public BaseItemAnimator animation() {
        FadeInLeftAnimator fade = new FadeInLeftAnimator();
        fade.setAddDuration(animateDuration);
        fade.setRemoveDuration(animateDuration);
        fade.setMoveDuration(animateDuration);
        fade.setChangeDuration(animateDuration);
        return fade;
    }

    @Override
    public void onFirstLoaded(List<UserPreviewsBean> userPreviewsBeans) {
        mRefreshLayout.setEnableRefresh(false);
        mRefreshLayout.setEnableLoadMore(false);
    }

    @Override
    public void initRecyclerView() {
        baseBind.recyclerView.addItemDecoration(new LinearItemHorizontalDecoration(
                DensityUtil.dp2px(12.0f)));
        LinearLayoutManager manager = new LinearLayoutManager(mContext,
                LinearLayoutManager.HORIZONTAL, false);
        baseBind.recyclerView.setLayoutManager(manager);
        baseBind.recyclerView.setHasFixedSize(true);
    }

    @Override
    public void showDataBase() {
        Observable.create((ObservableOnSubscribe<List<IllustRecmdEntity>>) emitter -> {
            List<IllustRecmdEntity> temp = AppDatabase.getAppDatabase(mContext).recmdDao().getAll();
            Thread.sleep(100);
            emitter.onNext(temp);
            emitter.onComplete();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(entities -> {
                    Common.showLog(className + entities.size());
                    List<IllustsBean> temp = new ArrayList<>();
                    for (int i = 0; i < entities.size(); i++) {
                        IllustsBean illustsBean = Lisa.sGson.fromJson(
                                entities.get(i).getIllustJson(), IllustsBean.class);
                        temp.add(illustsBean);
                    }
                    return temp;
                })
                .subscribe(new NullCtrl<List<IllustsBean>>() {
                    @Override
                    public void success(List<IllustsBean> illustsBeans) {
                        for (IllustsBean illustsBean : illustsBeans) {
                            UserPreviewsBean userPreviewsBean = new UserPreviewsBean();
                            userPreviewsBean.setUser(illustsBean.getUser());
                            allItems.add(userPreviewsBean);
                        }
                        mAdapter.notifyItemRangeInserted(mAdapter.headerSize(), allItems.size());
                    }

                    @Override
                    public void must(boolean isSuccess) {
                        baseBind.refreshLayout.finishRefresh(isSuccess);
                        baseBind.refreshLayout.setEnableRefresh(false);
                        baseBind.refreshLayout.setEnableLoadMore(false);
                    }
                });
    }
}
