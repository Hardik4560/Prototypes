
package com.hd.snscoins.ui;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.googlecode.androidannotations.annotations.EActivity;
import com.hardy.utils.ToastMaker;
import com.hd.snscoins.R;
import com.hd.snscoins.application.SnSCoreSystem;
import com.hd.snscoins.core.CoinSubType;
import com.hd.snscoins.core.CoinType;
import com.hd.snscoins.db.SnsDatabase;

@EActivity(R.layout.activity_coin_currency_type)
public class CurrenciesActivity extends Activity {

    private List<CoinType> coinList;
    SnSCoreSystem mAppContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        coinList = SnsDatabase.session().getCoinTypeDao().loadAll();
        mAppContext = (SnSCoreSystem) getApplicationContext();
    }

    public void onBritishClicked(View v) {
        CoinType coinType = coinList.get(3);
        final List<CoinSubType> subTypeList = coinType.getSubTypeList();

        ArrayAdapter<CoinSubType> adapter = new ArrayAdapter<CoinSubType>(this,
                android.R.layout.simple_list_item_single_choice, subTypeList);

        AlertDialog.Builder dialogAlert = new AlertDialog.Builder(this);
        dialogAlert.setAdapter(adapter, new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(), CoinsListActivity_.class);

                mAppContext.setTransientSubType(subTypeList.get(which));
                startActivity(intent);
            }
        });

        dialogAlert.create().show();
    }

    public void onRepublicClicked(View v) {
        CoinType coinType = coinList.get(4);
        final List<CoinSubType> subTypeList = coinType.getSubTypeList();

        ArrayAdapter<CoinSubType> adapter = new ArrayAdapter<CoinSubType>(this,
                android.R.layout.simple_list_item_single_choice, subTypeList);

        AlertDialog.Builder dialogAlert = new AlertDialog.Builder(this);
        dialogAlert.setAdapter(adapter, new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(), CoinsListActivity_.class);

                mAppContext.setTransientSubType(subTypeList.get(which));
                startActivity(intent);
            }
        });

        dialogAlert.create().show();
    }
}
