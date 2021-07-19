package sckdn.lisa.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;

import sckdn.lisa.activities.SearchActivity;
import sckdn.lisa.adapters.BaseAdapter;
import sckdn.lisa.adapters.TagAdapter;
import sckdn.lisa.repo.RemoteRepo;
import sckdn.lisa.databinding.FragmentBaseListBinding;
import sckdn.lisa.databinding.RecyTagGridBinding;
import sckdn.lisa.interfaces.OnItemClickListener;
import sckdn.lisa.model.ListTrendingtag;
import sckdn.lisa.repo.HotTagRepo;
import sckdn.lisa.utils.DensityUtil;
import sckdn.lisa.utils.Params;
import sckdn.lisa.view.TagItemDecoration;


public class FragmentHotTag extends NetListFragment<FragmentBaseListBinding,
        ListTrendingtag, ListTrendingtag.TrendTagsBean> {

    private String contentType = "";

    public static FragmentHotTag newInstance(String type) {
        Bundle args = new Bundle();
        args.putString(Params.CONTENT_TYPE, type);
        FragmentHotTag fragment = new FragmentHotTag();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initBundle(Bundle bundle) {
        contentType = bundle.getString(Params.CONTENT_TYPE);
    }

    @Override
    public void initRecyclerView() {
        GridLayoutManager manager = new GridLayoutManager(mContext, 3);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0) {
                    return 3;
                } else {
                    return 1;
                }
            }
        });
        baseBind.recyclerView.setLayoutManager(manager);
        baseBind.recyclerView.addItemDecoration(new TagItemDecoration(
                3, DensityUtil.dp2px(1.0f), false));
    }

    @Override
    public RemoteRepo<ListTrendingtag> repository() {
        return new HotTagRepo(contentType);
    }

    @Override
    public BaseAdapter<ListTrendingtag.TrendTagsBean, RecyTagGridBinding> adapter() {
        return new TagAdapter(allItems, mContext).setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position, int viewType) {
                Intent intent = new Intent(mContext, SearchActivity.class);
                intent.putExtra(Params.KEY_WORD, allItems.get(position).getTag());
                intent.putExtra(Params.INDEX, Params.TYPE_ILLUST.equals(contentType) ? 0 : 2);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean showToolbar() {
        return false;
    }
}
