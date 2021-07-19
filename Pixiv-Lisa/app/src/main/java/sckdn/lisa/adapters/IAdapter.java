package sckdn.lisa.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.util.List;

import sckdn.lisa.R;
import sckdn.lisa.activities.Lisa;
import sckdn.lisa.activities.VActivity;
import sckdn.lisa.core.Container;
import sckdn.lisa.core.PageData;
import sckdn.lisa.core.TimeRecord;
import sckdn.lisa.databinding.RecyIllustStaggerBinding;
//import ceui.lisa.interfaces.MultiDownload;
import sckdn.lisa.interfaces.OnItemClickListener;
import sckdn.lisa.model.IllustsBean;
import sckdn.lisa.utils.GlideUtil;
import sckdn.lisa.utils.Params;
import sckdn.lisa.utils.PixivOperate;

public class IAdapter extends BaseAdapter<IllustsBean, RecyIllustStaggerBinding> {

    private static final float MIN_HEIGHT_RATIO = 0.4f;
    private static final float MAX_HEIGHT_RATIO = 3.0f;

    public IAdapter(List<IllustsBean> targetList, Context context) {
        super(targetList, context);
        handleClick();
    }

    @Override
    public void initLayout() {
        mLayoutID = R.layout.recy_illust_stagger;
    }

    @Override
    public void bindData(IllustsBean target, ViewHolder<RecyIllustStaggerBinding> bindView, int position) {

        float ratio = 1.0f * target.getHeight() / target.getWidth();
        if (ratio > MAX_HEIGHT_RATIO) {
            ratio = MAX_HEIGHT_RATIO;
            bindView.baseBind.illustImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else if (ratio < MIN_HEIGHT_RATIO) {
            ratio = MIN_HEIGHT_RATIO;
            bindView.baseBind.illustImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            bindView.baseBind.illustImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }
        bindView.baseBind.illustImage.setHeightRatio(ratio);

        if (target.isIs_bookmarked()) {
            bindView.baseBind.likeButton.setImageTintList(
                    ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.has_bookmarked)));
        } else {
            bindView.baseBind.likeButton.setImageTintList(
                    ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.not_bookmarked)));
        }
        bindView.baseBind.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (target.isIs_bookmarked()) {
                    bindView.baseBind.likeButton.setImageTintList(
                            ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.not_bookmarked)));
                } else {
                    bindView.baseBind.likeButton.setImageTintList(
                            ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.has_bookmarked)));
                }
                if (Lisa.sSettings.isPrivateStar()) {
                    PixivOperate.postLike(target, Params.TYPE_PRIVATE, showRelated, (position + 2));
                } else {
                    PixivOperate.postLike(target, Params.TYPE_PUBLUC, showRelated, (position + 2));
                }
            }
        });


        Glide.with(mContext)
                .load(GlideUtil.getMediumImg(target))
                .placeholder(R.color.second_light_bg)
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(getBuilder(target))
                .into(bindView.baseBind.illustImage);

        if (target.getPage_count() == 1) {
            bindView.baseBind.pSize.setVisibility(View.GONE);
        } else {
            bindView.baseBind.pSize.setVisibility(View.VISIBLE);
            bindView.baseBind.pSize.setText(String.format("%dP", target.getPage_count()));
        }

        bindView.itemView.setOnClickListener(view -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(view, position, 0);
            }
        });

        if (target.isRelated()) {
            bindView.baseBind.pRelated.setVisibility(View.VISIBLE);
        } else {
            bindView.baseBind.pRelated.setVisibility(View.GONE);
        }
    }

    public RequestBuilder<Drawable> getBuilder(IllustsBean illust) {
        return Glide.with(mContext)
                .load(GlideUtil.getMediumImg(illust))
                .placeholder(R.color.second_light_bg)
                .transition(DrawableTransitionOptions.withCrossFade());
    }

   /* @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public List<IllustsBean> getIllustList() {
        return allIllust;
    }*/

    private void handleClick() {
        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position, int viewType) {
                TimeRecord.start();

                final PageData pageData = new PageData(uuid, nextUrl, allIllust);
                Container.get().addPageToMap(pageData);

                Intent intent = new Intent(mContext, VActivity.class);
                intent.putExtra(Params.POSITION, position);
                intent.putExtra(Params.PAGE_UUID, pageData.getUUID());
                mContext.startActivity(intent);
            }
        });
    }
    private boolean showRelated = false;

    public boolean isShowRelated() {
        return showRelated;
    }

    public IAdapter setShowRelated(boolean showRelated) {
        this.showRelated = showRelated;
        return this;
    }
}
