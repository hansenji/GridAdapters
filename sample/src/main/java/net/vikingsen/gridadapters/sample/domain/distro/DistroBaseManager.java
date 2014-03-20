/*
 * DistroBaseManager.java
 *
 * GENERATED FILE - DO NOT EDIT
 * CHECKSTYLE:OFF
 * 
 */



package net.vikingsen.gridadapters.sample.domain.distro;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.vikingsen.gridadapters.sample.domain.BaseManager;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("all")
public class DistroBaseManager {


    public DistroBaseManager() {
    }

    public static void dropTable(SQLiteDatabase db) {
        BaseManager.executeSQL(db, Distro.DROP_TABLE);
    }

    public static void createTable(SQLiteDatabase db) {
        BaseManager.executeSQL(db, Distro.CREATE_TABLE);
    }

    public static long insert(SQLiteDatabase db, Distro record) {
        return BaseManager.insert(db, record);
    }

    public static int update(SQLiteDatabase db, Distro record) {
        return BaseManager.update(db, record);
    }

    public static long delete(SQLiteDatabase db, Distro record) {
        return BaseManager.delete(db, record);
    }

    public static int update(SQLiteDatabase db, ContentValues values, long rowID) {
        return BaseManager.update(db, Distro.TABLE, values, Distro.PRIMARY_KEY_COLUMN, rowID);
    }

    public static int update(SQLiteDatabase db, ContentValues values, String where, String[] whereArgs) {
        return BaseManager.update(db, Distro.TABLE, values, where, whereArgs);
    }

    public static long delete(SQLiteDatabase db, long rowID) {
        return BaseManager.delete(db, Distro.TABLE, Distro.PRIMARY_KEY_COLUMN, rowID);
    }

    public static long delete(SQLiteDatabase db, String where, String[] whereArgs) {
        return BaseManager.delete(db, Distro.TABLE, where, whereArgs);
    }

    public static Cursor findCursorBySelection(SQLiteDatabase db, String selection, String orderBy) {
        Cursor cursor = db.query(true, Distro.TABLE, Distro.ALL_KEYS, selection, null, null, null, orderBy, null);
        
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                return cursor;
            } else {
                cursor.close();
            }
        }
        return null;
    }

    public static Cursor findCursorByRowID(SQLiteDatabase db, long rowID) {
        return findCursorBySelection(db, Distro.PRIMARY_KEY_COLUMN+ "=" + rowID, null);
    }

    public static Distro findBySelection(SQLiteDatabase db, String selection, String orderBy) {
        Cursor cursor = findCursorBySelection(db, selection, null);
        if (cursor != null) {
            Distro record = new Distro();
            record.setContent(cursor);
            cursor.close();
            return record;
        } else {
            return null;
        }
    }

    public static Distro findBySelection(SQLiteDatabase db, String selection) {
        return findBySelection(db, selection, null);
    }

    public static List<Distro> findAllBySelection(SQLiteDatabase db, String selection, String orderBy) {
        Cursor cursor = findCursorBySelection(db, selection, orderBy);
        List<Distro> foundItems = new ArrayList<Distro>();
        if (cursor != null) {
            do {
                Distro record = new Distro();
                record.setContent(cursor);
                foundItems.add(record);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return foundItems;
    }

    public static List<Distro> findAllBySelection(SQLiteDatabase db, String selection) {
        return findAllBySelection(db, selection, null);
    }

    public static Distro findByRowID(SQLiteDatabase db, long rowID) {
        return findBySelection(db, Distro.PRIMARY_KEY_COLUMN+ "=" + rowID, null);
    }

    public long findCount(SQLiteDatabase db) {
        long count = -1;
        
        Cursor c = db.query(Distro.TABLE, new String[]{"count(1)"}, null, null, null, null, null);
        if (c != null) {
            if (c.getCount() > 0) {
                c.moveToFirst();
                count = c.getLong(0);
                }
            c.close();
        }
        return count;
    }


}