package com.ifyosakwe.cryptowatch.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ifyosakwe.data.models.CoinModel;
import com.ifyosakwe.data.repository.CryptoRepository;
import com.ifyosakwe.data.repository.CryptoRepositoryImpl;

import java.util.List;

public class CryptoViewModel extends AndroidViewModel {

    private CryptoRepository mCryptoRepository;
    public static final String TAG = CryptoViewModel.class.getSimpleName();

    public CryptoViewModel(@NonNull Application application) {
        super(application);
        mCryptoRepository = CryptoRepositoryImpl.create(application);
    }

    public LiveData<List<CoinModel>> getCoinsMarketData() {
        return mCryptoRepository.getCryptoCoinsData();
    }

    public LiveData<String> getErrorUpdates() {
        return mCryptoRepository.getErrorStream();
    }

    public void fetchData() {
        mCryptoRepository.fetchData();
    }

    public LiveData<Double> getTotalMarketCap() {
        return mCryptoRepository.getTotalMarketCapStream();
    }

    @Override
    protected void onCleared() {
        Log.d(TAG, "onCleared: called");
        super.onCleared();
    }

    @VisibleForTesting
    public CryptoViewModel(@NonNull Application application, CryptoRepositoryImpl repo) {
        super(application);
        this.mCryptoRepository = repo;
    }

}
