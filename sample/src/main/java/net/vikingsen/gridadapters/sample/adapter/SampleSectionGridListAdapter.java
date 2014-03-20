package net.vikingsen.gridadapters.sample.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.vikingsen.gridadapters.SectionGridListAdapter;
import net.vikingsen.gridadapters.sample.R;
import net.vikingsen.gridadapters.sample.domain.Data;
import net.vikingsen.gridadapters.sample.domain.distro.Distro;
import net.vikingsen.gridadapters.sample.view.ViewHolder;

/**
 * Created by Jordan Hansen
 */
public class SampleSectionGridListAdapter extends SectionGridListAdapter<Distro> {

    private final LayoutInflater inflater;

    public SampleSectionGridListAdapter(Context context, int numColumns) {
        super(context, numColumns);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, int position, Distro distro, ViewGroup parent) {
        View view = inflater.inflate(R.layout.grid_item, parent, false);
        view.setTag(new ViewHolder(view));
        return view;
    }

    @Override
    public void bindView(View view, Context context, int position, Distro distro) {
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.getImageView().setImageResource(Data.getImageId(distro.getName()));
    }

    @Override
    public long getGridItemId(int gridPosition, Distro distro) {
        return distro.getId();
    }
}
