package sckdn.lisa.fragments;

import sckdn.lisa.R;
import sckdn.lisa.adapters.BaseAdapter;
import sckdn.lisa.adapters.IAdapter;
import sckdn.lisa.repo.RemoteRepo;
import sckdn.lisa.databinding.FragmentBaseListBinding;
import sckdn.lisa.databinding.RecyIllustStaggerBinding;
import sckdn.lisa.model.ListIllust;
import sckdn.lisa.model.IllustsBean;
import sckdn.lisa.repo.WalkThroughRepo;

public class FragmentWalkThrough extends NetListFragment<FragmentBaseListBinding,
        ListIllust, IllustsBean> {

    @Override
    public RemoteRepo<ListIllust> repository() {
        return new WalkThroughRepo();
    }

    @Override
    public BaseAdapter<IllustsBean, RecyIllustStaggerBinding> adapter() {
        return new IAdapter(allItems, mContext);
    }

    @Override
    public String getToolbarTitle() {
        return getString(R.string.string_234);
    }

    @Override
    public void initRecyclerView() {
        staggerRecyclerView();
    }
}
