package sckdn.lisa.fragments;

import android.content.IntentFilter;
import android.view.View;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.List;

import sckdn.lisa.adapters.BaseAdapter;
import sckdn.lisa.adapters.DownloadingAdapter;
import sckdn.lisa.repo.BaseRepo;
import sckdn.lisa.core.DownloadItem;
import sckdn.lisa.repo.LocalRepo;
import sckdn.lisa.core.Manager;
import sckdn.lisa.databinding.FragmentBaseListBinding;
import sckdn.lisa.databinding.RecyDownloadTaskBinding;
import sckdn.lisa.interfaces.Callback;
import sckdn.lisa.model.Holder;
import sckdn.lisa.notification.DownloadReceiver;
import sckdn.lisa.utils.Common;
import sckdn.lisa.utils.Params;

public class FragmentDownloading extends LocalListFragment<FragmentBaseListBinding, DownloadItem> {

    private DownloadReceiver<?> mReceiver;

    @Override
    public BaseAdapter<DownloadItem, RecyDownloadTaskBinding> adapter() {
        return new DownloadingAdapter(allItems, mContext);
    }

    @Override
    public BaseRepo repository() {
        return new LocalRepo<List<DownloadItem>>() {
            @Override
            public List<DownloadItem> first() {
                return Manager.get().getContent();
            }

            @Override
            public List<DownloadItem> next() {
                return null;
            }
        };
    }

    @Override
    public boolean showToolbar() {
        return false;
    }

    @Override
    public void onAdapterPrepared() {
        super.onAdapterPrepared();
        IntentFilter intentFilter = new IntentFilter();
        mReceiver = new DownloadReceiver<>((Callback<Holder>) entity -> {
            if (entity.getCode() == Params.DOWNLOAD_FAILED) {
                final DownloadItem item = entity.getDownloadItem();
                allItems.remove(item);
                item.setProcessed(true);
                allItems.add(item);
                mAdapter.notifyDataSetChanged();
                Common.showLog("收到了失败提醒");
            } else if(entity.getCode() == Params.DOWNLOAD_SUCCESS) {
                int position = entity.getIndex();
                if (position < allItems.size()) {
                    allItems.remove(position);
                    mAdapter.notifyItemRemoved(position);
                    mAdapter.notifyItemRangeChanged(position, allItems.size() - position);
                }

                if (allItems.size() == 0) {
                    emptyRela.setVisibility(View.VISIBLE);
                }
            }
        }, DownloadReceiver.NOTIFY_FRAGMENT_DOWNLOADING);
        intentFilter.addAction(Params.DOWNLOAD_ING);
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) {
            LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mReceiver);
        }
        Manager.get().setCallback(null);
    }
}
