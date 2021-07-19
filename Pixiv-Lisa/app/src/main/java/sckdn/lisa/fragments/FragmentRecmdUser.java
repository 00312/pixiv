package sckdn.lisa.fragments;

import sckdn.lisa.R;
import sckdn.lisa.adapters.BaseAdapter;
import sckdn.lisa.adapters.UAdapter;
import sckdn.lisa.repo.RemoteRepo;
import sckdn.lisa.databinding.FragmentBaseListBinding;
import sckdn.lisa.databinding.RecyUserPreviewBinding;
import sckdn.lisa.model.ListUser;
import sckdn.lisa.model.UserPreviewsBean;
import sckdn.lisa.repo.RecmdUserRepo;

/**
 * 推荐用户
 */
public class FragmentRecmdUser extends NetListFragment<FragmentBaseListBinding,
        ListUser, UserPreviewsBean> {



    @Override
    public RemoteRepo<ListUser> repository() {
        return new RecmdUserRepo(false);
    }

    @Override
    public BaseAdapter<UserPreviewsBean, RecyUserPreviewBinding> adapter() {
        return new UAdapter(allItems, mContext);
    }

    @Override
    public String getToolbarTitle() {
        return getString(R.string.recomment_user);
    }
}
