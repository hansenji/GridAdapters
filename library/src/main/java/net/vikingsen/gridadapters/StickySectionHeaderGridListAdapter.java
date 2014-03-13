package net.vikingsen.gridadapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by Jordan Hansen
 *
 */
public abstract class StickySectionHeaderGridListAdapter<T> extends SectionGridListAdapter<T> implements StickyListHeadersAdapter {

    public StickySectionHeaderGridListAdapter(Context context, int numColumns) {
        super(context, numColumns);
    }

    public abstract View newHeaderView(Context context, int position, T t, ViewGroup parent);

    public abstract void bindHeaderView(View view, Context context, int position, T t);

    public abstract long getHeaderId(int position, T t);

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        Context context = getContext();
        Section section = getSection(position);
        T t = getGridItem(section.firstDataPosition);
        if (view == null) {
            view = newHeaderView(context, section.firstDataPosition, t, parent);
        }
        bindHeaderView(view, context, section.firstDataPosition, t);
        return view;
    }

    @Override
    public long getHeaderId(int position) {
        Section section = getSection(position);
        T t = getGridItem(section.firstDataPosition);
        return getHeaderId(section.firstDataPosition, t);
    }
}
