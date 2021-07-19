package sckdn.lisa.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import sckdn.lisa.activities.ImageDetailActivity;
import sckdn.lisa.activities.Lisa;
import sckdn.lisa.activities.UserActivity;
import sckdn.lisa.adapters.BaseAdapter;
import sckdn.lisa.adapters.DownloadedAdapter;
import sckdn.lisa.repo.BaseRepo;
import sckdn.lisa.repo.LocalRepo;
import sckdn.lisa.database.AppDatabase;
import sckdn.lisa.database.DownloadEntity;
import sckdn.lisa.databinding.FragmentBaseListBinding;
import sckdn.lisa.databinding.RecyDownloadedBinding;
import sckdn.lisa.interfaces.Callback;
import sckdn.lisa.interfaces.OnItemClickListener;
import sckdn.lisa.model.IllustsBean;
import sckdn.lisa.notification.DownloadReceiver;
import sckdn.lisa.utils.Common;
import sckdn.lisa.utils.Params;

public class FragmentDownloadFinish extends LocalListFragment<FragmentBaseListBinding,
        DownloadEntity> {

    private List<IllustsBean> all = new ArrayList<>();
    private List<String> filePaths = new ArrayList<>();
    private DownloadReceiver<?> mReceiver;

    @Override
    public BaseAdapter<DownloadEntity, RecyDownloadedBinding> adapter() {
        return new DownloadedAdapter(allItems, mContext).setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position, int viewType) {
                Common.showLog(className + position + " " + allItems.size());
                if (viewType == 0) {
                    Intent intent = new Intent(mContext, ImageDetailActivity.class);
                    intent.putExtra("illust", (Serializable) filePaths);
                    intent.putExtra("dataType", "下载详情");
                    intent.putExtra("index", position);
                    startActivity(intent);
                } else if (viewType == 1) {
                    Intent intent = new Intent(mContext, UserActivity.class);
                    intent.putExtra(Params.USER_ID, all.get(position).getUser().getId());
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public BaseRepo repository() {
        return new LocalRepo<List<DownloadEntity>>() {
            @Override
            public List<DownloadEntity> first() {
                return AppDatabase.getAppDatabase(mContext).downloadDao().getAll(PAGE_SIZE, 0);
            }

            @Override
            public List<DownloadEntity> next() {
                return AppDatabase.getAppDatabase(mContext)
                        .downloadDao().getAll(PAGE_SIZE, allItems.size());
            }

            @Override
            public boolean hasNext() {
                return true;
            }
        };
    }

    @Override
    public void onFirstLoaded(List<DownloadEntity> illustHistoryEntities) {
        all.clear();
        filePaths.clear();
        for (int i = 0; i < illustHistoryEntities.size(); i++) {
            IllustsBean illustsBean = Lisa.sGson.fromJson(
                    illustHistoryEntities.get(i).getIllustGson(), IllustsBean.class);
            all.add(illustsBean);
            filePaths.add(illustHistoryEntities.get(i).getFilePath());
        }
    }

    @Override
    public void onNextLoaded(List<DownloadEntity> illustHistoryEntities) {
        for (int i = 0; i < illustHistoryEntities.size(); i++) {
            IllustsBean illustsBean = Lisa.sGson.fromJson(
                    illustHistoryEntities.get(i).getIllustGson(), IllustsBean.class);
            Common.showLog(className + "add " + i + illustsBean.getTitle());
            all.add(illustsBean);
            filePaths.add(illustHistoryEntities.get(i).getFilePath());
        }
    }

    @Override
    public boolean showToolbar() {
        return false;
    }

    @Override
    public void onAdapterPrepared() {
        super.onAdapterPrepared();
        IntentFilter intentFilter = new IntentFilter();
        mReceiver = new DownloadReceiver<>((Callback<DownloadEntity>) entity -> {
            mRecyclerView.setVisibility(View.VISIBLE);
            emptyRela.setVisibility(View.INVISIBLE);
            allItems.add(0, entity);
            all.add(Lisa.sGson.fromJson(entity.getIllustGson(), IllustsBean.class));
            filePaths.add(0, entity.getFilePath());
            mAdapter.notifyItemInserted(0);
            mRecyclerView.scrollToPosition(0);
            mAdapter.notifyItemRangeChanged(0, allItems.size());
        }, DownloadReceiver.NOTIFY_FRAGMENT_DOWNLOAD_FINISH);
        intentFilter.addAction(Params.DOWNLOAD_FINISH);
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) {
            LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mReceiver);
        }
    }




    @Override
    public boolean isLazy() {
        return false;
    }
}
