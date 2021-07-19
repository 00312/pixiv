package sckdn.lisa.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.CallSuper;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.footer.FalsifyFooter;

import java.util.UUID;

import sckdn.lisa.adapters.BaseAdapter;
import sckdn.lisa.adapters.EventAdapter;
import sckdn.lisa.adapters.IAdapter;
import sckdn.lisa.adapters.SimpleUserAdapter;
import sckdn.lisa.adapters.UAdapter;
import sckdn.lisa.core.Container;
import sckdn.lisa.core.PageData;
import sckdn.lisa.repo.RemoteRepo;
import sckdn.lisa.http.NullCtrl;
import sckdn.lisa.interfaces.ListShow;
import sckdn.lisa.model.ListIllust;
import sckdn.lisa.interfaces.Starable;
import sckdn.lisa.notification.BaseReceiver;
import sckdn.lisa.notification.CallBackReceiver;
import sckdn.lisa.notification.CommonReceiver;
import sckdn.lisa.utils.Common;
import sckdn.lisa.utils.Params;

/**
 * 联网获取xx列表，
 *
 * @param <Layout>   这个列表的LayoutBinding
 * @param <Response> 这次请求的Response
 * @param <Item>     这个列表的单个Item实体类
 */
public abstract class NetListFragment<Layout extends ViewDataBinding,
        Response extends ListShow<Item>, Item> extends ListFragment<Layout, Item> {

    protected RemoteRepo<Response> mRemoteRepo;
    protected Response mResponse;
    protected BroadcastReceiver mReceiver = null, dataReceiver = null;
    protected String uuid;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uuid = UUID.randomUUID().toString();
    }

    @Override
    public void fresh() {
        if (!mRemoteRepo.localData()) {
            emptyRela.setVisibility(View.INVISIBLE);
            mRemoteRepo.getFirstData(new NullCtrl<Response>() {
                @Override
                public void success(Response response) {
                    Common.showLog("trace 000");
                    if (!isAdded()) {
                        return;
                    }
                    Common.showLog("trace 111");
                    mResponse = response;
                    tryCatchResponse(mResponse);
                    if (!Common.isEmpty(mResponse.getList())) {
                        Common.showLog("trace 222 " + mAdapter.getItemCount());
                        beforeFirstLoad(mResponse.getList());
                        mModel.load(mResponse.getList(), true);
                        allItems = mModel.getContent();
                        onFirstLoaded(mResponse.getList());
                        mRecyclerView.setVisibility(View.VISIBLE);
                        emptyRela.setVisibility(View.INVISIBLE);
                        mAdapter.notifyItemRangeInserted(getStartSize(), mResponse.getList().size());
                        Common.showLog("trace 777 " + mAdapter.getItemCount() + " allItems.size():" + allItems.size() + " modelSize:" + mModel.getContent().size());
                    } else {
                        Common.showLog("trace 333");
                        mRecyclerView.setVisibility(View.INVISIBLE);
                        emptyRela.setVisibility(View.VISIBLE);
                    }
                    Common.showLog("trace 444");
                    mRemoteRepo.setNextUrl(mResponse.getNextUrl());
                    mAdapter.setNextUrl(mResponse.getNextUrl());
                    if (!TextUtils.isEmpty(mResponse.getNextUrl())) {
                        Common.showLog("trace 555");
                        mRefreshLayout.setRefreshFooter(new ClassicsFooter(mContext));
                    } else {
                        Common.showLog("trace 666");
                        mRefreshLayout.setRefreshFooter(new FalsifyFooter(mContext));
                    }
                }

                @Override
                public void must(boolean isSuccess) {
                    mRefreshLayout.finishRefresh(isSuccess);
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    mRecyclerView.setVisibility(View.INVISIBLE);
                    emptyRela.setVisibility(View.VISIBLE);
                }
            });
        } else {
            showDataBase();
        }
    }

    private void tryCatchResponse(Response response) {
        try {
            onResponse(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadMore() {
        if (!TextUtils.isEmpty(mRemoteRepo.getNextUrl())) {
            mRemoteRepo.getNextData(new NullCtrl<Response>() {
                @Override
                public void success(Response response) {
                    if (!isAdded()) {
                        return;
                    }
                    mResponse = response;
                    if (!Common.isEmpty(mResponse.getList())) {
                        beforeNextLoad(mResponse.getList());
                        mModel.load(mResponse.getList(), false);
                        allItems = mModel.getContent();
                        onNextLoaded(mResponse.getList());
                        mAdapter.notifyItemRangeInserted(getStartSize(), mResponse.getList().size());
                    }
                    mRemoteRepo.setNextUrl(mResponse.getNextUrl());
                    mAdapter.setNextUrl(mResponse.getNextUrl());
                    if (!TextUtils.isEmpty(mResponse.getNextUrl())) {
                        mRefreshLayout.setRefreshFooter(new ClassicsFooter(mContext));
                    } else {
                        mRefreshLayout.setRefreshFooter(new FalsifyFooter(mContext));
                    }
                }

                @Override
                public void must(boolean isSuccess) {
                    mRefreshLayout.finishLoadMore(isSuccess);
                }
            });
        } else {
            if (mRemoteRepo.showNoDataHint()) {
                Common.showToast("没有更多数据啦");
            }
            mRefreshLayout.finishLoadMore();
        }
    }

    @Override
    protected void initData() {
        mRemoteRepo = (RemoteRepo<Response>) mModel.getBaseRepo();
        super.initData();
    }


    public void showDataBase() {
    }

    public void onResponse(Response response) {

    }

    @CallSuper
    @Override
    public void onAdapterPrepared() {
        mAdapter.setUuid(uuid);
        //注册本地广播
        if (mAdapter instanceof IAdapter || mAdapter instanceof EventAdapter) {
            {
                IntentFilter intentFilter = new IntentFilter();
                mReceiver = new CommonReceiver((BaseAdapter<Starable, ?>) mAdapter);
                intentFilter.addAction(Params.LIKED_ILLUST);
                LocalBroadcastManager.getInstance(mContext).registerReceiver(mReceiver, intentFilter);
            }
            if (mAdapter instanceof IAdapter) {
                IntentFilter intentFilter = new IntentFilter();
                dataReceiver = new CallBackReceiver(new BaseReceiver.CallBack() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        Bundle bundle = intent.getExtras();
                        if (bundle != null) {
                            //接受VActivity传过来的ListIllust 数据
                            PageData pageData = Container.get().getPage(uuid);
                            if (pageData != null) {
                                if (TextUtils.equals(pageData.getUUID(), uuid)) {
                                    ListIllust listIllust = (ListIllust) bundle.getSerializable(Params.CONTENT);
                                    if (listIllust != null){
                                        if (!Common.isEmpty(listIllust.getList())) {
                                            if (!isAdded()) {
                                                return;
                                            }
                                            mResponse = (Response) listIllust;
                                            if (!Common.isEmpty(mResponse.getList())) {
                                                beforeNextLoad(mResponse.getList());
                                                mModel.load(mResponse.getList(), false);
                                                allItems = mModel.getContent();
                                                onNextLoaded(mResponse.getList());
                                                mAdapter.notifyItemRangeInserted(getStartSize(), mResponse.getList().size());
                                            }
                                            mRemoteRepo.setNextUrl(mResponse.getNextUrl());
                                            mAdapter.setNextUrl(mResponse.getNextUrl());
                                            if (!TextUtils.isEmpty(mResponse.getNextUrl())) {
                                                mRefreshLayout.setRefreshFooter(new ClassicsFooter(mContext));
                                            } else {
                                                mRefreshLayout.setRefreshFooter(new FalsifyFooter(mContext));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                });
                intentFilter.addAction(Params.FRAGMENT_ADD_DATA);
                LocalBroadcastManager.getInstance(mContext).registerReceiver(dataReceiver, intentFilter);
            }
        } else if (mAdapter instanceof UAdapter || mAdapter instanceof SimpleUserAdapter) {
            IntentFilter intentFilter = new IntentFilter();
            mReceiver = new CommonReceiver((BaseAdapter<Starable, ?>) mAdapter);
            intentFilter.addAction(Params.LIKED_USER);
            LocalBroadcastManager.getInstance(mContext).registerReceiver(mReceiver, intentFilter);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) {
            LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mReceiver);
        }
        if (dataReceiver != null) {
            LocalBroadcastManager.getInstance(mContext).unregisterReceiver(dataReceiver);
        }
    }
}
