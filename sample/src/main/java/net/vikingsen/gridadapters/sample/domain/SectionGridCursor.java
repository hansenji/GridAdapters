package net.vikingsen.gridadapters.sample.domain;

import android.database.Cursor;

import net.vikingsen.gridadapters.SectionGridCursorAdapter;

/**
 * Created by Jordan Hansen
 */
public class SectionGridCursor {

    private final Cursor cursor;
    private final SectionGridCursorAdapter.Section[] sections;

    public SectionGridCursor(Cursor cursor, SectionGridCursorAdapter.Section[] sections) {
        this.cursor = cursor;
        this.sections = sections;
    }

    public Cursor getCursor() {
        return cursor;
    }

    public SectionGridCursorAdapter.Section[] getSections() {
        return sections;
    }
}
