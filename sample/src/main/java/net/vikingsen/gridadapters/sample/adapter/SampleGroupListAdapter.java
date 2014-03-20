package net.vikingsen.gridadapters.sample.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import net.vikingsen.gridadapters.GroupListAdapter;
import net.vikingsen.gridadapters.sample.R;
import net.vikingsen.gridadapters.sample.domain.Data;
import net.vikingsen.gridadapters.sample.domain.distro.Distro;
import net.vikingsen.gridadapters.sample.view.GroupHolder;

import java.util.List;

/**
 * Created by Jordan Hansen
 */
public class SampleGroupListAdapter extends GroupListAdapter<Distro> implements View.OnClickListener {

    private final LayoutInflater inflater;

    private OnItemClickListener onItemClickListener;

    public SampleGroupListAdapter(Context context) {
        super(context, context.getResources().getIntArray(R.array.groups), context.getResources().getInteger(R.integer.items_in_first_group));
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, ViewGroup parent, int type, int itemsInGroup) {
        // first group
        if (type == 0) {
            View v = inflater.inflate(R.layout.group_first_item, parent, false);
            v.setTag(new GroupHolder(v));
            return v;
        }
        // Recurring groups
        int layoutId;
        switch (itemsInGroup) {
            case 2:
                layoutId = R.layout.group_two_item;
                break;
            case 3:
                layoutId = R.layout.group_three_item;
                break;
            default:
                throw new IllegalStateException("Invalid Number of Items: " + type);
        }
        View v = inflater.inflate(layoutId, parent, false);
        v.setTag(new GroupHolder(v));
        return v;
    }

    @Override
    public void bindView(View view, Context context, List<Distro> groupItems, int type, int itemsInGroup, int firstPosition) {
        final GroupHolder holder = (GroupHolder) view.getTag();
        int numItems = groupItems.size();
        for (int i = 0; i < itemsInGroup; i++) {
            ImageView imageView = holder.getImage(i);
            if (i >= numItems) {
                imageView.setVisibility(View.INVISIBLE);
                continue;
            }
            imageView.setVisibility(View.VISIBLE);
            Distro distro = groupItems.get(i);
            imageView.setImageResource(Data.getImageId(distro.getName()));
            imageView.setOnClickListener(this);
            imageView.setTag(firstPosition + i);
        }
    }

    @Override
    public long getGroupItemId(int groupPosition, Distro distro) {
        return distro.getId();
    }

    @Override
    public void onClick(View view) {
        if (onItemClickListener != null) {
            this.onItemClickListener.onItemClick(this, view, (Integer)view.getTag());
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public static interface OnItemClickListener {
        void onItemClick(SampleGroupListAdapter adapter, View view, int position);
    }
}
