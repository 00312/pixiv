package sckdn.lisa.notification;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import sckdn.lisa.adapters.BaseAdapter;
import sckdn.lisa.interfaces.Starable;
import sckdn.lisa.utils.Params;

public class CommonReceiver extends BaseReceiver<Starable> {

    public CommonReceiver(BaseAdapter<Starable, ?> adapter) {
        super(adapter);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && mAdapter != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                int id = bundle.getInt(Params.ID);
                boolean isLiked = bundle.getBoolean(Params.IS_LIKED);
                mAdapter.setLiked(id, isLiked);
            }
        }
    }
}
