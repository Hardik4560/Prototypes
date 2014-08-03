
package com.hd.snscoins.ui;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
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

@EActivity(R.layout.activity_coin_country_type)
public class CoinsActivity extends Activity {

    private List<CoinType> coinList;
    SnSCoreSystem mAppContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        coinList = SnsDatabase.session().getCoinTypeDao().loadAll();
        mAppContext = (SnSCoreSystem) getApplicationContext();
    }

    public void onBritishIndiaCoinsClicked(View v) {
        CoinType coinType = coinList.get(0);
        final List<CoinSubType> subTypeList = coinType.getSubTypeList();

        ArrayAdapter<CoinSubType> adapter = new ArrayAdapter<CoinSubType>(this, android.R.layout.simple_list_item_single_choice, subTypeList);

        AlertDialog.Builder dialogAlert = new AlertDialog.Builder(this);
        dialogAlert.setAdapter(adapter, new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                ToastMaker.getInstance().createToast(subTypeList.get(which).toString());
                Intent intent = new Intent(getApplicationContext(), CoinsListActivity_.class);

                mAppContext.setTransientSubType(subTypeList.get(which));
                startActivity(intent);
            }
        });

        dialogAlert.create().show();
    }

    public void onRepublicClicked(View v) {
        CoinType coinType = coinList.get(1);
        final List<CoinSubType> subTypeList = coinType.getSubTypeList();

        ArrayAdapter<CoinSubType> adapter = new ArrayAdapter<CoinSubType>(this, android.R.layout.simple_list_item_single_choice, subTypeList);

        AlertDialog.Builder dialogAlert = new AlertDialog.Builder(this);
        dialogAlert.setAdapter(adapter, new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                ToastMaker.getInstance().createToast(subTypeList.get(which).toString());
                Intent intent = new Intent(getApplicationContext(), CoinsListActivity_.class);
                mAppContext.setTransientSubType(subTypeList.get(which));
                startActivity(intent);
            }
        });

        dialogAlert.create().show();
    }

    public void onUncirculatedSetClicked(View v) {
        CoinType coinType = coinList.get(2);
        final List<CoinSubType> subTypeList = coinType.getSubTypeList();

        ArrayAdapter<CoinSubType> adapter = new ArrayAdapter<CoinSubType>(this, android.R.layout.simple_list_item_single_choice, subTypeList);

        AlertDialog.Builder dialogAlert = new AlertDialog.Builder(this);
        dialogAlert.setAdapter(adapter, new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                ToastMaker.getInstance().createToast(subTypeList.get(which).toString());
                Intent intent = new Intent(getApplicationContext(), CoinsListActivity_.class);
                mAppContext.setTransientSubType(subTypeList.get(which));
                startActivity(intent);
            }
        });

        dialogAlert.create().show();
    }
}
