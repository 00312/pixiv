package sckdn.lisa.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;

import sckdn.lisa.activities.RankActivity;
import sckdn.lisa.activities.VActivity;
import sckdn.lisa.core.PageData;
import sckdn.lisa.databinding.RecyRecmdHeaderBinding;
import sckdn.lisa.model.IllustsBean;
import sckdn.lisa.core.Container;
import sckdn.lisa.utils.DensityUtil;
import sckdn.lisa.utils.Params;
import sckdn.lisa.view.LinearItemHorizontalDecoration;

public class IllustHeader extends ViewHolder<RecyRecmdHeaderBinding> {

    private String type = "";

    public IllustHeader(RecyRecmdHeaderBinding bindView, String type) {
        super(bindView);
        this.type = type;
    }

    public void show(Context context, List<IllustsBean> illustsBeans) {
        baseBind.topRela.setVisibility(View.VISIBLE);
        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(800L);
        baseBind.topRela.startAnimation(animation);
        RAdapter adapter = new RAdapter(illustsBeans, context);
        adapter.setOnItemClickListener((v, position, viewType) -> {
            final PageData pageData = new PageData(illustsBeans);
            Container.get().addPageToMap(pageData);

            Intent intent = new Intent(context, VActivity.class);
            intent.putExtra(Params.POSITION, position);
            intent.putExtra(Params.PAGE_UUID, pageData.getUUID());
            context.startActivity(intent);
        });
        baseBind.ranking.setAdapter(adapter);
    }

    public void initView(Context context) {
        baseBind.topRela.setVisibility(View.GONE);
        baseBind.seeMore.setOnClickListener(v -> {
            Intent intent = new Intent(context, RankActivity.class);
            intent.putExtra("dataType", type);
            context.startActivity(intent);
        });
        baseBind.ranking.addItemDecoration(new LinearItemHorizontalDecoration(DensityUtil.dp2px(8.0f)));
        LinearLayoutManager manager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        baseBind.ranking.setLayoutManager(manager);
        baseBind.ranking.setHasFixedSize(true);
    }
}
