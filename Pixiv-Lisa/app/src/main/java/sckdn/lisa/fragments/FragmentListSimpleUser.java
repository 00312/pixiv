package sckdn.lisa.fragments;

import android.os.Bundle;

import androidx.databinding.ViewDataBinding;

import sckdn.lisa.adapters.BaseAdapter;
import sckdn.lisa.adapters.SimpleUserAdapter;
import sckdn.lisa.repo.BaseRepo;
import sckdn.lisa.databinding.FragmentBaseListBinding;
import sckdn.lisa.model.ListSimpleUser;
import sckdn.lisa.model.IllustsBean;
import sckdn.lisa.model.UserBean;
import sckdn.lisa.repo.SimpleUserRepo;
import sckdn.lisa.utils.Params;

public class FragmentListSimpleUser extends NetListFragment<FragmentBaseListBinding,
        ListSimpleUser, UserBean> {

    private IllustsBean illustsBean;

    public static FragmentListSimpleUser newInstance(IllustsBean illustsBean) {
        Bundle args = new Bundle();
        args.putSerializable(Params.CONTENT, illustsBean);
        FragmentListSimpleUser fragment = new FragmentListSimpleUser();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initBundle(Bundle bundle) {
        illustsBean = (IllustsBean) bundle.getSerializable(Params.CONTENT);
    }

    @Override
    public BaseAdapter<?, ? extends ViewDataBinding> adapter() {
        return new SimpleUserAdapter(allItems, mContext);
    }

    @Override
    public BaseRepo repository() {
        return new SimpleUserRepo(illustsBean.getId());
    }

    @Override
    public String getToolbarTitle() {
        return "喜欢" + illustsBean.getTitle() + "的用户";
    }
}
