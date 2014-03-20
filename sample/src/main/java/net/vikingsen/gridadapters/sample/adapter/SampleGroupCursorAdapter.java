package net.vikingsen.gridadapters.sample.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import net.vikingsen.gridadapters.GroupCursorAdapter;
import net.vikingsen.gridadapters.sample.R;
import net.vikingsen.gridadapters.sample.domain.Data;
import net.vikingsen.gridadapters.sample.domain.distro.Distro;
import net.vikingsen.gridadapters.sample.view.GroupHolder;

/**
 * Created by Jordan Hansen
 */
public class SampleGroupCursorAdapter extends GroupCursorAdapter implements View.OnClickListener {

    private final LayoutInflater inflater;

    private OnItemClickListener onItemClickListener;

    public SampleGroupCursorAdapter(Context context) {
        super(context, context.getResources().getIntArray(R.array.groups), context.getResources().getInteger(R.integer.items_in_first_group));
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent, int type, int itemsInGroup) {
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
    public void bindView(View view, Context context, Cursor cursor, int type, int groupPosition, int itemsInGroup) {
        final GroupHolder holder = (GroupHolder) view.getTag();
        if (cursor != null) {
            Distro distro = new Distro(cursor);
            ImageView imageView = holder.getImage(groupPosition);
            imageView.setImageResource(Data.getImageId(distro.getName()));
            imageView.setVisibility(View.VISIBLE);
            imageView.setOnClickListener(this);
            imageView.setTag(cursor.getPosition());
        } else {
            holder.getImage(groupPosition).setVisibility(View.INVISIBLE);
        }
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

    public interface OnItemClickListener {
        void onItemClick(SampleGroupCursorAdapter adapter, View view, int position);
    }
}
