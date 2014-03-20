package net.vikingsen.gridadapters;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by Jordan Hansen
 *
 */
public abstract class GroupCursorAdapter extends BaseAdapter {

    private final Context context;
    private final int[] itemsPerGroup;
    private final int[] sumItems;
    private final int totalItemsForGroups;
    private final int numTypes;
    private final int itemsInFirstGroup;

    private Cursor cursor;
    private boolean allowPartialGroups = false;

    public GroupCursorAdapter(Context context, int[] itemsPerGroup, int itemsInFirstGroup) {
        this.context = context;

        this.itemsPerGroup = itemsPerGroup;
        int n = itemsPerGroup.length;
        this.sumItems = new int[n];
        int s = 0;
        for (int i = 0; i < n; i++) { // NOSONAR Sum loop not copy
            this.sumItems[i] = s;
            s += itemsPerGroup[i];
        }
        this.totalItemsForGroups = s;
        this.numTypes = n;
        this.itemsInFirstGroup = itemsInFirstGroup;
    }

    public abstract View newView(Context context, Cursor cursor, ViewGroup parent, int type, int itemsInGroup);

    public abstract void bindView(View view, Context context, Cursor cursor, int type, int groupPosition, int itemsInGroup);

    @Override
    public int getCount() {
        int cursorCount = cursor != null ? cursor.getCount() - itemsInFirstGroup : 0;
        if (cursorCount <= 0 || (cursorCount < itemsInFirstGroup && !allowPartialGroups)) {
            return 0;
        }
        int count = 0;
        if (itemsInFirstGroup > 0) {
            count++;
        }
        return count + getNumberOfGroups(cursorCount);
    }

    private int getNumberOfGroups(int n) {
        int count = numTypes;
        int groups = n / totalItemsForGroups;
        groups *= count;
        int remainder = n % totalItemsForGroups;
        for (int i = 0; i < count - 1; i++) {
            int g = itemsPerGroup[i];
            if (remainder >= g) {
                groups++;
                remainder -= g;
            } else {
                break;
            }
        }
        if (allowPartialGroups && remainder > 0) {
            groups++;
        }
        return groups;
    }

    protected int getFirstCursorPosition(int row) {
        if (row == 0) {
            return 0;
        }
        int p = itemsInFirstGroup > 0 ? row - 1 : row;
        int q = (p / numTypes) * totalItemsForGroups + sumItems[p % numTypes];
        if (itemsInFirstGroup > 0) {
            q += itemsInFirstGroup;
        }
        return q;
    }

    /**
     * Based off the formula
     * t = number of recurring groups
     * q = cursor position [- offset]
     * <p/>
     * j = (q) % T
     * <p/>
     * p = ((q) / T) * t + m(j) [+ 1 if offset]
     * <p/>
     * m(j) = the i that Σ(n(i-1)) < j < Σ(n(i)) as i goes to 0
     * n(i) = the number of items in group i;
     *
     * @param position cursor position
     * @return the row that contains that cursor position
     */
    protected int getRowForCursorPosition(int position) {
        if (position <= itemsInFirstGroup) {
            return 0;
        }
        int q = itemsInFirstGroup > 0 ? position - itemsInFirstGroup : position;
        int j = q % totalItemsForGroups;
        int m;
        for(m = 1; m < numTypes; m++) {
            if (sumItems[m] > j) {
                m--;
                break;
            }
        }
        int p = (q / totalItemsForGroups) * numTypes + m;
        if (itemsInFirstGroup > 0) {
            p++;
        }
        return p;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    public Cursor getGroupItem(int groupsPosition) {
        if (cursor != null && cursor.moveToPosition(groupsPosition)) {
            return cursor;
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public long getGroupItemId(int groupsPosition) {
        if (cursor != null && cursor.moveToPosition(groupsPosition)) {
            return cursor.getLong(cursor.getColumnIndex("_id"));
        }
        return 0L;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (cursor == null) {
            throw new IllegalStateException("Cursor is null");
        }
        int q = getFirstCursorPosition(position);
        if (!cursor.moveToPosition(q)) {
            throw new IllegalStateException("Can't move cursor to position: " + q);
        }
        int type = getItemViewType(position);
        int itemsInGroup = getItemsInGroup(type);
        View v;
        if (convertView == null) {
            v = newView(context, cursor, parent, type, itemsInGroup);
        } else {
            v = convertView;
        }
        Cursor c = cursor;
        for (int i = 0; i < itemsInGroup; i++) {
            bindView(v, context, c, type, i, itemsInGroup);
            if (c != null && !c.moveToNext()) {
                c = null;
            }
        }
        return v;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        }
        int i = itemsInFirstGroup > 0 ? position - 1 : position;
        i %= numTypes;
        if (itemsInFirstGroup > 0) {
            i++;
        }
        return i;
    }

    @Override
    public int getViewTypeCount() {
        int count = itemsPerGroup.length;
        if (itemsInFirstGroup > 0) {
            count++;
        }
        return count;
    }

    public int getItemsInGroup(int type) {
        if (type == 0 && itemsInFirstGroup > 0) {
            return itemsInFirstGroup;
        }
        if (itemsInFirstGroup > 0) {
            type--;
        }
        return itemsPerGroup[type];
    }

    public void changeCursor(Cursor newCursor) {
        if (newCursor == this.cursor) {
            return;
        }

        Cursor oldCursor = this.cursor;
        this.cursor = newCursor;
        if (this.cursor != null) {
            notifyDataSetChanged();
        }

        if (oldCursor != null) {
            oldCursor.close();
        }
    }

    public boolean allowPartialGroups() {
        return allowPartialGroups;
    }

    /**
     * Default is false
     * @param allowPartialGroups
     */
    public void setAllowPartialGroups(boolean allowPartialGroups) {
        this.allowPartialGroups = allowPartialGroups;
        notifyDataSetChanged();
    }


}
