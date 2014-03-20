package net.vikingsen.gridadapters.sample.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.vikingsen.gridadapters.SectionGridCursorAdapter;
import net.vikingsen.gridadapters.sample.R;
import net.vikingsen.gridadapters.sample.domain.Data;
import net.vikingsen.gridadapters.sample.domain.distro.Distro;
import net.vikingsen.gridadapters.sample.view.ViewHolder;

/**
 * Created by Jordan Hansen
 */
public class SampleSectionGridCursorAdapter extends SectionGridCursorAdapter {

    private final LayoutInflater inflater;

    public SampleSectionGridCursorAdapter(Context context, int numColumns) {
        super(context, numColumns);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = inflater.inflate(R.layout.grid_item, parent, false);
        view.setTag(new ViewHolder(view));
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        Distro distro = new Distro(cursor);
        holder.getImageView().setImageResource(Data.getImageId(distro.getName()));
    }
}
