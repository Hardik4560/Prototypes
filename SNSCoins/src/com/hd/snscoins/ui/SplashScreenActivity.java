
package com.hd.snscoins.ui;

import java.util.concurrent.ExecutionException;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.google.gson.Gson;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;
import com.hardy.utils.SharedPrefs;
import com.hardy.utils.ToastMaker;
import com.hd.snscoins.R;
import com.hd.snscoins.constants.SnsConstants;
import com.hd.snscoins.core.Coin;
import com.hd.snscoins.core.CoinSubType;
import com.hd.snscoins.core.CoinType;
import com.hd.snscoins.core.NewsCategory;
import com.hd.snscoins.db.SnsDatabase;
import com.hd.snscoins.network.NetworkController;
import com.hd.snscoins.utils.UrlConstants;
import com.hd.snscoins.webentities.WeCategory;
import com.hd.snscoins.webentities.WeNewsCategory;
import com.hd.snscoins.webentities.WeProduct;
import com.hd.snscoins.webentities.WeSubCategory;
import com.hd.snscoins.webentities.WeSyncData;

@EActivity(R.layout.activity_splash)
public class SplashScreenActivity extends Activity {

    @ViewById(R.id.btnRetry)
    Button btnRetry;

    @ViewById(R.id.text)
    TextView txtText;

    @ViewById(R.id.progress)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Sets Application to full screen by removing action bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @AfterViews
    public void init() {
        if ((!(Boolean) SharedPrefs.getInstance().get(SnsConstants.DATA_SYNCED, false))) {
            new SyncCallLoader().execute();
        }
        else {
            progressBar.setVisibility(View.GONE);
            txtText.setVisibility(View.GONE);

            Thread t = new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Intent intent = new Intent(getApplicationContext(), HomeScreenActivity_.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            });
            t.start();
        }
    }

    private class SyncCallLoader extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            btnRetry.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            txtText.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean success = true;

            //Clear any previous data
            SnsDatabase.session().deleteAll(Coin.class);
            SnsDatabase.session().deleteAll(CoinType.class);
            SnsDatabase.session().deleteAll(CoinSubType.class);
            SnsDatabase.session().deleteAll(NewsCategory.class);

            RequestFuture<JSONObject> futureCoins = RequestFuture.newFuture();
            RequestFuture<JSONObject> futureCurrencies = RequestFuture.newFuture();
            RequestFuture<JSONObject> futureNewsCategory = RequestFuture.newFuture();

            JsonObjectRequest requestCoins = new JsonObjectRequest(UrlConstants.GET_COINS_URL, new JSONObject(), futureCoins, futureCoins);
            JsonObjectRequest requestCurrencies = new JsonObjectRequest(UrlConstants.GET_CURRENCIES_URL, new JSONObject(), futureCurrencies, futureCurrencies);
            JsonObjectRequest requestNews = new JsonObjectRequest(UrlConstants.GET_NEWS_CATEGORY_URL, new JSONObject(), futureNewsCategory, futureNewsCategory);

            //Set the timeouts
            DefaultRetryPolicy defaultPolicy = new DefaultRetryPolicy(3000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            requestCoins.setRetryPolicy(defaultPolicy);
            requestCurrencies.setRetryPolicy(defaultPolicy);

            NetworkController.getInstance().addToRequestQueue(requestCurrencies);
            NetworkController.getInstance().addToRequestQueue(requestCoins);
            NetworkController.getInstance().addToRequestQueue(requestNews);

            try {
                JSONObject responseCoins = futureCoins.get(); // this will block
                JSONObject responseCurrencies = futureCurrencies.get(); // this will block
                JSONObject responseNews = futureNewsCategory.get(); // this will block

                // Do the unmarshaling of coins.
                Gson gson = new Gson();

                WeSyncData syncDataCoins = gson.fromJson(responseCoins.toString(), WeSyncData.class);
                WeSyncData syncDataCurrencies = gson.fromJson(responseCurrencies.toString(), WeSyncData.class);
                WeSyncData syncDataNews = gson.fromJson(responseNews.toString(), WeSyncData.class);

                success = saveProductDataToDb(syncDataCoins);
                success = saveProductDataToDb(syncDataCurrencies);
                success = saveNewsDataToDb(syncDataNews);

                SharedPrefs.getInstance().add(SnsConstants.DATA_SYNCED, true);
            }
            catch (InterruptedException e) {
                // exception handling
                success = false;
                e.printStackTrace();
            }
            catch (ExecutionException e) {
                // exception handling
                success = false;
                e.printStackTrace();
            }
            catch (Exception e) {
                // exception handling
                success = false;
                e.printStackTrace();
            }
            return success;
        }

        private boolean saveNewsDataToDb(WeSyncData syncDataNews) {
            try {
                // Persist in the database
                // Save the CoinType
                for (int i = 0; i < syncDataNews.getNews_category().size(); i++) {
                    WeNewsCategory weNewsCategory = syncDataNews.getNews_category().get(i);

                    NewsCategory newsCategory = new NewsCategory(weNewsCategory.getCid(), weNewsCategory.getCategory_title());

                    SnsDatabase.session().getNewsCategoryDao().insert(newsCategory);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                return false;
            }

            return true;
        }

        long productCount = 0;

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

                //Extra sub category for UNC Set.
                CoinSubType uncSetSubType = null;

                // Create some coins
                for (int i = 0; i < syncData.getProduct().size(); i++) {
                    WeProduct weProduct = syncData.getProduct().get(i);

                    String imageUrl = weProduct.getProduct_image();

                    long subCatId = weProduct.getSub_category_id();

                    Coin coin = null;
                    if (subCatId == 0) {

                        //Add an extra subcategory for UNC set.
                        if (uncSetSubType == null) {
                            uncSetSubType = new CoinSubType(
                                    -1l,
                                    "UNC / Proof set",
                                    weProduct.getCategory_id());
                            SnsDatabase.session().getCoinSubTypeDao().insert(uncSetSubType);
                        }

                        coin = new Coin(++productCount, weProduct.getProduct_id(), weProduct.getProduct_title(), imageUrl, null, uncSetSubType.getId(), false, weProduct.getSequence());
                    }
                    else {
                        coin = new Coin(++productCount, weProduct.getProduct_id(), weProduct.getProduct_title(), imageUrl, null, weProduct.getSub_category_id(), false, weProduct.getSequence());
                    }

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
            if (result) {
                Intent intent = new Intent(getApplicationContext(), HomeScreenActivity_.class);
                startActivity(intent);
                finish();
            }
            else {
                ToastMaker.getInstance().createToast("Error syncing data");
                btnRetry.setVisibility(View.VISIBLE);

                progressBar.setVisibility(View.GONE);
                txtText.setVisibility(View.GONE);
            }
        }
    }

    public void onRetry(View v) {
        new SyncCallLoader().execute();
    }

    @Override
    public void onBackPressed() {}
}
