package sckdn.lisa.fragments;


import android.os.Bundle;

import sckdn.lisa.R;
import sckdn.lisa.adapters.BaseAdapter;
import sckdn.lisa.adapters.IAdapter;
import sckdn.lisa.repo.RemoteRepo;
import sckdn.lisa.databinding.FragmentBaseListBinding;
import sckdn.lisa.databinding.RecyIllustStaggerBinding;
//import ceui.lisa.database.FeatureEntity;
import sckdn.lisa.model.ListIllust;
import sckdn.lisa.model.IllustsBean;
import sckdn.lisa.repo.UserMangaRepo;
import sckdn.lisa.utils.Params;

/**
 * 某人創作的漫画
 */
public class FragmentUserManga extends NetListFragment<FragmentBaseListBinding,
        ListIllust, IllustsBean> {

    private int userID;
    private boolean showToolbar = false;

    public static FragmentUserManga newInstance(int userID, boolean paramShowToolbar) {
        Bundle args = new Bundle();
        args.putInt(Params.USER_ID, userID);
        args.putBoolean(Params.FLAG, paramShowToolbar);
        FragmentUserManga fragment = new FragmentUserManga();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initBundle(Bundle bundle) {
        userID = bundle.getInt(Params.USER_ID);
        showToolbar = bundle.getBoolean(Params.FLAG);
    }
    @Override
    public RemoteRepo<ListIllust> repository() {
        return new UserMangaRepo(userID);
    }

    @Override
    public BaseAdapter<IllustsBean, RecyIllustStaggerBinding> adapter() {
        return new IAdapter(allItems, mContext);
    }

    @Override
    public boolean showToolbar() {
        return showToolbar;
    }

    @Override
    public String getToolbarTitle() {
        if (showToolbar) {
            return getString(R.string.string_233);
        } else {
            return super.getToolbarTitle();
        }
    }

    @Override
    public void initRecyclerView() {
        staggerRecyclerView();
    }
}
