package sckdn.lisa.model;

import java.util.List;

import sckdn.lisa.interfaces.ListShow;

public class ListMangaSeries implements ListShow<MangaSeriesItem> {

    private String next_url;
    private List<MangaSeriesItem> illust_series_details;

    @Override
    public List<MangaSeriesItem> getList() {
        return illust_series_details;
    }

    @Override
    public String getNextUrl() {
        return next_url;
    }
}
