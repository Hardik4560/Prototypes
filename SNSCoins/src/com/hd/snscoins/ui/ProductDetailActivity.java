
package com.hd.snscoins.ui;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.google.gson.Gson;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;
import com.hardy.utils.ToastMaker;
import com.hd.snscoins.R;
import com.hd.snscoins.application.SnSCoreSystem;
import com.hd.snscoins.core.Coin;
import com.hd.snscoins.core.Mint;
import com.hd.snscoins.core.Year;
import com.hd.snscoins.db.SnsDatabase;
import com.hd.snscoins.network.NetworkController;
import com.hd.snscoins.utils.ImageUtils;
import com.hd.snscoins.utils.SnsKeyConstants.ImageTypes;
import com.hd.snscoins.utils.UrlConstants;
import com.hd.snscoins.webentities.WeProduct;
import com.hd.snscoins.webentities.WeProduct.WeMint;
import com.hd.snscoins.webentities.WeProduct.WeYear;
import com.hd.snscoins.webentities.WeSyncData;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

@EActivity(R.layout.activity_product_detail)
public class ProductDetailActivity extends Activity {

    @ViewById(R.id.txtTitle)
    protected TextView txtTitle;

    @ViewById(R.id.txtCategory)
    TextView txtCategory;

    @ViewById(R.id.imgLogo)
    protected ImageView imgLogo;

    @ViewById(R.id.lst_years)
    ListView lstYears;

    @ViewById(R.id.btnRetry)
    Button btnRetry;

    @ViewById(R.id.progress)
    ProgressBar progressBar;

    private Coin mCoin;
    private SnSCoreSystem mAppContext;

    public ImageLoader imageLoader;
    DisplayImageOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAppContext = ((SnSCoreSystem) getApplicationContext());

        mCoin = mAppContext.getTransientProduct();
        mAppContext.setTransientProduct(null);

        //Initialize lazy loading api.
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.img_not_available)
                .showImageForEmptyUri(R.drawable.img_not_available)
                .showImageOnFail(R.drawable.img_not_available)
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    @AfterViews
    protected void init() {
        if (mCoin != null) {
            txtTitle.setText(mCoin.getName());
            txtCategory.setText(mCoin.getCoinSubType().getType());

            String photoPath = mCoin.getImage_path();
            if (photoPath.equals("")) {
                String url = mCoin.getImage_url();
                imageLoader.displayImage(url, imgLogo, options, new ImageLoadingListener() {

                    @Override
                    public void onLoadingStarted(String imageUri, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        //Save the image in file system.
                        // String image = ImageUtils.saveToInternalSorage(getApplicationContext(), loadedImage);
                        mCoin.setImage_path(imageUri);
                        SnsDatabase.session().update(mCoin);
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {

                    }
                });
            }
            else {
                imageLoader.displayImage("file://" + photoPath, imgLogo, options);
            }

            if (mCoin.getYearList().isEmpty()) {
                //Download from server.
                GetDetailsLoader getDetailsLoader = new GetDetailsLoader();
                getDetailsLoader.execute();
            }
            else {
                //Set the adapter for the years.
                lstYears.setAdapter(new YearAdapter(mCoin));
            }
        }
    }

    private class YearAdapter extends BaseAdapter {

        List<Year> yearList;

        public YearAdapter(Coin coin) {
            yearList = coin.getYearList();
        }

        @Override
        public int getCount() {
            return yearList.size();
        }

        @Override
        public Year getItem(int position) {

            return yearList.get(position);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int arg0, View convertView, ViewGroup arg2) {
            ViewHolder viewHolder;

            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.e_lst_year_detail, null);

                viewHolder = new ViewHolder();
                viewHolder.txtYear = (TextView) convertView.findViewById(R.id.txt_year);
                viewHolder.mintLayout = (LinearLayout) convertView.findViewById(R.id.lnr_mint);

                convertView.setTag(viewHolder);
            }
            else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            Year year = getItem(arg0);
            viewHolder.txtYear.setText(year.getTitle());
            List<Mint> mintList = year.getMintList();

            //Add the mints.
            for (Iterator<Mint> iterator = mintList.iterator(); iterator.hasNext();) {
                Mint mint = iterator.next();

                RelativeLayout relativeLayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.e_lst_mint_chkbox, null);
                viewHolder.mintLayout.addView(relativeLayout);

                TextView txt_mint = (TextView) relativeLayout.findViewById(R.id.txt_mint);
                txt_mint.setText(mint.getTitle());

                CheckBox chkBox = (CheckBox) relativeLayout.findViewById(R.id.chkbox);
                if (mint.getRare() == 1) {
                    chkBox.setText("*");
                }
                else {
                    chkBox.setText("");
                }
            }

            return convertView;
        }

        private class ViewHolder {
            TextView txtYear;
            LinearLayout mintLayout;
        }
    }

    private class GetDetailsLoader extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);
            btnRetry.setVisibility(View.GONE);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean success = true;

            //Clear any previous data

            RequestFuture<JSONObject> futureYears = RequestFuture.newFuture();

            JsonObjectRequest requestYears = new JsonObjectRequest(UrlConstants.GET_PRODUCT_DETAIL + mCoin.getId(), new JSONObject(), futureYears, futureYears);

            //Set the timeouts
            DefaultRetryPolicy defaultPolicy = new DefaultRetryPolicy(3000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            requestYears.setRetryPolicy(defaultPolicy);
            requestYears.setRetryPolicy(defaultPolicy);

            NetworkController.getInstance().addToRequestQueue(requestYears);

            try {
                JSONObject responseYears = futureYears.get(); // this will block

                // Do the unmarshaling of coins.
                Gson gson = new Gson();
                WeSyncData syncDataCoins = gson.fromJson(responseYears.toString(), WeSyncData.class);

                success = saveProductDataToDb(syncDataCoins);
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
            return success;
        }

        private boolean saveProductDataToDb(WeSyncData syncData) {
            //Delete the previous data for this coins if any.
            List<Year> tempYearList = mCoin.getYearList();
            for (Iterator<Year> iterator = tempYearList.iterator(); iterator.hasNext();) {
                Year tempYear = iterator.next();

                List<Mint> tempMintList = tempYear.getMintList();
                for (Iterator<Mint> iMints = tempMintList.iterator(); iMints.hasNext();) {
                    Mint mint = iMints.next();
                    mint.delete();
                }
                tempYear.delete();
            }

            //INSERT THE NEW DATA IN THE LIST >>>>>>
            List<WeProduct> weProducts = syncData.getProduct();

            if (weProducts.isEmpty()) {
                //No years with this coins
                return true;
            }

            WeProduct weProduct = weProducts.get(0);
            List<WeYear> years = weProduct.getProduct_mint();

            // Create some coins
            SnsDatabase.db().beginTransaction();

            try {
                for (int i = 0; i < years.size(); i++) {
                    WeYear weYear = years.get(i);

                    Year year = new Year();
                    year.setId_coin(mCoin.getId());
                    year.setTitle(weYear.getYear());

                    SnsDatabase.session().getYearDao().insert(year);

                    List<WeMint> weMintList = weYear.getMint_title();
                    for (int jMint = 0; jMint < weMintList.size(); jMint++) {
                        WeMint weMint = weMintList.get(jMint);

                        Mint mint = new Mint();
                        mint.setTitle(weMint.getTitle());
                        mint.setRare(weMint.isRare());
                        mint.setId_year(year.getId());

                        SnsDatabase.session().getMintDao().insert(mint);
                    }
                }

                mCoin.resetYearList();
                SnsDatabase.db().setTransactionSuccessful();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                SnsDatabase.db().endTransaction();
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            progressBar.setVisibility(View.GONE);

            if (result) {
                //Set the adapter for the years.
                lstYears.setAdapter(new YearAdapter(mCoin));
            }
            else {
                ToastMaker.getInstance().createToast("Error syncing data");
                btnRetry.setVisibility(View.VISIBLE);
            }
        }
    }

    public void onRetry(View v) {
        //Download from server.
        GetDetailsLoader getDetailsLoader = new GetDetailsLoader();
        getDetailsLoader.execute();
    }
}
