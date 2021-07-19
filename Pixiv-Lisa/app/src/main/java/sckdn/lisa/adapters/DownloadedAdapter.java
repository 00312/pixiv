package sckdn.lisa.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import sckdn.lisa.R;
import sckdn.lisa.activities.Lisa;
import sckdn.lisa.database.DownloadEntity;
import sckdn.lisa.databinding.RecyDownloadedBinding;
import sckdn.lisa.model.IllustsBean;
import sckdn.lisa.utils.DensityUtil;

//已下载
public class DownloadedAdapter extends BaseAdapter<DownloadEntity, RecyDownloadedBinding> {

    private int imageSize;
    private SimpleDateFormat mTime = new SimpleDateFormat(
            mContext.getResources().getString(R.string.string_350),
            Locale.getDefault());

    public DownloadedAdapter(List<DownloadEntity> targetList, Context context) {
        super(targetList, context);
        imageSize = DensityUtil.dp2px(140.0f);
    }

    @Override
    public void initLayout() {
        mLayoutID = R.layout.recy_downloaded;
    }

    @Override
    public void bindData(DownloadEntity target,
                         ViewHolder<RecyDownloadedBinding> bindView, int position) {
            ViewGroup.LayoutParams params = bindView.baseBind.illustImage.getLayoutParams();
            params.height = imageSize;
            params.width = imageSize;
            bindView.baseBind.illustImage.setLayoutParams(params);

            IllustsBean currentIllust = Lisa.sGson.fromJson(allIllust.get(position).getIllustGson(), IllustsBean.class);
            if (!TextUtils.isEmpty(allIllust.get(position).getFileName()) &&
                    allIllust.get(position).getFileName().contains(".zip")) {
                bindView.baseBind.illustImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                Glide.with(mContext)
                        .load(R.mipmap.zip)
                        .placeholder(R.color.light_bg)
                        .into(bindView.baseBind.illustImage);
            } else {
                    bindView.baseBind.illustImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    Glide.with(mContext)
                            .load(allIllust.get(position).getFilePath())
                            .placeholder(R.color.light_bg)
                            .into(bindView.baseBind.illustImage);
            }
            bindView.baseBind.title.setText(allIllust.get(position).getFileName());
            bindView.baseBind.author.setText(String.format("by: %s", currentIllust.getUser().getName()));
            bindView.baseBind.time.setText(mTime.format(allIllust.get(position).getDownloadTime()));

            if (currentIllust.getPage_count() == 1) {
                bindView.baseBind.pSize.setVisibility(View.GONE);
            } else {
                bindView.baseBind.pSize.setVisibility(View.VISIBLE);
                bindView.baseBind.pSize.setText(String.format("%dP", currentIllust.getPage_count()));
            }

            if (mOnItemClickListener != null) {
                bindView.itemView.setOnClickListener(v ->
                        mOnItemClickListener.onItemClick(v, position, 0));
                bindView.baseBind.author.setOnClickListener(v -> {
                    bindView.baseBind.author.setTag(currentIllust.getUser().getId());
                    mOnItemClickListener.onItemClick(bindView.baseBind.author, position, 1);
                });
            }

        //从-400 丝滑滑动到0
        ((DownloadedHolder) bindView).spring.setCurrentValue(-400);
        ((DownloadedHolder) bindView).spring.setEndValue(0);
    }

    @Override
    public ViewHolder<RecyDownloadedBinding> getNormalItem(ViewGroup parent) {
        return new DownloadedHolder(
                DataBindingUtil.inflate(
                        LayoutInflater.from(mContext), mLayoutID, parent, false
                )
        );
    }
}
