package ru.objects;

import java.util.ArrayList;
import java.util.List;

public class Page<T> {

    private final int maxSize;
    private final List<T> list;

    public Page(int maxSize) {
        this.maxSize = maxSize;
        list = new ArrayList<>();
    }

    public void add(T object) {
        list.add(0, object);
        if (list.size() > maxSize) list.remove(list.size()-1);
    }

    public List<T> getList() {
        return list;
    }
}
