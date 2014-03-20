package net.vikingsen.gridadapters.sample.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.squareup.otto.Bus;
import com.squareup.otto.Produce;

import net.vikingsen.gridadapters.SectionGridCursorAdapter;
import net.vikingsen.gridadapters.sample.R;
import net.vikingsen.gridadapters.sample.adapter.SampleStickySectionGridCursorAdapter;
import net.vikingsen.gridadapters.sample.domain.SectionGridCursor;
import net.vikingsen.gridadapters.sample.domain.distro.Distro;
import net.vikingsen.gridadapters.sample.event.AndroidBus;
import net.vikingsen.gridadapters.sample.event.TitleEvent;
import net.vikingsen.gridadapters.sample.loader.SectionGridCursorLoader;

import butterknife.ButterKnife;
import butterknife.InjectView;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by Jordan Hansen
 */
public class StickySectionHeaderGridCursorFragment extends Fragment implements LoaderManager.LoaderCallbacks<SectionGridCursor>,SectionGridCursorAdapter.OnItemClickListener, SectionGridCursorAdapter.OnItemLongClickListener {

    private static final String ARG_TITLE = "ARG_TITLE";

    @InjectView(R.id.stickylistview)
    StickyListHeadersListView listView;

    private SampleStickySectionGridCursorAdapter adapter;
    private ActionMode actionMode = null;
    private Bus bus;
    private String title;


    public static Fragment newInstance(String title) {
        Fragment fragment = new StickySectionHeaderGridCursorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sticky_list, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listView.setDividerHeight(0);
        listView.setFastScrollEnabled(true);

        getLoaderManager().initLoader(0, null, this);

        bus = AndroidBus.getBus();
        Bundle args = getArguments();
        if (args != null) {
            title = args.getString(ARG_TITLE);
        }
        bus.post(new TitleEvent(title));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        bus.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        bus.unregister(this);
    }

    @Override
    public Loader<SectionGridCursor> onCreateLoader(int id, Bundle args) {
        return new SectionGridCursorLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<SectionGridCursor> loader, SectionGridCursor data) {
        if (adapter == null) {
            adapter = new SampleStickySectionGridCursorAdapter(getActivity(), getResources().getInteger(R.integer.num_columns));
            adapter.setHorizontalSpacing(10);
            adapter.setVerticalSpacing(10);
            adapter.setOnItemClickListener(this);
            adapter.setOnItemLongClickListener(this);
        }
        if (listView.getAdapter() == null) {
            listView.setAdapter(adapter);
        }
        adapter.changeCursor(data.getCursor());
        adapter.setSections(data.getSections());
    }

    @Override
    public void onLoaderReset(Loader<SectionGridCursor> loader) {
        if (adapter != null) {
            adapter.changeCursor(null);
        }
    }


    @Override
    public void onItemClick(SectionGridCursorAdapter adapter, View view, int position) {
        if (actionMode != null) {
            adapter.setItemChecked(position, !adapter.isItemChecked(position));
        } else {
            Cursor cursor = adapter.getGridItem(position);
            Distro distro = new Distro(cursor);
            Toast.makeText(getActivity(), distro.getName(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onItemLongClick(SectionGridCursorAdapter adapter, View view, int position) {
        if (actionMode != null) {
            return false;
        }

        actionMode = ((ActionBarActivity)getActivity()).startSupportActionMode(actionModeCallback);
        adapter.setItemChecked(position, true);
        return true;
    }

    private ActionMode.Callback actionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem menuItem) {
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            actionMode = null;
            adapter.clearChecked();
        }
    };

    @Produce
    public TitleEvent produceTitle() {
        return new TitleEvent(title);
    }
}
