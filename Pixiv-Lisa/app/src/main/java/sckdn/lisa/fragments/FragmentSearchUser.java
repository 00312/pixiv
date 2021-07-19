package sckdn.lisa.fragments;

import android.os.Bundle;

import sckdn.lisa.R;
import sckdn.lisa.adapters.BaseAdapter;
import sckdn.lisa.adapters.UAdapter;
import sckdn.lisa.repo.RemoteRepo;
import sckdn.lisa.databinding.FragmentBaseListBinding;
import sckdn.lisa.databinding.RecyUserPreviewBinding;
import sckdn.lisa.model.ListUser;
import sckdn.lisa.model.UserPreviewsBean;
import sckdn.lisa.repo.SearchUserRepo;
import sckdn.lisa.utils.Params;

/**
 * 搜索用户
 */
public class FragmentSearchUser extends NetListFragment<FragmentBaseListBinding,
        ListUser, UserPreviewsBean> {

    private String word;

    public static FragmentSearchUser newInstance(String word) {
        Bundle args = new Bundle();
        args.putString(Params.KEY_WORD, word);
        FragmentSearchUser fragment = new FragmentSearchUser();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initBundle(Bundle bundle) {
        word = bundle.getString(Params.KEY_WORD);
    }

    @Override
    public RemoteRepo<ListUser> repository() {
        return new SearchUserRepo(word);
    }

    @Override
    public BaseAdapter<UserPreviewsBean, RecyUserPreviewBinding> adapter() {
        return new UAdapter(allItems, mContext);
    }

    @Override
    public String getToolbarTitle() {
        return getString(R.string.string_236) + word;
    }
}
