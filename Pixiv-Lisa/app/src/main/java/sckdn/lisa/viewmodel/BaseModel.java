package sckdn.lisa.viewmodel;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import sckdn.lisa.repo.BaseRepo;
import sckdn.lisa.utils.Common;


public class BaseModel<T> extends ViewModel{

    private List<T> content = null;
    private boolean isLoaded = false;
    private BaseRepo mBaseRepo;

    public BaseModel() {
        Common.showLog("trace 构造 000");
    }

    public List<T> getContent() {
        if (content == null) {
            content = new ArrayList<>();
        }
        return content;
    }

    public void load(List<T> list, boolean isFresh) {
        if (isFresh) {
            content.clear();
        }
        content.addAll(list);
        isLoaded = true;
    }

    public void load(List<T> list, int index) {
        content.addAll(index, list);
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public BaseRepo getBaseRepo() {
        return mBaseRepo;
    }

    public void setBaseRepo(BaseRepo baseRepo) {
        mBaseRepo = baseRepo;
    }
}
