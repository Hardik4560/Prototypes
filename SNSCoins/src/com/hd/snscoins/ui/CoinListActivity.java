
package com.hd.snscoins.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.hd.snscoins.R;
import com.hd.snscoins.application.SnSCoreSystem;
import com.hd.snscoins.core.Coin;
import com.hd.snscoins.core.CoinSubType;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

@EActivity(R.layout.activity_coin_list)
public class CoinListActivity extends ListActivity {

    public static final String TAG = CoinListActivity.class.getSimpleName();
    private CoinAdapter adapterCoin;

    CoinSubType subType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {

        }
        else {
            subType = ((SnSCoreSystem) getApplicationContext()).getTransientSubType();
            ((SnSCoreSystem) getApplicationContext()).setTransientSubType(null);
        }
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

            String coinName = getItem(position).getName();
            String photoPath = getItem(position).getIcon_location();

            viewHolder.name.setText(coinName);
            imageLoader.displayImage("file://" + photoPath, viewHolder.photo, options);

            return convertView;
        }

        public void setDataSource(List<Coin> coins) {
            this.coinList = coins;
            notifyDataSetChanged();
        }
    }

    public static class ViewHolder {
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
            coins = subType.getCoinList();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            adapterCoin.setDataSource(coins);
        }
    }
}
