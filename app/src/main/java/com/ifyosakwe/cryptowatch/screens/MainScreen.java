package com.ifyosakwe.cryptowatch.screens;

import com.ifyosakwe.data.models.CoinModel;

import java.util.List;

public interface MainScreen {

    void updateData(List<CoinModel> data);
    void setError(String msg);

}
