package sckdn.lisa.fragments;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.Toolbar;

import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import java.util.List;

import sckdn.lisa.R;
import sckdn.lisa.activities.Lisa;
import sckdn.lisa.activities.UserActivity;
import sckdn.lisa.activities.VActivity;
import sckdn.lisa.adapters.BaseAdapter;
import sckdn.lisa.adapters.HistoryAdapter;
import sckdn.lisa.repo.BaseRepo;
import sckdn.lisa.core.Container;
import sckdn.lisa.repo.LocalRepo;
import sckdn.lisa.core.PageData;
import sckdn.lisa.database.AppDatabase;
import sckdn.lisa.database.IllustHistoryEntity;
import sckdn.lisa.databinding.FragmentBaseListBinding;
import sckdn.lisa.databinding.RecyViewHistoryBinding;
import sckdn.lisa.interfaces.OnItemClickListener;
import sckdn.lisa.model.IllustsBean;
import sckdn.lisa.utils.Common;
import sckdn.lisa.utils.Params;
import sckdn.lisa.viewmodel.BaseModel;
import sckdn.lisa.viewmodel.HistoryModel;


public class FragmentHistory extends LocalListFragment<FragmentBaseListBinding,
        IllustHistoryEntity> {

    @Override
    public BaseAdapter<IllustHistoryEntity, RecyViewHistoryBinding> adapter() {
        return new HistoryAdapter(allItems, mContext).setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position, int viewType) {
                Common.showLog(className + position + " " + allItems.size());
                if (viewType == 0) {
                    List<IllustsBean> allImages = ((HistoryModel) mModel).getAll();
                    if (!Common.isEmpty(allImages)) {
                        final PageData pageData = new PageData(allImages);
                        Container.get().addPageToMap(pageData);

                        IllustHistoryEntity historyEntity = allItems.get(position);
                        int index = 0;
                        for (int i = 0; i < allImages.size(); i++) {
                            if (allImages.get(i).getId() == historyEntity.getIllustID()) {
                                index = i;
                                break;
                            }
                        }

                        Intent intent = new Intent(mContext, VActivity.class);
                        intent.putExtra(Params.POSITION, index);
                        intent.putExtra(Params.PAGE_UUID, pageData.getUUID());
                        mContext.startActivity(intent);
                    }
                } else if (viewType == 1) {
                    Intent intent = new Intent(mContext, UserActivity.class);
                    intent.putExtra(Params.USER_ID, (int) v.getTag());
                    mContext.startActivity(intent);
                } else if (viewType == 2) {
                    new QMUIDialog.MessageDialogBuilder(mActivity)
                            .setTitle(getString(R.string.string_143))
                            .setMessage(getString(R.string.string_352))
                            .setSkinManager(QMUISkinManager.defaultInstance(mActivity))
                            .addAction(getString(R.string.string_142), new QMUIDialogAction.ActionListener() {
                                @Override
                                public void onClick(QMUIDialog dialog, int index) {
                                    dialog.dismiss();
                                }
                            })
                            .addAction(0, getString(R.string.string_141), QMUIDialogAction.ACTION_PROP_NEGATIVE,
                                    new QMUIDialogAction.ActionListener() {
                                        @Override
                                        public void onClick(QMUIDialog dialog, int index) {
                                            AppDatabase.getAppDatabase(mContext).downloadDao().delete(allItems.get(position));
                                            allItems.remove(position);
                                            mAdapter.notifyItemRemoved(position);
                                            mAdapter.notifyItemRangeChanged(position, allItems.size() - position);
                                            if (allItems.size() == 0) {
                                                mRecyclerView.setVisibility(View.INVISIBLE);
                                                emptyRela.setVisibility(View.VISIBLE);
                                            }
                                            Common.showToast(getString(R.string.string_220));
                                            dialog.dismiss();
                                        }
                                    })
                            .show();
                }
            }
        });
    }

    @Override
    public BaseRepo repository() {
        return new LocalRepo<List<IllustHistoryEntity>>() {
            @Override
            public List<IllustHistoryEntity> first() {
                return AppDatabase.getAppDatabase(mContext)
                        .downloadDao().getAllViewHistory(PAGE_SIZE, 0);
            }

            @Override
            public List<IllustHistoryEntity> next() {
                return AppDatabase.getAppDatabase(mContext)
                        .downloadDao().getAllViewHistory(PAGE_SIZE, allItems.size());
            }

            @Override
            public boolean hasNext() {
                return true;
            }
        };
    }

    @Override
    public void onFirstLoaded(List<IllustHistoryEntity> illustHistoryEntities) {
        ((HistoryModel)mModel).getAll().clear();
        for (int i = 0; i < illustHistoryEntities.size(); i++) {
            if (illustHistoryEntities.get(i).getType() == 0) {
                IllustsBean illustsBean = Lisa.sGson.fromJson(
                        illustHistoryEntities.get(i).getIllustJson(), IllustsBean.class);
                ((HistoryModel)mModel).getAll().add(illustsBean);
            }
        }
    }

    @Override
    public void onNextLoaded(List<IllustHistoryEntity> illustHistoryEntities) {
        for (int i = 0; i < illustHistoryEntities.size(); i++) {
            if (illustHistoryEntities.get(i).getType() == 0) {
                IllustsBean illustsBean = Lisa.sGson.fromJson(
                        illustHistoryEntities.get(i).getIllustJson(), IllustsBean.class);
                ((HistoryModel)mModel).getAll().add(illustsBean);
            }
        }
    }

    @Override
    public void initToolbar(Toolbar toolbar) {
        super.initToolbar(toolbar);
        toolbar.inflateMenu(R.menu.delete_all);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_delete) {
                    if (Common.isEmpty(allItems)) {
                        Common.showToast(getString(R.string.string_254));
                    } else {
                        new QMUIDialog.MessageDialogBuilder(mActivity)
                                .setTitle(getString(R.string.string_143))
                                .setMessage(getString(R.string.string_255))
                                .setSkinManager(QMUISkinManager.defaultInstance(mActivity))
                                .addAction(getString(R.string.string_142), new QMUIDialogAction.ActionListener() {
                                    @Override
                                    public void onClick(QMUIDialog dialog, int index) {
                                        dialog.dismiss();
                                    }
                                })
                                .addAction(0, getString(R.string.string_141), QMUIDialogAction.ACTION_PROP_NEGATIVE,
                                        new QMUIDialogAction.ActionListener() {
                                    @Override
                                    public void onClick(QMUIDialog dialog, int index) {
                                        AppDatabase.getAppDatabase(mContext).downloadDao().deleteAllHistory();
                                        Common.showToast(getString(R.string.string_220));
                                        dialog.dismiss();
                                        mAdapter.clear();
                                        emptyRela.setVisibility(View.VISIBLE);
                                    }
                                })
                                .show();
                    }
                }
                return true;
            }
        });
    }

    @Override
    public Class<? extends BaseModel<?>> modelClass() {
        return HistoryModel.class;
    }

    @Override
    public String getToolbarTitle() {
        return "浏览记录";
    }
}
