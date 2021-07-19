package sckdn.lisa.fragments;

import android.os.Bundle;

import sckdn.lisa.adapters.BaseAdapter;
import sckdn.lisa.adapters.IAdapter;
import sckdn.lisa.repo.RemoteRepo;
import sckdn.lisa.databinding.FragmentBaseListBinding;
import sckdn.lisa.databinding.RecyIllustStaggerBinding;
import sckdn.lisa.model.ListIllust;
import sckdn.lisa.model.IllustsBean;
import sckdn.lisa.repo.RankIllustRepo;
import sckdn.lisa.utils.Params;

/**
 * illust / manga 排行榜都用这个
 */
public class FragmentRankIllust extends NetListFragment<FragmentBaseListBinding,
        ListIllust, IllustsBean> {

    private static final String[] API_TITLES = new String[]{"day", "week",
            "month", "week_original"};
    private static final String[] API_TITLES_MANGA = new String[]{"day_manga",
            "week_manga", "month_manga"};


    private int mIndex = -1;
    private boolean isManga = false;
    private String queryDate = "";

    public static FragmentRankIllust newInstance(int index, String date, boolean isManga) {
        Bundle args = new Bundle();
        args.putInt(Params.INDEX, index);
        args.putBoolean(Params.MANGA, isManga);
        args.putString(Params.DAY, date);
        FragmentRankIllust fragment = new FragmentRankIllust();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initBundle(Bundle bundle) {
        mIndex = bundle.getInt(Params.INDEX);
        queryDate = bundle.getString(Params.DAY);
        isManga = bundle.getBoolean(Params.MANGA);
    }

    @Override
    public boolean showToolbar() {
        return false;
    }

    @Override
    public RemoteRepo<ListIllust> repository() {
        return new RankIllustRepo(isManga ? API_TITLES_MANGA[mIndex] : API_TITLES[mIndex], queryDate);
    }

    @Override
    public BaseAdapter<IllustsBean, RecyIllustStaggerBinding> adapter() {
        return new IAdapter(allItems, mContext);
    }

    @Override
    public void initRecyclerView() {
        staggerRecyclerView();
    }
}
