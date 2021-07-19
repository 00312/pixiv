package sckdn.lisa.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.scwang.smartrefresh.layout.footer.FalsifyFooter;

import java.util.ArrayList;
import java.util.List;

import sckdn.lisa.R;
import sckdn.lisa.activities.Lisa;
import sckdn.lisa.adapters.BaseAdapter;
import sckdn.lisa.adapters.IAdapterWithHeadView;
import sckdn.lisa.repo.RemoteRepo;
import sckdn.lisa.database.AppDatabase;
import sckdn.lisa.database.IllustRecmdEntity;
import sckdn.lisa.databinding.FragmentBaseListBinding;
import sckdn.lisa.databinding.RecyIllustStaggerBinding;
import sckdn.lisa.helper.IllustFilter;
import sckdn.lisa.http.NullCtrl;
import sckdn.lisa.model.ListIllust;
import sckdn.lisa.model.RecmdIllust;
import sckdn.lisa.model.IllustsBean;
import sckdn.lisa.notification.BaseReceiver;
import sckdn.lisa.notification.CallBackReceiver;
import sckdn.lisa.repo.RecmdIllustRepo;
import sckdn.lisa.utils.Common;
import sckdn.lisa.utils.DensityUtil;
import sckdn.lisa.utils.Params;
import sckdn.lisa.view.SpacesItemWithHeadDecoration;
import sckdn.lisa.viewmodel.BaseModel;
import sckdn.lisa.viewmodel.RecmdModel;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class FragmentRecmdIllust extends NetListFragment<FragmentBaseListBinding,
        RecmdIllust, IllustsBean> {

    private String dataType;
    private List<IllustRecmdEntity> localData;
    private BroadcastReceiver relatedReceiver;

    public static FragmentRecmdIllust newInstance(String dataType) {
        Bundle args = new Bundle();
        args.putString(Params.DATA_TYPE, dataType);
        FragmentRecmdIllust fragment = new FragmentRecmdIllust();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initBundle(Bundle bundle) {
        dataType = bundle.getString(Params.DATA_TYPE);
    }

    @Override
    public Class<? extends BaseModel<IllustsBean>> modelClass() {
        return RecmdModel.class;
    }

   @Override
    public RemoteRepo<ListIllust> repository() {

            return new RecmdIllustRepo(dataType);
    }

    @Override
    public BaseAdapter<IllustsBean, RecyIllustStaggerBinding> adapter() {
        return new IAdapterWithHeadView(allItems, mContext, dataType).setShowRelated(true);
    }

    //收藏后加载相关插画
    @Override
    public void onAdapterPrepared() {
        super.onAdapterPrepared();
        IntentFilter intentFilter = new IntentFilter();
        relatedReceiver = new CallBackReceiver(new BaseReceiver.CallBack() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    int index = bundle.getInt(Params.INDEX);
                    ListIllust listIllust = (ListIllust) bundle.getSerializable(Params.CONTENT);
                    if (listIllust != null){
                        if (!Common.isEmpty(listIllust.getList())) {
                            if (!isAdded()) {
                                return;
                            }
                            List<IllustsBean> temp = new ArrayList<>();
                            for (int i = 0; i < listIllust.getList().size(); i++) {
                                listIllust.getList().get(i).setRelated(true);
                                if (i < 5) {
                                    temp.add(listIllust.getList().get(i));
                                } else {
                                    break;
                                }
                            }

                            if (!Common.isEmpty(temp)) {
                                mModel.load(temp, index);
                               // Common.showToast(index);
                                mAdapter.notifyItemRangeInserted(index + 1, temp.size());
                                mAdapter.notifyItemRangeChanged(index + 1, allItems.size() - index - 1);
                            }
                        }
                    }
                }
            }
        });
        intentFilter.addAction(Params.FRAGMENT_ADD_RELATED_DATA);
        LocalBroadcastManager.getInstance(mContext).registerReceiver(relatedReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (relatedReceiver != null) {
            LocalBroadcastManager.getInstance(mContext).unregisterReceiver(relatedReceiver);
        }
    }

    @Override
    public void initRecyclerView() {
        StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(Lisa.sSettings.getLineCount(), StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        baseBind.recyclerView.setLayoutManager(layoutManager);
        baseBind.recyclerView.addItemDecoration(new SpacesItemWithHeadDecoration(DensityUtil.dp2px(8.0f)));
    }

    @Override
    public String getToolbarTitle() {
        return getString(R.string.string_239) + dataType;
    }

    @Override
    public boolean showToolbar() {
        return getString(R.string.string_240).equals(dataType);
    }

    @Override
    public void onFirstLoaded(List<IllustsBean> illustsBeans) {
        ((RecmdModel) mModel).getRankList().clear();
        Observable.create((ObservableOnSubscribe<String>) emitter -> {
            emitter.onNext("开始写入数据库");
            if (allItems != null) {
                if (allItems.size() >= 20) {
                    for (int i = 0; i < 20; i++) {
                        insertViewHistory(allItems.get(i));
                    }
                } else {
                    for (int i = 0; i < allItems.size(); i++) {
                        insertViewHistory(allItems.get(i));
                    }
                }
            }
            emitter.onComplete();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NullCtrl<String>() {
                    @Override
                    public void success(String s) {

                    }
                });
        ((RecmdModel) mModel).getRankList().addAll(((RecmdIllust) mResponse).getRanking_illusts());
        ((IAdapterWithHeadView) mAdapter).setHeadData(((RecmdModel) mModel).getRankList());
    }

    private void insertViewHistory(IllustsBean illustsBean) {
        IllustRecmdEntity illustRecmdEntity = new IllustRecmdEntity();
        illustRecmdEntity.setIllustID(illustsBean.getId());
        illustRecmdEntity.setIllustJson(Lisa.sGson.toJson(illustsBean));
        illustRecmdEntity.setTime(System.currentTimeMillis());
        AppDatabase.getAppDatabase(Lisa.getContext()).recmdDao().insert(illustRecmdEntity);
    }

    @Override
    public void showDataBase() {
        if (Common.isEmpty(localData)) {
            return;
        }
        Observable.create((ObservableOnSubscribe<List<IllustRecmdEntity>>) emitter -> {
            Thread.sleep(100);
            emitter.onNext(localData);
            emitter.onComplete();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(entities -> {
                    Common.showLog(className + entities.size());
                    List<IllustsBean> temp = new ArrayList<>();
                    for (int i = 0; i < entities.size(); i++) {
                        IllustsBean illustsBean = Lisa.sGson.fromJson(
                                entities.get(i).getIllustJson(), IllustsBean.class);
                        if (!IllustFilter.judge(illustsBean)) {
                            temp.add(illustsBean);
                        }
                    }
                    return temp;
                })
                .subscribe(new NullCtrl<List<IllustsBean>>() {
                    @Override
                    public void success(List<IllustsBean> illustsBeans) {
                        allItems.addAll(illustsBeans);
                        ((RecmdModel) mModel).getRankList().addAll(illustsBeans);
                        ((IAdapterWithHeadView) mAdapter).setHeadData(((RecmdModel) mModel).getRankList());
                        mAdapter.notifyItemRangeInserted(mAdapter.headerSize(), allItems.size());
                    }

                    @Override
                    public void must(boolean isSuccess) {
                        baseBind.refreshLayout.finishRefresh(isSuccess);
                        baseBind.refreshLayout.setRefreshFooter(new FalsifyFooter(mContext));
                    }
                });
    }
}
