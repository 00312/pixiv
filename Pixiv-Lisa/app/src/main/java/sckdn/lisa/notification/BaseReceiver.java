package sckdn.lisa.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import sckdn.lisa.adapters.BaseAdapter;
import sckdn.lisa.interfaces.Starable;

public abstract class BaseReceiver<Item extends Starable> extends BroadcastReceiver {

    protected BaseAdapter<Item, ?> mAdapter;

    public BaseReceiver(BaseAdapter<Item, ?> adapter) {
        mAdapter = adapter;
    }

    public interface CallBack{
        void onReceive(Context context, Intent intent);
    }
}
