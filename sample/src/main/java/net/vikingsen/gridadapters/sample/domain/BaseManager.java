package net.vikingsen.gridadapters.sample.domain;

import android.database.sqlite.SQLiteDatabase;

import org.dbtools.android.domain.AndroidBaseManager;

public abstract class BaseManager<T extends BaseRecord> extends AndroidBaseManager<T> {

    public DatabaseManager databaseManager;

    public SQLiteDatabase getReadableDatabase() {
        return databaseManager.getReadableDatabase(getDatabaseName());
    }

    public SQLiteDatabase getReadableDatabase(String databaseName) {
        return databaseManager.getReadableDatabase(databaseName);
    }

    public SQLiteDatabase getWritableDatabase() {
        return databaseManager.getWritableDatabase(getDatabaseName());
    }

    public SQLiteDatabase getWritableDatabase(String databaseName) {
        return databaseManager.getWritableDatabase(databaseName);
    }

}
