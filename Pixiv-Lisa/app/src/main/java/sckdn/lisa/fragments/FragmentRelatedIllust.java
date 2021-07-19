package sckdn.lisa.fragments;

import android.os.Bundle;

import androidx.databinding.ViewDataBinding;

import sckdn.lisa.R;
import sckdn.lisa.adapters.BaseAdapter;
import sckdn.lisa.adapters.IAdapter;
import sckdn.lisa.repo.RemoteRepo;
import sckdn.lisa.databinding.FragmentBaseListBinding;
//import ceui.lisa.database.FeatureEntity;
import sckdn.lisa.model.ListIllust;
import sckdn.lisa.model.IllustsBean;
import sckdn.lisa.repo.RelatedIllustRepo;
import sckdn.lisa.utils.Params;

/**
 * 相关插画
 */
public class FragmentRelatedIllust extends NetListFragment<FragmentBaseListBinding,
        ListIllust, IllustsBean> {

    private int illustID;
    private String mTitle;

    public static FragmentRelatedIllust newInstance(int id, String title) {
        Bundle args = new Bundle();
        args.putInt(Params.ILLUST_ID, id);
        args.putString(Params.ILLUST_TITLE, title);
        FragmentRelatedIllust fragment = new FragmentRelatedIllust();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initBundle(Bundle bundle) {
        illustID = bundle.getInt(Params.ILLUST_ID);
        mTitle = bundle.getString(Params.ILLUST_TITLE);
    }
    @Override
    public void initRecyclerView() {
        staggerRecyclerView();
    }

    @Override
    public BaseAdapter<?, ? extends ViewDataBinding> adapter() {
        return new IAdapter(allItems, mContext);
    }

    @Override
    public RemoteRepo<ListIllust> repository() {
        return new RelatedIllustRepo(illustID);
    }

    @Override
    public String getToolbarTitle() {
        return mTitle + getString(R.string.string_231);
    }
}
