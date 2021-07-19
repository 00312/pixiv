package sckdn.lisa.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.ToxicBakery.viewpager.transforms.DrawerTransformer;

import sckdn.lisa.R;
import sckdn.lisa.activities.Lisa;
import sckdn.lisa.activities.TemplateActivity;
import sckdn.lisa.databinding.ViewpagerWithTablayoutBinding;
import sckdn.lisa.utils.Params;

import static sckdn.lisa.activities.Lisa.sUserModel;

public class FragmentCollection extends BaseFragment<ViewpagerWithTablayoutBinding> {

    private Fragment[] allPages;
    private String[] CHINESE_TITLES;

    private int type; //0插画收藏,2关注

    public static FragmentCollection newInstance(int type) {
        Bundle args = new Bundle();
        args.putInt(Params.DATA_TYPE, type);
        FragmentCollection fragment = new FragmentCollection();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initBundle(Bundle bundle) {
        type = bundle.getInt(Params.DATA_TYPE);
    }

    @Override
    public void initLayout() {
        mLayoutID = R.layout.viewpager_with_tablayout;
    }

    @Override
    public void initView() {
        if (type == 0) {
            allPages = new Fragment[]{
                    FragmentLikeIllust.newInstance(sUserModel.getUser().getId(),
                            Params.TYPE_PUBLUC),
                    FragmentLikeIllust.newInstance(sUserModel.getUser().getId(),
                            Params.TYPE_PRIVATE)
            };
            CHINESE_TITLES = new String[]{
                    Lisa.getContext().getString(R.string.public_like_illust),
                    Lisa.getContext().getString(R.string.private_like_illust)
            };
        }  else if (type == 2) {
            allPages = new Fragment[]{
                    FragmentFollowUser.newInstance(sUserModel.getUser().getId(),
                            Params.TYPE_PUBLUC, false),
                    FragmentFollowUser.newInstance(sUserModel.getUser().getId(),
                            Params.TYPE_PRIVATE, false)
            };
            CHINESE_TITLES = new String[]{
                    Lisa.getContext().getString(R.string.public_like_user),
                    Lisa.getContext().getString(R.string.private_like_user)
            };
        }

        if (type == 0) {
            baseBind.toolbarTitle.setText(R.string.string_319);
        }  else if (type == 2) {
            baseBind.toolbarTitle.setText(R.string.string_321);
        }
        baseBind.toolbar.setNavigationOnClickListener(v -> mActivity.finish());
        baseBind.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (baseBind.viewPager.getCurrentItem() == 0) {
                    Intent intent = new Intent(mContext, TemplateActivity.class);
                    intent.putExtra(TemplateActivity.EXTRA_KEYWORD,
                            Params.TYPE_PUBLUC);
                    intent.putExtra(TemplateActivity.EXTRA_FRAGMENT, "按标签筛选");
                    startActivity(intent);
                    return true;
                } else if (baseBind.viewPager.getCurrentItem() == 1) {
                    Intent intent = new Intent(mContext, TemplateActivity.class);
                    intent.putExtra(TemplateActivity.EXTRA_KEYWORD,
                            Params.TYPE_PRIVATE);
                    intent.putExtra(TemplateActivity.EXTRA_FRAGMENT, "按标签筛选");
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
        baseBind.viewPager.setPageTransformer(true, new DrawerTransformer());
        baseBind.viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager(), 0) {
            @NonNull
            @Override
            public Fragment getItem(int i) {
                return allPages[i];
            }

            @Override
            public int getCount() {
                return CHINESE_TITLES.length;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return CHINESE_TITLES[position];
            }
        });
        baseBind.tabLayout.setupWithViewPager(baseBind.viewPager);
        baseBind.toolbar.inflateMenu(R.menu.illust_filter);
        baseBind.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                baseBind.toolbar.getMenu().clear();
                if (type == 0) {
                    baseBind.toolbar.inflateMenu(R.menu.illust_filter);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }
}
