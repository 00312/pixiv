package sckdn.lisa.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import sckdn.lisa.R;
import sckdn.lisa.activities.Lisa;
import sckdn.lisa.database.IllustHistoryEntity;
import sckdn.lisa.databinding.RecyViewHistoryBinding;
import sckdn.lisa.model.IllustsBean;
import sckdn.lisa.utils.DensityUtil;
import sckdn.lisa.utils.GlideUtil;

//浏览历史
public class HistoryAdapter extends BaseAdapter<IllustHistoryEntity, RecyViewHistoryBinding> {

    private int illustImageSize = 0, novelImageSize = 0;
    private SimpleDateFormat mTime = new SimpleDateFormat(
            mContext.getResources().getString(R.string.string_350),
            Locale.getDefault());

    public HistoryAdapter(List<IllustHistoryEntity> targetList, Context context) {
        super(targetList, context);
        illustImageSize = (mContext.getResources().getDisplayMetrics().widthPixels -
                mContext.getResources().getDimensionPixelSize(R.dimen.four_dp)) / 2;
        novelImageSize = DensityUtil.dp2px(110.0f);
    }

    @Override
    public void initLayout() {
        mLayoutID = R.layout.recy_view_history;
    }

    @Override
    public void bindData(IllustHistoryEntity target, ViewHolder<RecyViewHistoryBinding> bindView, int position) {
        if (target.getType() == 0) {
            ViewGroup.LayoutParams params = bindView.baseBind.illustImage.getLayoutParams();
            params.height = illustImageSize;
            params.width = illustImageSize;
            bindView.baseBind.illustImage.setLayoutParams(params);

            IllustsBean current = Lisa.sGson.fromJson(allIllust.get(position).getIllustJson(), IllustsBean.class);
            Glide.with(mContext)
                    .load(GlideUtil.getMediumImg(current))
                    .placeholder(R.color.light_bg)
                    .into(bindView.baseBind.illustImage);
            bindView.baseBind.title.setText(current.getTitle());
            bindView.baseBind.author.setText(String.format("by: %s", current.getUser().getName()));

            if (current.isGif()) {
                bindView.baseBind.pSize.setVisibility(View.VISIBLE);
                bindView.baseBind.pSize.setText("GIF");
            } else {
                if (current.getPage_count() == 1) {
                    bindView.baseBind.pSize.setVisibility(View.GONE);
                } else {
                    bindView.baseBind.pSize.setVisibility(View.VISIBLE);
                    bindView.baseBind.pSize.setText(String.format("%dP", current.getPage_count()));
                }
            }

            if (mOnItemClickListener != null) {
                bindView.itemView.setOnClickListener(v ->
                        mOnItemClickListener.onItemClick(v, position, 0));
                bindView.baseBind.author.setOnClickListener(v -> {
                    bindView.baseBind.author.setTag(current.getUser().getId());
                    mOnItemClickListener.onItemClick(bindView.baseBind.author, position, 1);
                });
            }
        }

        bindView.baseBind.time.setText(mTime.format(allIllust.get(position).getTime()));
        bindView.baseBind.deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(v, position, 2);
            }
        });
        //从-400 丝滑滑动到0
        ((SpringHolder) bindView).spring.setCurrentValue(-400);
        ((SpringHolder) bindView).spring.setEndValue(0);
    }

    @Override
    public ViewHolder<RecyViewHistoryBinding> getNormalItem(ViewGroup parent) {
        return new SpringHolder(
                DataBindingUtil.inflate(
                        LayoutInflater.from(mContext),
                        mLayoutID,
                        parent,
                        false
                )
        );
    }
}
