package sckdn.lisa.model;

import java.util.List;

public class RecmdIllust extends ListIllust {

    private List<IllustsBean> ranking_illusts;

    public List<IllustsBean> getRanking_illusts() {
        return ranking_illusts;
    }

    public void setRanking_illusts(List<IllustsBean> ranking_illusts) {
        this.ranking_illusts = ranking_illusts;
    }
}
