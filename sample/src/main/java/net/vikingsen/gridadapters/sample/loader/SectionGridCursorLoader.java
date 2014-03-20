package net.vikingsen.gridadapters.sample.loader;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;

import net.vikingsen.gridadapters.SectionGridCursorAdapter;
import net.vikingsen.gridadapters.sample.domain.DatabaseManager;
import net.vikingsen.gridadapters.sample.domain.SectionGridCursor;
import net.vikingsen.gridadapters.sample.domain.distro.Distro;

/**
 * Created by Jordan Hansen
 */
public class SectionGridCursorLoader extends AsyncTaskLoader<SectionGridCursor> {

    private final DatabaseManager databaseManager;

    private static final String CURSOR_QUERY = "SELECT * FROM " + Distro.TABLE;

    private static final String SECTION_QUERY = "SELECT COUNT(" + Distro.C_NAME + ") AS " + Distro.C_COUNT +
            " FROM " + Distro.TABLE +
            " GROUP BY SUBSTR(" + Distro.C_NAME + ", 1, 1)" +
            " ORDER BY " + Distro.C_NAME;

    public SectionGridCursorLoader(Context context) {
        super(context);
        databaseManager = new DatabaseManager();
        databaseManager.setContext(context);
    }

    @Override
    public SectionGridCursor loadInBackground() {
        return new SectionGridCursor(findCursorByRawQuery(), findSectionsByRawQuery());
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
        super.onStartLoading();
    }

    private Cursor findCursorByRawQuery() {
        return databaseManager.getWritableDatabase(DatabaseManager.MAIN_DATABASE_NAME).rawQuery(CURSOR_QUERY, null);
    }


    private SectionGridCursorAdapter.Section[] findSectionsByRawQuery() {
        Cursor c = databaseManager.getWritableDatabase(DatabaseManager.MAIN_DATABASE_NAME).rawQuery(SECTION_QUERY, null);
        SectionGridCursorAdapter.Section[] sections;
        if (c != null) {
            sections = new SectionGridCursorAdapter.Section[c.getCount()];
            int index = c.getColumnIndex(Distro.C_COUNT);
            if (c.moveToFirst()) {
                int i = 0;
                do {
                    sections[i++] = new SectionGridCursorAdapter.Section(c.getInt(index));
                } while (c.moveToNext());
            }
            c.close();
        } else {
            sections = new SectionGridCursorAdapter.Section[0];
        }
        return sections;
    }
}
