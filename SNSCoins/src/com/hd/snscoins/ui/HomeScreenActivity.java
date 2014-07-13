
package com.hd.snscoins.ui;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.googlecode.androidannotations.annotations.EActivity;
import com.hd.snscoins.R;

@EActivity(R.layout.activity_home)
public class HomeScreenActivity extends Activity {

    public void onCoinsClicked(View v) {
        Intent intent = new Intent(this, CoinTypeActivity_.class);
        startActivity(intent);
    }

    public void onCurrencyClicked(View v) {
        Intent intent = new Intent(this, CoinCurrencyTypeActivity_.class);
        startActivity(intent);
    }
}
