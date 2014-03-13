package net.vikingsen.gridadapters;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by Jordan Hansen
 *
 */
public abstract class StickySectionHeaderGridCursorAdapter extends SectionGridCursorAdapter implements StickyListHeadersAdapter {

    public StickySectionHeaderGridCursorAdapter(Context context, int numColumns) {
        super(context, numColumns);
    }

    public abstract View newHeaderView(Context context, Cursor cursor, ViewGroup parent);

    /**
     * @param view    View to bind.
     * @param context view context.
     * @param cursor  cursor of data.  Will be set to first item of section.
     */
    public abstract void bindHeaderView(View view, Context context, Cursor cursor);

    public abstract long getHeaderId(Cursor cursor);

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        Context context = getContext();
        Section section = getSection(position);
        Cursor cursor = getCursor();
        if (cursor != null && cursor.moveToPosition(section.firstCursorPosition)) {
            if (view == null) {
                view = newHeaderView(context, cursor, parent);
            }
            bindHeaderView(view, context, cursor);
        }
        return view;
    }

    @Override
    public long getHeaderId(int position) {
        Cursor cursor = getCursor();
        Section section = getSection(position);
        if (cursor != null && cursor.moveToPosition(section.firstCursorPosition)) {
            return getHeaderId(cursor);
        }
        return 0;
    }
}
