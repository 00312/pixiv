package sckdn.lisa.fragments;

import androidx.databinding.ViewDataBinding;

import java.util.ArrayList;
import java.util.List;

import sckdn.lisa.activities.Lisa;
import sckdn.lisa.adapters.BaseAdapter;
import sckdn.lisa.adapters.ColorAdapter;
import sckdn.lisa.repo.BaseRepo;
import sckdn.lisa.repo.LocalRepo;
import sckdn.lisa.databinding.FragmentBaseListBinding;
import sckdn.lisa.model.ColorItem;

public class FragmentColors extends LocalListFragment<FragmentBaseListBinding, ColorItem> {

    @Override
    public BaseAdapter<?, ? extends ViewDataBinding> adapter() {
        return new ColorAdapter(allItems, mContext);
    }

    @Override
    public BaseRepo repository() {
        return new LocalRepo<List<ColorItem>>() {
            @Override
            public List<ColorItem> first() {
                return getList();
            }

            @Override
            public List<ColorItem> next() {
                return null;
            }
        };
    }

    public List<ColorItem> getList() {
        List<ColorItem> itemList = new ArrayList<>();
        int current = Lisa.sSettings.getThemeIndex();
        itemList.add(new ColorItem(1, COLOR_NAMES[0], "#56baec", current == 0));
        itemList.add(new ColorItem(0, COLOR_NAMES[1], "#686bdd", current == 1));
        itemList.add(new ColorItem(2, COLOR_NAMES[2], "#008BF3", current == 2));
        itemList.add(new ColorItem(3, COLOR_NAMES[3], "#03d0bf", current == 3));
        itemList.add(new ColorItem(4, COLOR_NAMES[4], "#fee65e", current == 4));
        itemList.add(new ColorItem(5, COLOR_NAMES[5], "#fe83a2", current == 5));
        itemList.add(new ColorItem(6, COLOR_NAMES[6], "#f44336", current == 6));
        itemList.add(new ColorItem(7, COLOR_NAMES[7], "#673AB7", current == 7));
        itemList.add(new ColorItem(8, COLOR_NAMES[8], "#4CAF50", current == 8));
        itemList.add(new ColorItem(9, COLOR_NAMES[9], "#E91E63", current == 9));
        return itemList;
    }

    public static String[] COLOR_NAMES = new String[]{
            "浅蓝",
            "紫",
            "深蓝",
            "绿",
            "黄",
            "粉",
            "红",
            "深紫",
            "绿",
            "浅红"
    };

    @Override
    public String getToolbarTitle() {
        return "主题颜色";
    }
}
