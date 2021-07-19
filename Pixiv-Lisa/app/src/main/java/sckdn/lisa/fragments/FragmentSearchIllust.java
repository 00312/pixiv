package sckdn.lisa.fragments;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import sckdn.lisa.adapters.BaseAdapter;
import sckdn.lisa.adapters.IAdapter;
import sckdn.lisa.repo.BaseRepo;
import sckdn.lisa.databinding.FragmentBaseListBinding;
import sckdn.lisa.model.ListIllust;
import sckdn.lisa.model.IllustsBean;
import sckdn.lisa.repo.SearchIllustRepo;
import sckdn.lisa.utils.Params;
import sckdn.lisa.viewmodel.SearchModel;

public class FragmentSearchIllust extends NetListFragment<FragmentBaseListBinding, ListIllust,
        IllustsBean> {

    private SearchModel searchModel;
    private boolean isPopular = false;

    public static FragmentSearchIllust newInstance(boolean popular) {
        Bundle args = new Bundle();
        args.putBoolean(Params.IS_POPULAR, popular);
        FragmentSearchIllust fragment = new FragmentSearchIllust();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initModel() {
        searchModel = new ViewModelProvider(requireActivity()).get(SearchModel.class);
        super.initModel();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchModel.getNowGo().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                ((SearchIllustRepo) mRemoteRepo).update(searchModel, isPopular);
                mRefreshLayout.autoRefresh();
            }
        });
        // 监测侧滑过滤器中的收藏数选项变化
        searchModel.getStarSize().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                ((SearchIllustRepo) mRemoteRepo).update(searchModel, isPopular);
            }
        });
    }

    @Override
    protected void initBundle(Bundle bundle) {
        isPopular = bundle.getBoolean(Params.IS_POPULAR);
    }

    @Override
    public BaseAdapter<?, ? extends ViewDataBinding> adapter() {
        return new IAdapter(allItems, mContext);
    }

    @Override
    public BaseRepo repository() {
        return new SearchIllustRepo(
                searchModel.getKeyword().getValue(),
                searchModel.getSortType().getValue(),
                searchModel.getSearchType().getValue(),
                searchModel.getStarSize().getValue(),
                isPopular
        );
    }

    @Override
    public boolean showToolbar() {
        return false;
    }

    @Override
    public void initRecyclerView() {
        staggerRecyclerView();
    }
}
