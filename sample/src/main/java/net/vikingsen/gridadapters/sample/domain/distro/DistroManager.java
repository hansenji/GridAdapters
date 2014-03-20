/*
 * DistroManager.java
 *
 * Generated on: 03/16/2014 07:23:16
 *
 */



package net.vikingsen.gridadapters.sample.domain.distro;


import android.database.sqlite.SQLiteDatabase;

import java.util.List;

public class DistroManager extends DistroBaseManager {


    public DistroManager() {
    }


    public static List<Distro> findAll(SQLiteDatabase db) {
        return findAllBySelection(db, null, Distro.C_NAME);
    }
}