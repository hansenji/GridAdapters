package net.vikingsen.gridadapters.sample.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import net.vikingsen.gridadapters.SectionGridListAdapter;
import net.vikingsen.gridadapters.sample.domain.DatabaseManager;
import net.vikingsen.gridadapters.sample.domain.SectionGridList;
import net.vikingsen.gridadapters.sample.domain.distro.Distro;
import net.vikingsen.gridadapters.sample.domain.distro.DistroManager;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Jordan Hansen
 */
public class SectionGridListLoader extends AsyncTaskLoader<SectionGridList<Distro>> {

    private final DatabaseManager databaseManager;

    public SectionGridListLoader(Context context) {
        super(context);
        this.databaseManager = new DatabaseManager();
        this.databaseManager.setContext(context);
    }

    @Override
    public SectionGridList<Distro> loadInBackground() {
        List<Distro> data = DistroManager.findAll(databaseManager.getWritableDatabase(DatabaseManager.MAIN_DATABASE_NAME));
        return new SectionGridList<>(data, getSections(data));
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
        super.onStartLoading();
    }

    private SectionGridListAdapter.Section[] getSections(List<Distro> data) {
        if (data == null || data.size() == 0) {
            return new SectionGridListAdapter.Section[0];
        }

        List<SectionGridListAdapter.Section> sections = new ArrayList<>();
        char current = data.get(0).getName().charAt(0);
        int length = data.size();
        int count = 1;
        for (int i = 1; i < length; i++) {
            char c = data.get(i).getName().charAt(0);
            if (c == current) {
                count++;
            } else {
                current = c;
                sections.add(new SectionGridListAdapter.Section(count));
                count = 1;
            }
        }
        sections.add(new SectionGridListAdapter.Section(count));


        return sections.toArray(new SectionGridListAdapter.Section[sections.size()]);

    }
}
