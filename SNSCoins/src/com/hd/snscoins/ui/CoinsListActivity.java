
package com.hd.snscoins.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.hd.snscoins.R;
import com.hd.snscoins.application.SnSCoreSystem;
import com.hd.snscoins.core.Coin;
import com.hd.snscoins.core.CoinDao.Properties;
import com.hd.snscoins.core.CoinSubType;
import com.hd.snscoins.db.SnsDatabase;
import com.hd.snscoins.utils.ImageUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import de.greenrobot.dao.Property;
import de.greenrobot.dao.query.QueryBuilder;

@EActivity(R.layout.activity_coin_list)
public class CoinsListActivity extends ListActivity {

    public static final String TAG = CoinsListActivity.class.getSimpleName();
    private CoinAdapter adapterCoin;

    CoinSubType subType;

    private SnSCoreSystem mAppContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAppContext = ((SnSCoreSystem) getApplicationContext());
        subType = mAppContext.getTransientSubType();
        mAppContext.setTransientSubType(null);

        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @AfterViews
    protected void init() {
        adapterCoin = new CoinAdapter(this);
        setListAdapter(adapterCoin);

        new LoadData(this).execute();
    }

    private class CoinAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public ImageLoader imageLoader;
        DisplayImageOptions options;

        List<Coin> coinList;

        public CoinAdapter(Context context) {
            super();
            coinList = new ArrayList<Coin>();
            mInflater = LayoutInflater.from(context);

            //Initialize lazy loading api.
            ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(context));
            imageLoader = ImageLoader.getInstance();
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.img_not_available)
                    .showImageForEmptyUri(R.drawable.img_not_available)
                    .showImageOnFail(R.drawable.img_not_available)
                    .cacheInMemory(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
        }

        @Override
        public Coin getItem(int position) {
            return coinList.get(position);
        }

        @Override
        public int getCount() {
            return coinList.size();
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup arg2) {

            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.e_lst_coin, null);

                viewHolder = new ViewHolder();
                viewHolder.name = (TextView) convertView.findViewById(R.id.txt_name);
                viewHolder.photo = (ImageView) convertView.findViewById(R.id.img_view);

                convertView.setTag(viewHolder);
            }
            else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            final Coin coin = getItem(position);
            String coinName = coin.getName();
            String photoPath = coin.getImage_path();

            viewHolder.name.setText(coinName);

            if (photoPath == null || photoPath.equals("")) {
                String url = coin.getImage_url();
                imageLoader.displayImage(url, viewHolder.photo, options, new ImageLoadingListener() {

                    @Override
                    public void onLoadingStarted(String imageUri, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        //Save the image in file system.
                        coin.setImage_path(ImageUtils.saveToInternalSorage(mAppContext, loadedImage));
                        coin.update();
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {

                    }
                });
            }
            else {
                imageLoader.displayImage("file://" + photoPath, viewHolder.photo, options);
            }

            //Set the onclick listener
            convertView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    mAppContext.setTransientProduct(coin);
                    Intent intent = new Intent(getApplicationContext(), CoinsDetailActivity_.class);
                    startActivity(intent);
                }
            });

            return convertView;
        }

        public void setDataSource(List<Coin> coins) {
            this.coinList = coins;
            notifyDataSetChanged();
        }
    }

    private static class ViewHolder {
        TextView name;
        ImageView photo;
    }

    public final class LoadData extends AsyncTask<Void, Void, Void> {
        List<Coin> coins = new ArrayList<Coin>();
        ProgressDialog progressDialog;

        public LoadData(Context context) {
            super();
            progressDialog = new ProgressDialog(context);
            coins = new ArrayList<Coin>();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            /*Cursor cursor = null;
            //Get the selected subType.
            String[] projectionSubType = new String[] { "_id", "TYPE" };
            String whereSubType = "TYPE = " + subType;
            Cursor cursorSubType = SnsDatabase.db().query(SnsDatabase.TABLE_COIN_SUB_TYPE, projectionSubType, whereSubType, null, null, null, null);

            //Get the selected subType id and use it to get all the coins.
            String[] projection = new String[] { "_id", "NAME", "ICON_LOCATION" };
            String where = "id_sub_type = " + cursorSubType.getLong(cursorSubType.getColumnIndex("_id"));

            cursor = SnsDatabase.db().query(SnsDatabase.TABLE_COIN, projection, where, null, null, null, null);

            if (cursor != null) {
                Logger.d(TAG, "Cursor count = " + cursor.getCount());
            }

            if (cursor.moveToFirst()) {
                do {
                    Long id = cursor.getLong(cursor.getColumnIndex("_id"));
                    String name = cursor.getString(cursor.getColumnIndex("NAME"));
                    String path = cursor.getString(cursor.getColumnIndex("ICON_LOCATION"));

                    coins.add(new Coin(id, name, path, 1));

                } while (cursor.moveToNext());
            }
            */
            coins = SnsDatabase.session().getCoinDao().queryBuilder()
                    .where(Properties.Id_sub_type.eq(subType.getId()))
                    .orderAsc(Properties.Sequence)
                    .list();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            adapterCoin.setDataSource(coins);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
