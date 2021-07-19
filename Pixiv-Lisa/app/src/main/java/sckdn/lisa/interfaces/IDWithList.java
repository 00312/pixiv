package sckdn.lisa.interfaces;

import java.util.List;

public interface IDWithList<T> {

    String getUUID();

    List<T> getList();
}
