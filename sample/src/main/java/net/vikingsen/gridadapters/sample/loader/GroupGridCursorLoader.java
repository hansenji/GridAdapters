package net.vikingsen.gridadapters.sample.loader;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

import net.vikingsen.gridadapters.sample.domain.DatabaseManager;
import net.vikingsen.gridadapters.sample.domain.distro.Distro;
import net.vikingsen.gridadapters.sample.domain.distro.DistroManager;

/**
 * Created by Jordan Hansen
 */
public class GroupGridCursorLoader extends CursorLoader {
    private final DatabaseManager databaseManager;

    public GroupGridCursorLoader(Context context) {
        super(context);
        databaseManager = new DatabaseManager();
        databaseManager.setContext(context);
    }

    @Override
    public Cursor loadInBackground() {
        return DistroManager.findCursorBySelection(databaseManager.getWritableDatabase(DatabaseManager.MAIN_DATABASE_NAME), null, Distro.C_NAME);
    }
}
