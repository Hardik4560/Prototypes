
package com.hd.snscoins.ui;

import java.util.concurrent.ExecutionException;

import org.json.JSONObject;

import retrofit.http.GET;
import retrofit.http.Path;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.google.gson.Gson;
import com.googlecode.androidannotations.annotations.EActivity;
import com.hardy.utils.SharedPrefs;
import com.hardy.utils.ToastMaker;
import com.hd.snscoins.R;
import com.hd.snscoins.constants.SnsConstants;
import com.hd.snscoins.core.Coin;
import com.hd.snscoins.core.CoinSubType;
import com.hd.snscoins.core.CoinType;
import com.hd.snscoins.db.SnsDatabase;
import com.hd.snscoins.network.NetworkController;
import com.hd.snscoins.webentities.WeCategory;
import com.hd.snscoins.webentities.WeProduct;
import com.hd.snscoins.webentities.WeSubCategory;
import com.hd.snscoins.webentities.WeSyncData;

@EActivity(R.layout.activity_home)
public class HomeScreenActivity extends Activity {
    private static final String TAG = HomeScreenActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((!(Boolean) SharedPrefs.getInstance().get(SnsConstants.TYPE, false))) {
            new SyncCallLoader(this).execute();
        }
    }

    public void onCoinsClicked(View v) {
        Intent intent = new Intent(this, CoinTypeActivity_.class);
        startActivity(intent);
    }

    public void onCurrencyClicked(View v) {
        Intent intent = new Intent(this, CoinCurrencyTypeActivity_.class);
        startActivity(intent);
    }

    private class SyncCallLoader extends AsyncTask<Void, Void, Boolean> {
        private ProgressDialog progressDialog;
        private static final String API_URL = "http://www.mocky.io/v2/53c6ce3d1fe368eb184a6117";

        public SyncCallLoader(Context context) {
            progressDialog = new ProgressDialog(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Downloading data from server...");
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean success = true;

            RequestFuture<JSONObject> future = RequestFuture.newFuture();
            JsonObjectRequest request = new JsonObjectRequest(API_URL, new JSONObject(), future, future);
            NetworkController.getInstance().addToRequestQueue(request);

            try {
                JSONObject response = future.get(); // this will block

                //Do the unmarshaling.
                Gson gson = new Gson();
                WeSyncData syncData = gson.fromJson(response.toString(), WeSyncData.class);

                //Persist in the database
                //Save the CoinType
                for (int i = 0; i < syncData.getCategory().size(); i++) {
                    WeCategory weCategory = syncData.getCategory().get(i);

                    CoinType coinType = new CoinType(weCategory.getId(), weCategory.getProduct_title());
                    SnsDatabase.session().getCoinTypeDao().insert(coinType);
                }

                //Save the coinSubType in the database.
                for (int i = 0; i < syncData.getSub_category().size(); i++) {
                    WeSubCategory weSubCategory = syncData.getSub_category().get(i);

                    CoinSubType coinSubType = new CoinSubType(weSubCategory.getId(), weSubCategory.getProduct_title(), weSubCategory.getP_id());
                    SnsDatabase.session().getCoinSubTypeDao().insert(coinSubType);
                }

                //Create some coins
                for (int i = 0; i < syncData.getProducts().size(); i++) {
                    WeProduct weProduct = syncData.getProducts().get(i);

                    Coin coin = new Coin(weProduct.getId(), weProduct.getProduct_title(), "", weProduct.getId_sub_category());
                    SnsDatabase.session().getCoinDao().insert(coin);
                }

                SharedPrefs.getInstance().add(SnsConstants.DATA_SYNCED, true);
            }
            catch (InterruptedException e) {
                // exception handling
                success = false;
            }
            catch (ExecutionException e) {
                // exception handling
                success = false;
            }
            return success;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            if (result) {
                ToastMaker.getInstance().createToast("Synced Successfully");
            }
            else {
                ToastMaker.getInstance().createToast("Error syncing data");
            }
        }
    }

    public interface SyncDataInterface {
        @GET("/repos/{owner}/{repo}/contributors")
        WeSyncData syncdata(
                @Path("owner") String owner,
                @Path("repo") String repo
                );
    }
}
