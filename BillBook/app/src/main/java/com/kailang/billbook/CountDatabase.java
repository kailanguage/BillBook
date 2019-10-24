package com.kailang.billbook;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Count.class},version = 1,exportSchema = false)
public abstract class CountDatabase extends RoomDatabase {
    private static CountDatabase INSTANCE;

    static synchronized CountDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),CountDatabase.class,"count_database")
                    .allowMainThreadQueries()//允许主线程查询
                    .build();
        }
        return INSTANCE;
    }
    public abstract CountDao getCountDao();
}
