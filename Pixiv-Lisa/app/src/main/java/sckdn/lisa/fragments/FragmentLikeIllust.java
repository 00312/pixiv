package sckdn.lisa.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import sckdn.lisa.R;
import sckdn.lisa.activities.Lisa;
import sckdn.lisa.adapters.BaseAdapter;
import sckdn.lisa.adapters.IAdapterWithStar;
import sckdn.lisa.repo.RemoteRepo;
import sckdn.lisa.databinding.FragmentBaseListBinding;
import sckdn.lisa.databinding.RecyIllustStaggerBinding;
//import ceui.lisa.database.FeatureEntity;
import sckdn.lisa.model.ListIllust;
import sckdn.lisa.model.IllustsBean;
import sckdn.lisa.notification.BaseReceiver;
import sckdn.lisa.notification.FilterReceiver;
import sckdn.lisa.repo.LikeIllustRepo;
import sckdn.lisa.utils.Params;

/**
 * 某人收藏的插畫
 */
public class FragmentLikeIllust extends NetListFragment<FragmentBaseListBinding,
        ListIllust, IllustsBean> {

    private int userID;
    private String starType, tag = "";
    private boolean showToolbar = false;
    private BroadcastReceiver filterReceiver;

    public static FragmentLikeIllust newInstance(int userID, String starType) {
        return newInstance(userID, starType, false);
    }

    public static FragmentLikeIllust newInstance(int userID, String starType,
                                                 boolean paramShowToolbar) {
        Bundle args = new Bundle();
        args.putInt(Params.USER_ID, userID);
        args.putString(Params.STAR_TYPE, starType);
        args.putBoolean(Params.FLAG, paramShowToolbar);
        FragmentLikeIllust fragment = new FragmentLikeIllust();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initBundle(Bundle bundle) {
        userID = bundle.getInt(Params.USER_ID);
        starType = bundle.getString(Params.STAR_TYPE);
        showToolbar = bundle.getBoolean(Params.FLAG);
    }

    @Override
    public RemoteRepo<ListIllust> repository() {
        return new LikeIllustRepo(userID, starType, tag);
    }

    @Override
    public BaseAdapter<IllustsBean, RecyIllustStaggerBinding> adapter() {
        boolean isOwnPage = Lisa.sUserModel.getUser().getUserId() == userID;
        return new IAdapterWithStar(allItems, mContext).setHideStarIcon(isOwnPage);
    }

    @Override
    public void onAdapterPrepared() {
        super.onAdapterPrepared();
        IntentFilter intentFilter = new IntentFilter();
        filterReceiver = new FilterReceiver(new BaseReceiver.CallBack() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    String type = bundle.getString(Params.STAR_TYPE);
                    if (starType.equals(type)) {
                        tag = bundle.getString(Params.CONTENT);
                        ((LikeIllustRepo) mRemoteRepo).setTag(tag);
                        baseBind.refreshLayout.autoRefresh();
                    }
                }
            }
        });
        intentFilter.addAction(Params.FILTER_ILLUST);
        LocalBroadcastManager.getInstance(mContext).registerReceiver(filterReceiver, intentFilter);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (filterReceiver != null) {
            LocalBroadcastManager.getInstance(mContext).unregisterReceiver(filterReceiver);
        }
    }

    @Override
    public void initRecyclerView() {
        staggerRecyclerView();
    }

    @Override
    public boolean showToolbar() {
        return showToolbar;
    }

    @Override
    public String getToolbarTitle() {
        return showToolbar ? getString(R.string.string_164) : super.getToolbarTitle();
    }
}
