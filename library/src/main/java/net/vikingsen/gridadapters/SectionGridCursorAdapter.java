package net.vikingsen.gridadapters;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.util.DisplayMetrics;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import net.vikingsen.gridadapters.library.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jordan Hansen
 *
 */
public abstract class SectionGridCursorAdapter extends BaseAdapter implements View.OnClickListener, View.OnLongClickListener {

    private final DisplayMetrics displayMetrics;

    private final Resources resources;

    protected final int numColumns;

    private final Context context;
    private final LayoutInflater inflater;

    private Cursor cursor;
    private int verticalSpacing = 0;
    private int horizontalSpacing = 0;
    private Section[] sections;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;
    private SparseBooleanArray checked;
    private List<Long> checkedIds;

    public SectionGridCursorAdapter(Context context, int numColumns) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        if (numColumns < 1) {
            throw new IllegalStateException("Cannot have less than 1 column");
        }
        this.numColumns = numColumns;
        this.resources = context.getResources();
        this.displayMetrics = this.resources.getDisplayMetrics();
        resetChecked();
    }

    public abstract View newView(Context context, Cursor cursor, ViewGroup parent);

    public abstract void bindView(View view, Context context, Cursor cursor);

    @Override
    public int getCount() {
        int numRows = 0;
        int numSections = sections == null ? 0 : sections.length;
        for (int i = 0; i < numSections; i++) {
            sections[i].firstRow = numRows;
            int count = sections[i].numItems;
            if (count > 0) {
                numRows += (count - 1) / numColumns + 1;
            }
        }
        return numRows;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public Cursor getItem(int position) {
        return null;
    }

    public Cursor getGridItem(int gridPosition) {
        if (cursor != null && cursor.moveToPosition(gridPosition)) {
            return cursor;
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public long getGridItemId(int gridPosition) {
        if (cursor != null && cursor.moveToPosition(gridPosition)) {
            return cursor.getLong(cursor.getColumnIndex("_id"));
        }
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (cursor == null || cursor.isClosed()) {
            throw new IllegalStateException("this should only be called when the cursor is valid");
        }

        Row r = getRow(position);

        // Handle row views
        return getRowView(convertView, r, position, parent);
    }

    private View getRowView(View convertView, Row row, int position, ViewGroup parent) {
        ViewHolder holder;
        if (convertView != null && !(convertView instanceof LinearLayout)) {
            throw new IllegalStateException("Convert view should be a LinearLayout but isn't");
        }

        LinearLayout rowView = (LinearLayout)convertView;
        if (rowView == null) {
            // Inflate
            rowView = (LinearLayout) inflater.inflate(R.layout.grid_row, parent, false);
            holder = new ViewHolder();
            // vertical is 0 because the items set the space between each row.
            rowView.setPadding(horizontalSpacing, 0, horizontalSpacing, 0);
            for (int i = 0; i < numColumns; i++) {
                FrameLayout cell = (FrameLayout) inflater.inflate(R.layout.grid_cell, rowView, false);
                cell.setOnClickListener(this);
                cell.setOnLongClickListener(this);
                holder.cells.add(cell);
                rowView.addView(cell);
            }
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        bindRowView(holder, row, position);

        return rowView;
    }

    private void bindRowView(ViewHolder holder, Row row, int position) {
        for (int i = 0; i < numColumns; i++) {
            FrameLayout v = holder.cells.get(i);
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            if (lp != null) {
                lp.setMargins(horizontalSpacing, position == 0 ? verticalSpacing << 1 : verticalSpacing, horizontalSpacing, verticalSpacing);
                v.setLayoutParams(lp);
            }
            if (i < row.numItems) {
                Integer cursorPos = row.firstCursorPosition + i;
                if (!cursor.moveToPosition(cursorPos)) {
                    throw new IllegalStateException("couldn't move cursor to position " + cursorPos);
                }
                v.setVisibility(View.VISIBLE);
                View child;
                if (v.getChildCount() == 0) {
                    child = newView(context, cursor, v);
                    v.addView(child);
                } else {
                    child = v.getChildAt(0);
                }
                v.setTag(cursorPos);
                bindView(child, context, cursor);
                v.setForeground(isItemChecked(cursorPos) ? resources.getDrawable(R.drawable.ga_list_activated_holo) : resources.getDrawable(R.drawable.ga_item_background_holo_light));
            } else {
                v.setVisibility(View.INVISIBLE);
            }
        }
    }

    protected Context getContext() {
        return context;
    }

    public Cursor getCursor() {
        return cursor;
    }

    public Section getSection(int rowNum) {
        if (sections != null && sections.length > 0 && rowNum >= 0) {
            Section s = sections[0];
            int lo = 0;
            int hi = sections.length - 1;
            int index= 0;
            while (lo <= hi) {
                int mid = (lo + hi) >>> 1;
                index = mid;
                s = sections[mid];

                if (s.firstRow < rowNum) {
                    lo = mid + 1;
                } else if (s.firstRow > rowNum) {
                    hi = mid - 1;
                } else {
                    return s;
                }
            }
            if (s.firstRow > rowNum) {
                s = sections[index - 1];
            }
            return s;
        }
        return null;
    }

    /**
     *
     * @param verticalSpacing between items in dp
     */
    public void setVerticalSpacing(int verticalSpacing) {
        this.verticalSpacing = (int)(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, verticalSpacing, displayMetrics) / 2);
    }

    /**
     *
     * @param horizontalSpacing between items in dp
     */
    public void setHorizontalSpacing(int horizontalSpacing) {
        this.horizontalSpacing = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, horizontalSpacing, displayMetrics) / 2);
    }

    public void setSections(Section[] sections) {
        setSections(sections, 0);
    }

    public void setSections(Section[] sections, int firstCursorPosition) {
        this.sections = sections == null ? null : sections.clone();
        int count = this.sections == null ? 0 : this.sections.length;
        int pos = firstCursorPosition;
        for (int i = 0; i < count; i++) {
            Section s = sections[i];
            s.firstCursorPosition = pos;
            pos += s.numItems;
        }
        notifyDataSetChanged();
    }

    private Row getRow(int rowNum) {
        Section section = getSection(rowNum);
        int numItems = section.numItems - ((rowNum - section.firstRow) * numColumns);
        if (numItems > numColumns) {
            numItems = numColumns;
        }
        int firstCursorPosition = section.firstCursorPosition + ((rowNum - section.firstRow) * numColumns);
        return new Row(section, firstCursorPosition, numItems);
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
        resetChecked();
    }

    private void resetChecked() {
        checked = new SparseBooleanArray();
        checkedIds = new ArrayList<>();
    }

    @Override
    public void onClick(View v) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(this, ((FrameLayout)v).getChildAt(0), (Integer) v.getTag());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return onItemLongClickListener != null && onItemLongClickListener.onItemLongClick(this, ((FrameLayout) v).getChildAt(0), (Integer) v.getTag());
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public void setItemChecked(int position, boolean checked) {
        long id = getGridItemId(position);
        if (checked) {
            this.checked.put(position, true);
            this.checkedIds.add(id);
        } else {
            this.checked.delete(position);
            this.checkedIds.remove(id);
        }
        notifyDataSetChanged();
    }

    public boolean isItemChecked(int position) {
        return checked.get(position, false);
    }

    public Long[] getCheckedItemIds() {
        return checkedIds.toArray(new Long[checkedIds.size()]);
    }

    public void clearChecked() {
        resetChecked();
        notifyDataSetChanged();
    }

    private static class Row {
        private Section section;
        private int firstCursorPosition;
        private int numItems;

        public Row(Section section, int firstCursorPosition, int numItems) {
            this.section = section;
            this.firstCursorPosition = firstCursorPosition;
            this.numItems = numItems;
        }

        @Override
        public String toString() {
            return "Row{" +
                    "section=" + section +
                    ", firstDataPosition=" + firstCursorPosition +
                    ", numItems=" + numItems +
                    '}';
        }
    }

    public static class Section {
        protected int firstRow;
        protected int firstCursorPosition;
        protected int numItems;

        public Section(int numItems) {
            this.numItems = numItems;
        }
    }

    static class ViewHolder {
        List<FrameLayout> cells = new ArrayList<>();
    }

    public interface OnItemClickListener {
        void onItemClick(SectionGridCursorAdapter adapter, View view, int position);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(SectionGridCursorAdapter adapter, View view, int position);
    }
}
