package sckdn.lisa.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import sckdn.lisa.database.DownloadEntity;
import sckdn.lisa.interfaces.Callback;
import sckdn.lisa.model.Holder;
import sckdn.lisa.utils.Params;

public class DownloadReceiver<T> extends BroadcastReceiver {

    private Callback<T> mCallback;
    private int type; // 0是通知FragmentDownloading, 1是通知FragmentDownloadFinish
    public static final int NOTIFY_FRAGMENT_DOWNLOADING = 0;
    public static final int NOTIFY_FRAGMENT_DOWNLOAD_FINISH = 1;

    public DownloadReceiver(Callback<T> callback, int type) {
        mCallback = callback;
        this.type = type;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && context != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                if (type == 0) {
                    Holder holder = (Holder) bundle.getSerializable(Params.CONTENT);
                    if (mCallback != null) {
                        mCallback.doSomething((T) holder);
                    }
                } else if (type == 1) {
                    DownloadEntity downloadEntity = (DownloadEntity) bundle.getSerializable(Params.CONTENT);
                    if (mCallback != null) {
                        mCallback.doSomething((T) downloadEntity);
                    }
                }
            }
        }
    }
}
