package sckdn.lisa.fragments;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.FragmentTransaction;

import com.scwang.smartrefresh.layout.footer.FalsifyFooter;

import java.util.ArrayList;
import java.util.List;

import sckdn.lisa.R;
import sckdn.lisa.activities.MainActivity;
import sckdn.lisa.activities.Lisa;
import sckdn.lisa.activities.TemplateActivity;
import sckdn.lisa.adapters.BaseAdapter;
import sckdn.lisa.adapters.IAdapter;
import sckdn.lisa.repo.BaseRepo;
import sckdn.lisa.database.AppDatabase;
import sckdn.lisa.database.IllustRecmdEntity;
import sckdn.lisa.databinding.FragmentNewRightBinding;
import sckdn.lisa.http.NullCtrl;
import sckdn.lisa.model.ListIllust;
import sckdn.lisa.model.IllustsBean;
import sckdn.lisa.repo.RightRepo;
import sckdn.lisa.utils.Common;
import sckdn.lisa.utils.Params;
import sckdn.lisa.interfaces.OnCheckChangeListener;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class FragmentRight extends NetListFragment<FragmentNewRightBinding, ListIllust, IllustsBean> {

    @Override
    public void initLayout() {
        mLayoutID = R.layout.fragment_new_right;
    }

    @Override
    public BaseAdapter<?, ? extends ViewDataBinding> adapter() {
        return new IAdapter(allItems, mContext);
    }

    @Override
    public void initView() {
        super.initView();



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
        baseBind.seeMore.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, TemplateActivity.class);
            intent.putExtra(TemplateActivity.EXTRA_FRAGMENT, "推荐用户");
            startActivity(intent);
        });
        baseBind.glareLayout.setListener(new OnCheckChangeListener() {
            @Override
            public void onSelect(int index, View view) {
                Common.showLog("glareLayout onSelect " + index);
                if (index == 0) {
                    restrict = Params.TYPE_ALL;
                } else if (index == 1) {
                    restrict = Params.TYPE_PUBLUC;
                } else if (index == 2) {
                    restrict = Params.TYPE_PRIVATE;
                }
                ((RightRepo) mRemoteRepo).setRestrict(restrict);
                clearAndRefresh();
            }

            @Override
            public void onReselect(int index, View view) {
                Common.showLog("glareLayout onReselect " + index);
                clearAndRefresh();
            }
        });
    }

    @Override
    public BaseRepo repository() {
        return new RightRepo(restrict);
    }

    @Override
    public void initRecyclerView() {
        staggerRecyclerView();
    }

    @Override
    public void lazyData() {
        super.lazyData();

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

        FragmentRecmdUserHorizontal recmdUser = new FragmentRecmdUserHorizontal();
        transaction.add(R.id.user_recmd_fragment, recmdUser, "FragmentRecmdUserHorizontal");
        transaction.commitNowAllowingStateLoss();

        baseBind.refreshLayout.autoRefresh();
    }

    @Override
    public boolean autoRefresh() {
        return false;
    }

    private String restrict = Params.TYPE_PUBLUC;

    @Override
    public void showDataBase() {
        Observable.create((ObservableOnSubscribe<List<IllustRecmdEntity>>) emitter -> {
            List<IllustRecmdEntity> temp = AppDatabase.getAppDatabase(mContext).recmdDao().getAll();
            Thread.sleep(100);
            emitter.onNext(temp);
            emitter.onComplete();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(entities -> {
                    List<IllustsBean> temp = new ArrayList<>();
                    for (int i = 0; i < entities.size(); i++) {
                        IllustsBean illustsBean = Lisa.sGson.fromJson(
                                entities.get(i).getIllustJson(), IllustsBean.class);
                        temp.add(illustsBean);
                    }
                    return temp;
                })
                .subscribe(new NullCtrl<List<IllustsBean>>() {
                    @Override
                    public void success(List<IllustsBean> illustsBeans) {
                        allItems.addAll(illustsBeans);
                        mAdapter.notifyItemRangeInserted(mAdapter.headerSize(), allItems.size());
                    }

                    @Override
                    public void must(boolean isSuccess) {
                        baseBind.refreshLayout.finishRefresh(isSuccess);
                        baseBind.refreshLayout.setRefreshFooter(new FalsifyFooter(mContext));
                    }
                });
    }
}
