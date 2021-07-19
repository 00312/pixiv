package sckdn.lisa.fragments;

import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.Toolbar;

import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import java.util.List;

import sckdn.lisa.R;
import sckdn.lisa.adapters.BaseAdapter;
import sckdn.lisa.adapters.MuteWorksAdapter;
import sckdn.lisa.repo.LocalRepo;
import sckdn.lisa.database.AppDatabase;
import sckdn.lisa.database.MuteEntity;
import sckdn.lisa.databinding.FragmentBaseListBinding;
import sckdn.lisa.databinding.RecyViewHistoryBinding;
import sckdn.lisa.helper.IllustFilter;
import sckdn.lisa.interfaces.OnItemClickListener;
import sckdn.lisa.utils.Common;

public class FragmentMutedObjects extends LocalListFragment<FragmentBaseListBinding, MuteEntity>
        implements Toolbar.OnMenuItemClickListener {

    @Override
    public LocalRepo<List<MuteEntity>> repository() {
        return new LocalRepo<List<MuteEntity>>() {
            @Override
            public List<MuteEntity> first() {
                return IllustFilter.getMutedWorks();
            }

            @Override
            public List<MuteEntity> next() {
                return null;
            }
        };
    }

    @Override
    public BaseAdapter<MuteEntity, RecyViewHistoryBinding> adapter() {
        return new MuteWorksAdapter(allItems, mContext).setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position, int viewType) {
                if (viewType == 2) {
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
                                            AppDatabase.getAppDatabase(mContext).downloadDao().deleteMuteEntity(allItems.get(position));
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
    public boolean showToolbar() {
        return false;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }
}
