package net.vikingsen.gridadapters.sample.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.squareup.otto.Bus;
import com.squareup.otto.Produce;

import net.vikingsen.gridadapters.sample.R;
import net.vikingsen.gridadapters.sample.event.AndroidBus;
import net.vikingsen.gridadapters.sample.event.GridSelectedEvent;
import net.vikingsen.gridadapters.sample.event.TitleEvent;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Jordan Hansen
 */
public class MainFragment extends Fragment implements AdapterView.OnItemClickListener {

    private static final String ARG_TITLE = "ARG_TITLE";

    @InjectView(R.id.listview)
    ListView listView;

    private Bus bus;
    private String title;

    public static Fragment newInstance(String title) {
        Fragment fragment = new MainFragment();
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


        Resources resources = getResources();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, resources.getStringArray(R.array.grid_types));
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

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
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        bus.post(new GridSelectedEvent(position));
    }

    @Produce
    public TitleEvent produceTitle() {
        return new TitleEvent(title);
    }
}
