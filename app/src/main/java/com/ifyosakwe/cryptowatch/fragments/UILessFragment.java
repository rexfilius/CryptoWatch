package com.ifyosakwe.cryptowatch.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.ifyosakwe.cryptowatch.viewmodel.CryptoViewModel;

public class UILessFragment extends Fragment {

    public static final String TAG = UILessFragment.class.getSimpleName();
    private CryptoViewModel mViewModel;

    private final Observer<Double> mObserver = totalMarketCap ->
            Log.d(TAG, "onChanged() called with: aDouble = [" + totalMarketCap + "]");

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //mViewModel = ViewModelProviders.of(this).get(CryptoViewModel.class);

        //mViewModel = ViewModelProvider.of(getActivity()).get(CryptoViewModel.class);

        mViewModel = new ViewModelProvider((ViewModelStoreOwner) this).get(CryptoViewModel.class);

        mViewModel.getTotalMarketCap().observe((LifecycleOwner) this, mObserver);
    }

}
