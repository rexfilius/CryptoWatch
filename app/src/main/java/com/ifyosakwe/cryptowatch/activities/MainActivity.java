package com.ifyosakwe.cryptowatch.activities;

import android.os.Bundle;

import com.ifyosakwe.cryptowatch.R;
import com.ifyosakwe.cryptowatch.screens.MainScreen;
import com.ifyosakwe.data.models.CoinModel;

import java.util.List;

public class MainActivity extends LocationActivity implements MainScreen {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void updateData(List<CoinModel> data) {

    }

    @Override
    public void setError(String msg) {

    }
}