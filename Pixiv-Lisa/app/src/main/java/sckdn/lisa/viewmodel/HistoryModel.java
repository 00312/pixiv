package sckdn.lisa.viewmodel;

import java.util.ArrayList;
import java.util.List;

import sckdn.lisa.database.IllustHistoryEntity;
import sckdn.lisa.model.IllustsBean;

public class HistoryModel extends BaseModel<IllustHistoryEntity> {

    private List<IllustsBean> all = new ArrayList<>();

    public List<IllustsBean> getAll() {
        return all;
    }

    public void setAll(List<IllustsBean> all) {
        this.all = all;
    }
}
