package net.vikingsen.gridadapters.sample.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import net.vikingsen.gridadapters.sample.R;
import net.vikingsen.gridadapters.sample.event.AndroidBus;
import net.vikingsen.gridadapters.sample.event.GridSelectedEvent;
import net.vikingsen.gridadapters.sample.event.TitleEvent;


public class MainActivity extends ActionBarActivity {

    private static final String MAIN_TITLE = "Grid Adapters";

    private Bus bus;

    String[] gridTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container, MainFragment.newInstance(MAIN_TITLE)).commit();
        }

        gridTypes = getResources().getStringArray(R.array.grid_types);
        bus = AndroidBus.getBus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bus.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        bus.unregister(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Subscribe
    public void onTitleEvent(TitleEvent event) {
        String title = event.getTitle();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title);
        actionBar.setDisplayHomeAsUpEnabled(!MAIN_TITLE.equals(title));
        actionBar.setHomeButtonEnabled(!MAIN_TITLE.equals(title));
    }

    @Subscribe
    public void onGridSelectedEvent(GridSelectedEvent event) {
        String title = gridTypes[event.getId()];
        Fragment fragment;
        switch (event.getId()) {
            case 0:
                fragment = SectionGridCursorFragment.newInstance(title);
                break;
            case 1:
                fragment = SectionGridListFragment.newInstance(title);
                break;
            case 2:
                fragment = StickySectionHeaderGridCursorFragment.newInstance(title);
                break;
            case 3:
                fragment = StickySectionHeaderGridListFragment.newInstance(title);
                break;
            case 4:
                fragment = GroupCursorFragment.newInstance(title);
                break;
            case 5:
                fragment = GroupListFragment.newInstance(title);
                break;
            default:
                throw new IllegalStateException("Invalid Event Id: " + event.getId());
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, fragment).addToBackStack(title).commit();
    }
}
