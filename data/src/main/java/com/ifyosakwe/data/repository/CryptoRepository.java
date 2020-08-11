package com.ifyosakwe.data.repository;

import androidx.lifecycle.LiveData;
import com.ifyosakwe.data.models.CoinModel;
import java.util.List;

public interface CryptoRepository {

    LiveData<List<CoinModel>> getCryptoCoinsData();
    LiveData<String> getErrorStream();
    LiveData<Double> getTotalMarketCapStream();
    void fetchData();

}
