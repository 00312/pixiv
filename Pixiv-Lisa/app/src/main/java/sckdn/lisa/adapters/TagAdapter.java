package sckdn.lisa.adapters;

import android.content.Context;
import android.text.TextUtils;

import com.bumptech.glide.Glide;

import java.util.List;

import sckdn.lisa.R;
import sckdn.lisa.databinding.RecyTagGridBinding;
//import ceui.lisa.interfaces.MultiDownload;
import sckdn.lisa.model.ListTrendingtag;
import sckdn.lisa.utils.GlideUtil;

public class TagAdapter extends BaseAdapter<ListTrendingtag.TrendTagsBean, RecyTagGridBinding>  {

    private static final float HEADER_RATIO = 0.66f;
    private static final float CONTENT_RATIO = 1.0f;

    public TagAdapter(List<ListTrendingtag.TrendTagsBean> targetList, Context context) {
        super(targetList, context);
    }

    @Override
    public void initLayout() {
        mLayoutID = R.layout.recy_tag_grid;
    }

    @Override
    public void bindData(ListTrendingtag.TrendTagsBean target, ViewHolder<RecyTagGridBinding> bindView, int position) {
        if (position == 0) {
            bindView.baseBind.illustImage.setHeightRatio(HEADER_RATIO);
            Glide.with(mContext)
                    .load(GlideUtil.getLargeImage(allIllust.get(position).getIllust()))
                    .placeholder(R.color.light_bg)
                    .into(bindView.baseBind.illustImage);
        } else {
            bindView.baseBind.illustImage.setHeightRatio(CONTENT_RATIO);
            Glide.with(mContext)
                    .load(GlideUtil.getMediumImg(allIllust.get(position).getIllust()))
                    .placeholder(R.color.light_bg)
                    .into(bindView.baseBind.illustImage);
        }

        if (TextUtils.isEmpty(allIllust.get(position).getTranslated_name())) {
            bindView.baseBind.chineseTitle.setText("");
        } else {
            bindView.baseBind.chineseTitle.setText(String.format("#%s", allIllust.get(position).getTranslated_name()));
        }
        bindView.baseBind.title.setText(String.format("#%s", allIllust.get(position).getTag()));


        if (mOnItemClickListener != null) {
            bindView.itemView.setOnClickListener(v -> mOnItemClickListener.onItemClick(v, position, 0));
        }
    }

   /* @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public List<IllustsBean> getIllustList() {
        List<IllustsBean> tempList = new ArrayList<>();
        for (int i = 0; i < allIllust.size(); i++) {
            tempList.add(allIllust.get(i).getIllust());
        }
        return tempList;
    }*/
}
