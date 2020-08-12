package com.ifyosakwe.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ifyosakwe.data.entities.CryptoCoinEntity;

import java.util.List;

public interface CoinDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCoins(List<CryptoCoinEntity> coins);

    LiveData<List<CryptoCoinEntity>> getAllCoinsLive();

    List<CryptoCoinEntity> getAllCoins();

    LiveData<List<CryptoCoinEntity>> getCoins(int limit);

    LiveData<CryptoCoinEntity> getCoin(String symbol);

    void deleteAllCoins();




}
