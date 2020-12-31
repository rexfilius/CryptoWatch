package com.ifyosakwe.cryptowatch.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ifyosakwe.cryptowatch.R;
import com.ifyosakwe.cryptowatch.fragments.UILessFragment;
import com.ifyosakwe.cryptowatch.recview.Divider;
import com.ifyosakwe.cryptowatch.recview.MyCryptoAdapter;
import com.ifyosakwe.cryptowatch.screens.MainScreen;
import com.ifyosakwe.cryptowatch.viewmodel.CryptoViewModel;
import com.ifyosakwe.data.models.CoinModel;

import java.util.List;

public class MainActivity extends LocationActivity implements MainScreen {

    private static final String TAG = MainActivity.class.getSimpleName();
    private final static int DATA_FETCHING_INTERVAL = 5 * 1000; // 5 seconds
    private long mLastFetchedDataTimeStamp;

    private RecyclerView recView;
    private MyCryptoAdapter mAdapter;
    private CryptoViewModel mViewModel;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private final Observer<List<CoinModel>> dataObserver = coinModels -> updateData(coinModels);
    private final Observer<String> errorObserver = errorMsg -> setError(errorMsg);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindViews();
        mViewModel = new ViewModelProvider(
                (ViewModelStoreOwner) this).get(CryptoViewModel.class);

        mViewModel.getCoinsMarketData().observe(this, dataObserver);
        mViewModel.getErrorUpdates().observe(this, errorObserver);
        mViewModel.fetchData();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        //fragmentTransaction.add(new UILessFragment(), "UILessFragment").commit();

//        getSupportFragmentManager().beginTransaction()
//                .add(new UILessFragment(), "UILessFragment").commit();
    }

    @Override
    public void updateData(List<CoinModel> data) {
        mLastFetchedDataTimeStamp = System.currentTimeMillis();
        mAdapter.setItems(data);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setError(String msg) {
        showErrorToast(msg);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void bindViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        recView = findViewById(R.id.recView);
        mSwipeRefreshLayout = findViewById(R.id.swipeToRefresh);

        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            if (System.currentTimeMillis() - mLastFetchedDataTimeStamp < DATA_FETCHING_INTERVAL) {
                Log.d(TAG, "\tNot fetching from network because interval didn't reach");
                mSwipeRefreshLayout.setRefreshing(false);
                return;
            }
            mViewModel.fetchData();
        });

        mAdapter = new MyCryptoAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recView.setLayoutManager(layoutManager);
        recView.setAdapter(mAdapter);
        recView.addItemDecoration(new Divider(this));

        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> recView.smoothScrollToPosition(0));

        fab = findViewById(R.id.fabExit);
        fab.setOnClickListener(view -> finish());
    }

    private void showErrorToast(String error) {
        Toast.makeText(this, "Error: " + error, Toast.LENGTH_LONG).show();
    }
}