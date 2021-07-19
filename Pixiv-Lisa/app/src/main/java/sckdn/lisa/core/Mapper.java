package sckdn.lisa.core;

import java.util.ArrayList;
import java.util.List;

import sckdn.lisa.helper.IllustFilter;
import sckdn.lisa.interfaces.ListShow;
import sckdn.lisa.model.IllustsBean;
import io.reactivex.functions.Function;

/**
 * 默认Mapper，从列表中隐藏掉包含“已屏蔽tag”的作品
 * @param <T>
 */
public class Mapper<T extends ListShow<?>> implements Function<T, T> {

    @Override
    public T apply(T t) {
        List<IllustsBean> dash = new ArrayList<>();
        for (Object o : t.getList()) {
            if (o instanceof IllustsBean) {
                boolean isTagBanned = IllustFilter.judgeTag((IllustsBean) o);
                boolean isIdBanned = IllustFilter.judgeID((IllustsBean) o);
                if (isTagBanned || isIdBanned) {
                    dash.add((IllustsBean) o);
                }
            }
        }

        if (t.getList() != null && dash.size() != 0) {
            t.getList().removeAll(dash);
        }
        return t;
    }
}
