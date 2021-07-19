package sckdn.lisa.model;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.List;

import sckdn.lisa.interfaces.ListShow;

public class ListTrendingtag implements ListShow<ListTrendingtag.TrendTagsBean>, Serializable {

    private List<TrendTagsBean> trend_tags;
    private List<TrendTagsBean> tags;

    public List<TrendTagsBean> getTags() {
        return tags;
    }

    public void setTags(List<TrendTagsBean> tags) {
        this.tags = tags;
    }

    public List<TrendTagsBean> getTrend_tags() {
        return this.trend_tags;
    }

    public void setTrend_tags(List<TrendTagsBean> paramList) {
        this.trend_tags = paramList;
    }

    @Override
    public List<TrendTagsBean> getList() {
        if (trend_tags != null && trend_tags.size() != 0) {
            return trend_tags;
        } else {
            return tags;
        }
    }

    @Override
    public String getNextUrl() {
        return null;
    }

    public static class TrendTagsBean implements Serializable {


        private String tag;
        private String name;
        private String translated_name;
        private IllustsBean illust;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTag() {
            if (!TextUtils.isEmpty(tag)) {
                return tag;
            }

            if (!TextUtils.isEmpty(name)) {
                return name;
            }

            return "";
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getTranslated_name() {
            return translated_name;
        }

        public void setTranslated_name(String translated_name) {
            this.translated_name = translated_name;
        }

        public IllustsBean getIllust() {
            return illust;
        }

        public void setIllust(IllustsBean illust) {
            this.illust = illust;
        }

    }
}
