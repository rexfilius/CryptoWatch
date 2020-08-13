package com.ifyosakwe.data.repository.datasource;

import android.content.Context;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ifyosakwe.data.db.CoinDb;
import com.ifyosakwe.data.entities.CryptoCoinEntity;

import java.util.List;

public class LocalDataSource implements DataSource<List<CryptoCoinEntity>> {

    private final CoinDb mCoinDb;
    private final MutableLiveData<String> mError = new MutableLiveData<>();

    public LocalDataSource(Context mAppContext) {
        mCoinDb = CoinDb.getDatabase(mAppContext);
    }

    @Override
    public LiveData<List<CryptoCoinEntity>> getDataStream() {
        return mCoinDb.coinDao().getAllCoinsLive();
    }

    @Override
    public LiveData<String> getErrorStream() {
        return mError;
    }

    public void writeData(List<CryptoCoinEntity> coins) {
        try {
            mCoinDb.coinDao().insertCoins(coins);
        } catch (Exception e) {
            e.printStackTrace();
            mError.postValue(e.getMessage());
        }
    }

    public List<CryptoCoinEntity> getAllCoins() {
        return mCoinDb.coinDao().getAllCoins();
    }

    @VisibleForTesting
    public void deleteAllCoins() {
        mCoinDb.coinDao().deleteAllCoins();
    }
}
