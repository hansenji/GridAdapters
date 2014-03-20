package net.vikingsen.gridadapters.sample.domain;

import net.vikingsen.gridadapters.SectionGridListAdapter;

import java.util.List;

/**
 * Created by Jordan Hansen
 */
public class SectionGridList<T> {

    private final List<T> list;
    private final SectionGridListAdapter.Section[] sections;

    public SectionGridList(List<T> list, SectionGridListAdapter.Section[] sections) {
        this.list = list;
        this.sections = sections;
    }

    public List<T> getList() {
        return list;
    }

    public SectionGridListAdapter.Section[] getSections() {
        return sections;
    }
}
