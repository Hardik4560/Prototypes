
package com.hd.snscoins.ui;

import android.app.Activity;
import android.content.Intent;

import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.hd.snscoins.R;

@EActivity(R.layout.activity_home)
public class HomeScreenActivity extends Activity {

    @Click(R.id.btnCoins)
    protected void onCoinsClick() {
        Intent intentCountries = new Intent(this, CountryListActivity_.class);
        startActivity(intentCountries);
    }
}
