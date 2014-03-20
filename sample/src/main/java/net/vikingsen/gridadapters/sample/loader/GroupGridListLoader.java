package net.vikingsen.gridadapters.sample.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import net.vikingsen.gridadapters.sample.domain.DatabaseManager;
import net.vikingsen.gridadapters.sample.domain.distro.Distro;
import net.vikingsen.gridadapters.sample.domain.distro.DistroManager;

import java.util.List;

/**
 * Created by Jordan Hansen
 */
public class GroupGridListLoader extends AsyncTaskLoader<List<Distro>> {

    private final DatabaseManager databaseManager;

    public GroupGridListLoader(Context context) {
        super(context);
        this.databaseManager = new DatabaseManager();
        this.databaseManager.setContext(context);
    }

    @Override
    public List<Distro> loadInBackground() {
        return DistroManager.findAllBySelection(databaseManager.getWritableDatabase(DatabaseManager.MAIN_DATABASE_NAME), null, Distro.C_NAME);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
        super.onStartLoading();
    }
}
