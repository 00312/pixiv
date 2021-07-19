package sckdn.lisa.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.List;

import sckdn.lisa.R;
import sckdn.lisa.activities.Lisa;
import sckdn.lisa.databinding.RecyColorBinding;
import sckdn.lisa.interfaces.OnItemClickListener;
import sckdn.lisa.model.ColorItem;
import sckdn.lisa.utils.Common;
import sckdn.lisa.utils.Local;

public class ColorAdapter extends BaseAdapter<ColorItem, RecyColorBinding> {

    public ColorAdapter(@Nullable List<ColorItem> targetList, Context context) {
        super(targetList, context);
        handleClick();
    }

    @Override
    public void initLayout() {
        mLayoutID = R.layout.recy_color;
    }

    @Override
    public void bindData(ColorItem target, ViewHolder<RecyColorBinding> bindView, int position) {
        bindView.baseBind.card.setCardBackgroundColor(Color.parseColor(target.getColor()));
        if (target.isSelect()) {
            bindView.baseBind.name.setText(String.format("%s（正在使用）", target.getName()));
        } else {
            bindView.baseBind.name.setText(target.getName());
        }
        bindView.baseBind.value.setText(target.getColor());
        bindView.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(v, position, 0);
            }
        });
    }

    private void handleClick() {
        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position, int viewType) {
                if (position == Lisa.sSettings.getThemeIndex()) {
                    return;
                }

                Lisa.sSettings.setThemeIndex(position);
                Local.setSettings(Lisa.sSettings);
                Common.restart();
                Common.showToast("设置成功", 2);
            }
        });
    }
}
