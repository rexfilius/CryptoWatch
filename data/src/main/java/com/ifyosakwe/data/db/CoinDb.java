package com.ifyosakwe.data.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.ifyosakwe.data.entities.CryptoCoinEntity;

@Database(entities = {CryptoCoinEntity.class}, version = 1)
public abstract class CoinDb extends RoomDatabase {

    static final String DATABASE_NAME = "market_data";
    private static CoinDb INSTANCE;

    public abstract CoinDao coinDao();

    public static CoinDb getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                    context.getApplicationContext(),
                    CoinDb.class,
                    DATABASE_NAME)
                    .build();
        }
        return INSTANCE;
    }

}
