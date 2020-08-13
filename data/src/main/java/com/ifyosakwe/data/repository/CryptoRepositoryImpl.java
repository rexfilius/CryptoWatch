package com.ifyosakwe.data.repository;

import android.content.Context;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Transformations;

import com.ifyosakwe.data.entities.CryptoCoinEntity;
import com.ifyosakwe.data.mappers.CryptoMapper;
import com.ifyosakwe.data.models.CoinModel;
import com.ifyosakwe.data.repository.datasource.LocalDataSource;
import com.ifyosakwe.data.repository.datasource.RemoteDataSource;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CryptoRepositoryImpl implements CryptoRepository {

    private static final String TAG = CryptoRepositoryImpl.class.getSimpleName();
    private ExecutorService mExecutorService = Executors.newFixedThreadPool(5);
    private final RemoteDataSource mRemoteDataSource;
    private final LocalDataSource mLocalDataSource;
    private final CryptoMapper mCryptoMapper;
    MediatorLiveData<List<CoinModel>> mDataMerger = new MediatorLiveData<>();
    MediatorLiveData<String> mErrorMerger = new MediatorLiveData<>();

    private CryptoRepositoryImpl(RemoteDataSource mRemoteDataSource,
                                 LocalDataSource mLocalDataSource,
                                 CryptoMapper cryptoMapper) {
        this.mRemoteDataSource = mRemoteDataSource;
        this.mLocalDataSource = mLocalDataSource;
        mCryptoMapper = cryptoMapper;

        mDataMerger.addSource(this.mRemoteDataSource.getDataStream(), entities ->
                mExecutorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        CryptoRepositoryImpl.this.mLocalDataSource.writeData(entities);
                        List<CoinModel> list = mCryptoMapper.mapEntityToModel(entities);
                        mDataMerger.postValue(list);
                    }
                })
        );

        mDataMerger.addSource(this.mLocalDataSource.getDataStream(), entities ->
                mExecutorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        List<CoinModel> models = mCryptoMapper.mapEntityToModel(entities);
                        mDataMerger.postValue(models);
                    }
                })
        );

        mErrorMerger.addSource(mRemoteDataSource.getErrorStream(), errorString -> {
                    mErrorMerger.setValue(errorString);
                    mExecutorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            List<CryptoCoinEntity> entities = (CryptoRepositoryImpl.this.mLocalDataSource.getAllCoins());
                            mDataMerger.postValue(mCryptoMapper.mapEntityToModel(entities));
                        }
                    });
                }
        );

        mErrorMerger.addSource(mLocalDataSource.getErrorStream(),
                errorString -> mErrorMerger.setValue(errorString)
        );

    }

    public static CryptoRepository create(Context mAppContext) {
        final CryptoMapper mapper = new CryptoMapper();
        final RemoteDataSource remoteDataSource = new RemoteDataSource(mAppContext, mapper);
        final LocalDataSource localDataSource = new LocalDataSource(mAppContext);
        return new CryptoRepositoryImpl(remoteDataSource, localDataSource, mapper);
    }

    @VisibleForTesting
    public static CryptoRepositoryImpl createImpl(Context mAppContext) {
        final CryptoMapper mapper = new CryptoMapper();
        final RemoteDataSource remoteDataSource = new RemoteDataSource(mAppContext, mapper);
        final LocalDataSource localDataSource = new LocalDataSource(mAppContext);
        return new CryptoRepositoryImpl(remoteDataSource, localDataSource, mapper);
    }

    @Override
    public LiveData<List<CoinModel>> getCryptoCoinsData() {
        return mDataMerger;
    }

    @Override
    public LiveData<String> getErrorStream() {
        return mErrorMerger;
    }

    @Override
    public LiveData<Double> getTotalMarketCapStream() {
        return Transformations.map(mDataMerger, input -> {
            double totalMarketCap = 0;
            for (int i = 0; i < input.size(); i++) {
                totalMarketCap += input.get(i).marketCap;
            }
            return totalMarketCap;
        });
    }

    @Override
    public void fetchData() {
        mRemoteDataSource.fetch();
    }

    @VisibleForTesting
    public void insertAllCoins(List<CryptoCoinEntity> entities) {
        mLocalDataSource.writeData(entities);
    }

    @VisibleForTesting
    public void deleteAllCoins() {
        mLocalDataSource.deleteAllCoins();
    }
}
