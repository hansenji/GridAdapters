package net.vikingsen.gridadapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by Jordan Hansen
 *
 */
public abstract class GroupListAdapter<T> extends BaseAdapter {

    private final Context context;
    private final int[] itemsPerGroup;
    private final int[] sumItems;
    private final int totalItemsForGroups;
    private final int numTypes;
    private final int itemsInFristGroup;

    private List<T> data;
    private boolean allowPartialGroups = false;

    public GroupListAdapter(Context context, int[] itemsPerGroup, int itemsInFirstGroup) {
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
        this.itemsInFristGroup = itemsInFirstGroup;
    }

    public abstract View newView(Context context, ViewGroup parent, int type, int itemsInGroup);

    public abstract void bindView(View view, Context context, List<T> groupItems, int type, int itemsInGroup, int firstPosition);

    public abstract long getGroupItemId(int groupPosition, T t);

    @Override
    public int getCount() {
        int listCount = data != null ? data.size() - itemsInFristGroup : 0;
        if (listCount <= 0 || (listCount < itemsInFristGroup && !allowPartialGroups)) {
            return 0;
        }
        int count = 0;
        if (itemsInFristGroup > 0) {
            count++;
        }
        return count + getNumberOfGroups(listCount);
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

    protected int getFirstListPosition(int row) {
        if (row == 0) {
            return 0;
        }
        int p = itemsInFristGroup > 0 ? row - 1 : row;
        int q = (p / numTypes) * totalItemsForGroups + sumItems[p % numTypes];
        if (itemsInFristGroup > 0) {
            q += itemsInFristGroup;
        }
        return q;
    }

    protected int getRowForListPosition(int position) {
        if (position <= itemsInFristGroup) {
            return 0;
        }
        int q = itemsInFristGroup > 0 ? position - itemsInFristGroup : position;
        int j = q % totalItemsForGroups;
        int m;
        for (m = 1; m < numTypes; m++) {
            if (sumItems[m] > j) {
                m--;
                break;
            }
        }
        int p = (q / totalItemsForGroups) * numTypes + m;
        if (itemsInFristGroup > 0) {
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

    public T getGroupItem(int groupsPosition) {
        if (data != null && data.size() > groupsPosition) {
            return data.get(groupsPosition);
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (data == null) {
            throw new IllegalStateException("This should only be called if we have valid data");
        }
        int q = getFirstListPosition(position);
        if (q >= data.size()) {
            throw new IllegalStateException("Invalid position " + q);
        }
        int type = getItemViewType(position);
        int itemsInGroup = getItemsInGroup(type);

        View v;
        if (convertView == null) {
            v = newView(context, parent, type, itemsInGroup);
        } else {
            v = convertView;
        }

        int n = itemsInGroup;
        if ((q + itemsInGroup) > data.size()) {
            n = data.size() - q;
        }
        List<T> items = data.subList(q, q+n);
        bindView(v, context, items, type, itemsInGroup, q);

        return v;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        }
        int i = itemsInFristGroup > 0 ? position - 1 : position;
        i %= numTypes;
        if (itemsInFristGroup > 0) {
            i++;
        }
        return i;
    }

    @Override
    public int getViewTypeCount() {
        int count = itemsPerGroup.length;
        if (itemsInFristGroup > 0) {
            count++;
        }
        return count;
    }

    private int getItemsInGroup(int type) {
        if (type == 0 && itemsInFristGroup > 0) {
            return itemsInFristGroup;
        }
        if (itemsInFristGroup > 0) {
            type--;
        }
        return itemsPerGroup[type];
    }

    public void setData(List<T> data) {
        this.data = data;
        if (this.data != null) {
            notifyDataSetChanged();
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
