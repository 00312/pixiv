 package sckdn.lisa.adapters;

import android.content.Context;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.List;

import sckdn.lisa.R;
import sckdn.lisa.databinding.RecyCardIllustBinding;
import sckdn.lisa.model.IllustsBean;
import sckdn.lisa.utils.GlideUtil;

public class LAdapter extends BaseAdapter<IllustsBean, RecyCardIllustBinding> {

    private int imageSize;

    public LAdapter(List<IllustsBean> targetList, Context context) {
        super(targetList, context);
        imageSize = mContext.getResources().getDisplayMetrics().widthPixels / 3;
    }

    public int getImageSize() {
        return imageSize;
    }

    @Override
    public void initLayout() {
        mLayoutID = R.layout.recy_card_illust;
    }

    @Override
    public void bindData(IllustsBean target, ViewHolder<RecyCardIllustBinding> bindView, int position) {
        ViewGroup.LayoutParams params = bindView.baseBind.imageView.getLayoutParams();
        params.width = imageSize;
        params.height = imageSize;
        bindView.baseBind.imageView.setLayoutParams(params);
        Glide.with(mContext)
                .load(GlideUtil.getMediumImg(target))
                .placeholder(R.color.second_light_bg)
                .into(bindView.baseBind.imageView);
        bindView.itemView.setOnClickListener(view -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(view, position, 0);
            }
        });
    }
}
