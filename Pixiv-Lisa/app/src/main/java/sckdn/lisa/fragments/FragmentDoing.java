package sckdn.lisa.fragments;

import androidx.databinding.ViewDataBinding;

import java.util.List;

import sckdn.lisa.adapters.BaseAdapter;
import sckdn.lisa.adapters.DoingAdapter;
import sckdn.lisa.repo.BaseRepo;
import sckdn.lisa.repo.LocalRepo;
import sckdn.lisa.databinding.FragmentBaseListBinding;
import sckdn.lisa.worker.AbstractTask;
import sckdn.lisa.worker.Worker;
import sckdn.lisa.interfaces.FeedBack;

public class FragmentDoing extends LocalListFragment<FragmentBaseListBinding, AbstractTask>{

    @Override
    public BaseAdapter<?, ? extends ViewDataBinding> adapter() {
        return new DoingAdapter(allItems, mContext);
    }

    @Override
    protected void initData() {
        super.initData();
        Worker.get().setFeedBack(new FeedBack() {
            @Override
            public void doSomething() {
                allItems.remove(0);
                mAdapter.notifyItemRemoved(0);
                mAdapter.notifyItemRangeChanged(0, allItems.size());
            }
        });
    }

    @Override
    public void onDestroyView() {
        Worker.get().setFeedBack(null);
        super.onDestroyView();
    }

    @Override
    public BaseRepo repository() {
        return new LocalRepo<List<AbstractTask>>() {
            @Override
            public List<AbstractTask> first() {
                return Worker.get().getRunningTask();
            }

            @Override
            public List<AbstractTask> next() {
                return null;
            }
        };
    }

    @Override
    public String getToolbarTitle() {
        return "任务中心";
    }
}
