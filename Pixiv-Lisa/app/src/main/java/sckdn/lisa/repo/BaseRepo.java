package sckdn.lisa.repo;

import android.content.Context;

import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;

import sckdn.lisa.activities.Lisa;
import sckdn.lisa.interfaces.DataView;
import sckdn.lisa.utils.Common;

public class BaseRepo implements DataView {

    public BaseRepo() {
        Common.showLog("BaseRepo " + getClass().getSimpleName() + " newInstance");
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public boolean enableRefresh() {
        return true;
    }

    @Override
    public RefreshHeader getHeader(Context context) {
        return new MaterialHeader(context);
    }

    @Override
    public RefreshFooter getFooter(Context context) {
        return new ClassicsFooter(context);
    }

    @Override
    public boolean showNoDataHint() {
        return true;
    }

    @Override
    public String token() {
        try {
            if (Lisa.sUserModel != null) {
                return Lisa.sUserModel.getAccess_token();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public int currentUserID() {
        try {
            if (Lisa.sUserModel != null) {
                return Lisa.sUserModel.getUser().getId();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public boolean localData() {
        return false;
    }
}
