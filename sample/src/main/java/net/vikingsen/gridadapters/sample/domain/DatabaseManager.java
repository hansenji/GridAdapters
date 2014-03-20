package net.vikingsen.gridadapters.sample.domain;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import net.vikingsen.gridadapters.sample.R;
import net.vikingsen.gridadapters.sample.domain.distro.Distro;

import org.dbtools.android.domain.AndroidDatabase;
import org.dbtools.android.domain.AndroidDatabaseManager;

import java.io.File;


/**
 * This class helps open, create, and upgrade the database file.
 */

public class DatabaseManager extends AndroidDatabaseManager {
    private static final String TAG = "Sample.DatabaseManager";

    public static final int DATABASE_VERSION = 1;
    public static final String MAIN_DATABASE_NAME = "main"; // !!!! WARNING be SURE this matches the value in the schema.xml !!!!

    public Context context;

    @Override
    public void identifyDatabases() {
        addDatabase(context, MAIN_DATABASE_NAME, DATABASE_VERSION);
    }

    @Override
    public void onCreate(AndroidDatabase androidDatabase) {
        Log.i(TAG, "Creating database: " + androidDatabase.getName());
            // begin transaction
            SQLiteDatabase database = androidDatabase.getSqLiteDatabase();
            database.beginTransaction();

            createTable(database);
            initializeData(database);

            // end transaction
            database.setTransactionSuccessful();
            database.endTransaction();
    }

    private void initializeData(SQLiteDatabase database) {
        String[] data = context.getResources().getStringArray(R.array.distros);
        for (String s : data) {
            Distro distro = new Distro();
            distro.setName(s);
            BaseManager.save(database, distro);
        }
    }

    public void createTable(SQLiteDatabase database) {
        BaseManager.createTable(database, Distro.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(AndroidDatabase androidDatabase, int oldVersion, int newVersion) {
        Log.i(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion);

        // Wipe database version if it is different.
        if (MAIN_DATABASE_NAME.equals(androidDatabase.getName()) && oldVersion != DATABASE_VERSION) {
            onCleanDatabase(androidDatabase);
        }
    }

    @Override
    public void onCleanDatabase(AndroidDatabase androidDatabase) {
        Log.i(TAG, "Cleaning Database");
        SQLiteDatabase database = androidDatabase.getSqLiteDatabase();
        String databasePath = androidDatabase.getPath();
        database.close();

        Log.i(TAG, "Deleting database: [" + databasePath + "]");
        File databaseFile = new File(databasePath);
        if (databaseFile.exists() && !databaseFile.delete()) {
            String errorMessage = "FAILED to delete database: [" + databasePath + "]";
            Log.e(TAG, errorMessage);
            throw new IllegalStateException(errorMessage);
        }

        connectDatabase(androidDatabase.getName(), false);  // do not update here, because it will cause a recursive call
    }

    // ONLY needed if NOT using injection
    public void setContext(Context context) {
        this.context = context;
    }
}
