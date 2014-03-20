package net.vikingsen.gridadapters.sample.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.squareup.otto.Bus;
import com.squareup.otto.Produce;

import net.vikingsen.gridadapters.sample.R;
import net.vikingsen.gridadapters.sample.adapter.SampleGroupCursorAdapter;
import net.vikingsen.gridadapters.sample.domain.distro.Distro;
import net.vikingsen.gridadapters.sample.event.AndroidBus;
import net.vikingsen.gridadapters.sample.event.TitleEvent;
import net.vikingsen.gridadapters.sample.loader.GroupGridCursorLoader;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Jordan Hansen
 */
public class GroupCursorFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,SampleGroupCursorAdapter.OnItemClickListener {

    private static final String ARG_TITLE = "ARG_TITLE";

    @InjectView(R.id.listview)
    ListView listView;

    private Bus bus;
    private String title;
    private SampleGroupCursorAdapter adapter;

    public static Fragment newInstance(String title) {
        Fragment fragment = new GroupCursorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);

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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.group_adapter, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem partialGroup = menu.findItem(R.id.menu_partial_groups);
        if (partialGroup != null) {
            partialGroup.setChecked(adapter != null && adapter.allowPartialGroups());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_partial_groups) {
            if (adapter != null) {
                adapter.setAllowPartialGroups(!adapter.allowPartialGroups());
                item.setChecked(adapter.allowPartialGroups());
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Produce
    public TitleEvent produceTitle() {
        return new TitleEvent(title);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new GroupGridCursorLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (adapter == null) {
            adapter = new SampleGroupCursorAdapter(getActivity());
            adapter.setOnItemClickListener(this);
        }
        if (listView.getAdapter() == null) {
            listView.setAdapter(adapter);
        }
        adapter.changeCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (adapter != null) {
            adapter.changeCursor(null);
        }
    }

    @Override
    public void onItemClick(SampleGroupCursorAdapter adapter, View view, int position) {
        Cursor cursor = adapter.getGroupItem(position);
        Distro distro = new Distro(cursor);
        Toast.makeText(getActivity(), distro.getName(), Toast.LENGTH_SHORT).show();
    }
}
