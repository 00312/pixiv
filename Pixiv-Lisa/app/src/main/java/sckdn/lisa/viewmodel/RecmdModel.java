package sckdn.lisa.viewmodel;

import java.util.ArrayList;
import java.util.List;

import sckdn.lisa.model.IllustsBean;

public class RecmdModel extends BaseModel<IllustsBean> {

    private List<IllustsBean> rankList = new ArrayList<>();

    public List<IllustsBean> getRankList() {
        return rankList;
    }
}
