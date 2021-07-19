package sckdn.lisa.fragments;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentTransaction;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import sckdn.lisa.R;
import sckdn.lisa.activities.MainActivity;
import sckdn.lisa.activities.TemplateActivity;
import sckdn.lisa.databinding.FragmentNewCenterBinding;

public class FragmentCenter extends SwipeFragment<FragmentNewCenterBinding> {

    @Override
    public void initLayout() {
        mLayoutID = R.layout.fragment_new_center;
    }

    @Override
    protected void initView() {

        baseBind.toolbar.inflateMenu(R.menu.fragment_left);
        baseBind.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mActivity instanceof MainActivity) {
                    ((MainActivity) mActivity).getDrawer().openDrawer(GravityCompat.START, true);
                }
            }
        });
        baseBind.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_search) {
                    Intent intent = new Intent(mContext, TemplateActivity.class);
                    intent.putExtra(TemplateActivity.EXTRA_FRAGMENT, "搜索");
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });

        baseBind.manga.setClipToOutline(true);
        baseBind.walkThrough.setClipToOutline(true);

        baseBind.manga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TemplateActivity.class);
                intent.putExtra(TemplateActivity.EXTRA_FRAGMENT, "推荐漫画");
                startActivity(intent);
            }
        });

        baseBind.walkThrough.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TemplateActivity.class);
                intent.putExtra(TemplateActivity.EXTRA_FRAGMENT, "画廊");
                startActivity(intent);
            }
        });
    }

    @Override
    public void lazyData() {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.commitNowAllowingStateLoss();
    }

    @Override
    public SmartRefreshLayout getSmartRefreshLayout() {
        return baseBind.refreshLayout;
    }



    }
