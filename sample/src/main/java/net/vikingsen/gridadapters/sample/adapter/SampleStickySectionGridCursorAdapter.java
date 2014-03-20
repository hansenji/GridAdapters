package net.vikingsen.gridadapters.sample.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.vikingsen.gridadapters.StickySectionHeaderGridCursorAdapter;
import net.vikingsen.gridadapters.sample.R;
import net.vikingsen.gridadapters.sample.domain.Data;
import net.vikingsen.gridadapters.sample.domain.distro.Distro;
import net.vikingsen.gridadapters.sample.view.HeaderViewHolder;
import net.vikingsen.gridadapters.sample.view.ViewHolder;

/**
 * Created by Jordan Hansen
 */
public class SampleStickySectionGridCursorAdapter extends StickySectionHeaderGridCursorAdapter {

    LayoutInflater inflater;

    public SampleStickySectionGridCursorAdapter(Context context, int numColumns) {
        super(context, numColumns);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View newHeaderView(Context context, Cursor cursor, ViewGroup parent) {
        View view = inflater.inflate(R.layout.grid_header, parent, false);
        view.setTag(new HeaderViewHolder(view));
        return view;
    }

    @Override
    public void bindHeaderView(View view, Context context, Cursor cursor) {
        HeaderViewHolder holder = (HeaderViewHolder) view.getTag();
        holder.getTextView().setText(cursor.getString(cursor.getColumnIndex(Distro.C_NAME)).substring(0, 1));
    }

    @Override
    public long getHeaderId(Cursor cursor) {
        String name = cursor.getString(cursor.getColumnIndex(Distro.C_NAME));
        return name != null ? name.charAt(0) : 0;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = inflater.inflate(R.layout.grid_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        Distro distro = new Distro(cursor);
        holder.getImageView().setImageResource(Data.getImageId(distro.getName()));
    }
}
