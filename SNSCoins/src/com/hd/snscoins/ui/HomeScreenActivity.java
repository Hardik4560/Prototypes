
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

import com.android.volley.DefaultRetryPolicy;
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
import com.hd.snscoins.core.Events;
import com.hd.snscoins.db.SnsDatabase;
import com.hd.snscoins.network.NetworkController;
import com.hd.snscoins.webentities.WeCategory;
import com.hd.snscoins.webentities.WeEvent;
import com.hd.snscoins.webentities.WeProduct;
import com.hd.snscoins.webentities.WeSubCategory;
import com.hd.snscoins.webentities.WeSyncData;

@EActivity(R.layout.activity_home)
public class HomeScreenActivity extends Activity {
    private static final String TAG = HomeScreenActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((!(Boolean) SharedPrefs.getInstance().get(SnsConstants.DATA_SYNCED, false))) {
            new SyncCallLoader(this).execute();
        }
    }

    public void onCoinsClicked(View v) {
        if (checkDataSynced()) {
            Intent intent = new Intent(this, CoinActivity_.class);
            startActivity(intent);
        }
        else {
            new SyncCallLoader(this).execute();
        }
    }

    public void onEventsClicked(View v) {
        Intent intent = new Intent(this, EventsActivity_.class);
        startActivity(intent);
    }

    public void onNewsClicked(View v) {
        Intent intent = new Intent(this, NewsActivity_.class);
        startActivity(intent);
    }
    

    private boolean checkDataSynced() {
        return (Boolean) SharedPrefs.getInstance().get(SnsConstants.DATA_SYNCED, false);
    }
    
    public void onCurrencyClicked(View v) {
        if (checkDataSynced()) {
            Intent intent = new Intent(this, CurrenciesActivity_.class);
            startActivity(intent);
        }
        else {
            new SyncCallLoader(this).execute();
        }
    }

    private class SyncCallLoader extends AsyncTask<Void, Void, Boolean> {
        private ProgressDialog progressDialog;
        private static final String GET_COINS_URL = "http://demo.iccgnews.com/mobile/get_coins.php";
        private static final String GET_CURRENCIES_URL = "http://demo.iccgnews.com/mobile/get_currencies.php";

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

            //Clear any previous data
            SnsDatabase.session().deleteAll(Coin.class);
            SnsDatabase.session().deleteAll(CoinType.class);
            SnsDatabase.session().deleteAll(CoinSubType.class);

            RequestFuture<JSONObject> futureCoins = RequestFuture.newFuture();
            RequestFuture<JSONObject> futureCurrencies = RequestFuture.newFuture();
            

            JsonObjectRequest requestCoins = new JsonObjectRequest(GET_COINS_URL, new JSONObject(), futureCoins, futureCoins);
            JsonObjectRequest requestCurrencies = new JsonObjectRequest(GET_CURRENCIES_URL, new JSONObject(), futureCurrencies, futureCurrencies);

            //Set the timeouts
            DefaultRetryPolicy defaultPolicy = new DefaultRetryPolicy(3000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            requestCoins.setRetryPolicy(defaultPolicy);
            requestCurrencies.setRetryPolicy(defaultPolicy);

            NetworkController.getInstance().addToRequestQueue(requestCurrencies);
            NetworkController.getInstance().addToRequestQueue(requestCoins);

            try {
                JSONObject responseCoins = futureCoins.get(); // this will block
                JSONObject responseCurrencies = futureCurrencies.get(); // this will block

                // Do the unmarshaling of coins.
                Gson gson = new Gson();
                WeSyncData syncDataCoins = gson.fromJson(responseCoins.toString(), WeSyncData.class);
                WeSyncData syncDataCurrencies = gson.fromJson(responseCurrencies.toString(), WeSyncData.class);
               
                success = saveProductDataToDb(syncDataCoins);
                success = saveProductDataToDb(syncDataCurrencies);

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

        private boolean saveProductDataToDb(WeSyncData syncData) {
            try {
                SnsDatabase.db().beginTransaction();
                // Persist in the database
                // Save the CoinType
                for (int i = 0; i < syncData.getCategory().size(); i++) {
                    WeCategory weCategory = syncData.getCategory().get(i);

                    CoinType category = new CoinType(
                            weCategory.getCategory_id(),
                            weCategory.getCategory_title());
                    SnsDatabase.session().getCoinTypeDao().insert(category);
                }

                // Save the coinSubType in the database.
                for (int i = 0; i < syncData.getSub_category().size(); i++) {
                    WeSubCategory weSubCategory = syncData.getSub_category().get(i);

                    CoinSubType subType = new CoinSubType(
                            weSubCategory.getSub_category_id(),
                            weSubCategory.getSub_category_title(),
                            weSubCategory.getParent_id());
                    SnsDatabase.session().getCoinSubTypeDao().insert(subType);
                }

                // Create some coins
                for (int i = 0; i < syncData.getProducts().size(); i++) {
                    WeProduct weProduct = syncData.getProducts().get(i);

                    Coin coin = new Coin(weProduct.getProduct_id(),
                            weProduct.getProduct_title(), "",
                            weProduct.getSub_category_id());
                    SnsDatabase.session().getCoinDao().insert(coin);
                }

                SnsDatabase.db().setTransactionSuccessful();
            }
            catch (Exception e) {
                e.printStackTrace();
                // exception handling
                return false;
            }
            finally {
                SnsDatabase.db().endTransaction();
            }
            return true;
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
        WeSyncData syncdata(@Path("owner") String owner, @Path("repo") String repo);
    }
}
