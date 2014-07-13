
package com.hd.snscoins.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;

import com.googlecode.androidannotations.annotations.EActivity;
import com.hardy.utils.ToastMaker;
import com.hd.snscoins.R;

@EActivity(R.layout.activity_coin_country_type)
public class CoinTypeActivity extends Activity {
    public void onBritishIndiaCoinsClicked(View v) {

        final String[] options = new String[] { "Copper/Nickle", "Silver" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, options);

        AlertDialog.Builder dialogAlert = new AlertDialog.Builder(this);
        dialogAlert.setAdapter(adapter, new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                ToastMaker.getInstance().createToast(options[which]);
                Intent intent = new Intent(getApplicationContext(), CoinListActivity_.class);
                startActivity(intent);
            }
        });

        dialogAlert.create().show();
    }

    public void onRepublicClicked(View v) {
        final String[] options = new String[] { "Commoditive", "Definative" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, options);

        AlertDialog.Builder dialogAlert = new AlertDialog.Builder(this);
        dialogAlert.setAdapter(adapter, new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                ToastMaker.getInstance().createToast(options[which]);
            }
        });

        dialogAlert.create().show();
    }

    public void onUncirculatedSetClicked(View v) {
        final String[] options = new String[] { "Proof Sets", "UNC Sets" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, options);

        AlertDialog.Builder dialogAlert = new AlertDialog.Builder(this);
        dialogAlert.setAdapter(adapter, new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                ToastMaker.getInstance().createToast(options[which]);
            }
        });

        dialogAlert.create().show();
    }
}
