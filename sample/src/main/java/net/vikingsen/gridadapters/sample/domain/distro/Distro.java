/*
 * Distro.java
 *
 * Created: 03/16/2014 07:23:16
 */



package net.vikingsen.gridadapters.sample.domain.distro;

import android.database.Cursor;
import android.content.ContentValues;


public class Distro extends DistroBaseRecord {


    public static final String C_COUNT = "count";

    public Distro(Cursor cursor) {
        setContent(cursor);
    }

    public Distro(ContentValues values) {
        setContent(values);
    }

    public Distro() {
    }


}