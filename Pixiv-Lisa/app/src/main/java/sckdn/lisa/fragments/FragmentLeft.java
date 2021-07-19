package sckdn.lisa.fragments;

import android.content.Intent;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import sckdn.lisa.R;
import sckdn.lisa.activities.MainActivity;
import sckdn.lisa.activities.Lisa;
import sckdn.lisa.activities.TemplateActivity;
import sckdn.lisa.databinding.FragmentLeftBinding;
import sckdn.lisa.utils.Params;

public class FragmentLeft extends BaseLazyFragment<FragmentLeftBinding> {

    private NetListFragment[] mFragments = null;

    @Override
    public void initLayout() {
        mLayoutID = R.layout.fragment_left;
    }

    @Override
    public void initView() {
        baseBind.toolbar.setNavigationOnClickListener(v -> {
            if (mActivity instanceof MainActivity) {
                ((MainActivity) mActivity).getDrawer().openDrawer(GravityCompat.START, true);
            }
        });
        baseBind.toolbarTitle.setText(R.string.string_207);
        baseBind.toolbar.inflateMenu(R.menu.fragment_left);
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
    }

    @Override
    public void lazyData() {
        String[] TITLES = new String[]{
                Lisa.getContext().getString(R.string.recommend_illust),
                Lisa.getContext().getString(R.string.hot_tag)
        };
        mFragments = new NetListFragment[]{
                FragmentRecmdIllust.newInstance("插画"),
                FragmentHotTag.newInstance(Params.TYPE_ILLUST)
        };
        baseBind.viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager(), 0) {
            @NonNull
            @Override
            public Fragment getItem(int i) {
                return mFragments[i];
            }

            @Override
            public int getCount() {
                return TITLES.length;
            }

            @NonNull
            @Override
            public CharSequence getPageTitle(int position) {
                return TITLES[position];
            }
        });
        baseBind.tabLayout.setupWithViewPager(baseBind.viewPager);
    }


}
