
package com.hd.snscoins.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;
import com.hardy.utils.ToastMaker;
import com.hd.snscoins.R;
import com.hd.snscoins.core.Coin;
import com.hd.snscoins.core.Events;
import com.hd.snscoins.core.News;
import com.hd.snscoins.db.SnsDatabase;
import com.hd.snscoins.network.NetworkController;
import com.hd.snscoins.webentities.WeEvent;
import com.hd.snscoins.webentities.WeNews;
import com.hd.snscoins.webentities.WeSyncData;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

@EActivity(R.layout.activity_events)
public class NewsActivity extends Activity implements OnRefreshListener {

    public static final String TAG = CoinListActivity.class.getSimpleName();
    private NewsAdapter adapterNews;

    @ViewById(R.id.listView)
    ListView listView;

    @ViewById(R.id.swipeRefreshLayout_listView)
    protected SwipeRefreshLayout mListViewContainer;

    @AfterViews
    protected void init() {
        // SwipeRefreshLayout
        mListViewContainer.setOnRefreshListener(this);

        mListViewContainer.setColorScheme(
                android.R.color.holo_blue_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light,
                android.R.color.holo_red_light);

        adapterNews = new NewsAdapter(this);
        listView.setAdapter(adapterNews);

        loadData();
    }

    private void loadData() {
        List<News> eventList = SnsDatabase.session().getNewsDao().loadAll();

        if (!eventList.isEmpty()) {
            adapterNews.setDataSource(eventList);
        }
        else {
            mListViewContainer.setRefreshing(true);
            onRefresh();
        }
    }

    private class NewsAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public ImageLoader imageLoader;
        DisplayImageOptions options;

        List<News> eventsList;

        public NewsAdapter(Context context) {
            super();
            eventsList = new ArrayList<News>();
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
        public News getItem(int position) {
            return eventsList.get(position);
        }

        @Override
        public int getCount() {
            return eventsList.size();
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

            String coinName = getItem(position).getTitle();
            String photoPath = getItem(position).getImg_path();

            viewHolder.name.setText(coinName);
            imageLoader.displayImage("file://" + photoPath, viewHolder.photo, options);

            return convertView;
        }

        public void setDataSource(List<News> news) {
            this.eventsList = news;
            notifyDataSetChanged();
        }
    }

    public static class ViewHolder {
        TextView name;
        ImageView photo;
    }

    @Background
    @Override
    public void onRefresh() {
        try {
            //final String GET_EVENTS_URL = "http://demo.iccgnews.com/mobile/get_news_details.php?id=1";
            final String GET_EVENTS_URL = "http://www.mocky.io/v2/53ce08be40305f9004c130eb";

            RequestFuture<JSONObject> futureEvents = RequestFuture.newFuture();
            JsonObjectRequest requestEvents = new JsonObjectRequest(GET_EVENTS_URL, new JSONObject(), futureEvents, futureEvents);

            //Set the timeouts
            DefaultRetryPolicy defaultPolicy = new DefaultRetryPolicy(3000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            requestEvents.setRetryPolicy(defaultPolicy);

            NetworkController.getInstance().addToRequestQueue(requestEvents);
            JSONObject responseEvents = futureEvents.get(); // this will block

            Gson gson = new Gson();
            WeSyncData syncDataEvents = gson.fromJson(responseEvents.toString(), WeSyncData.class);
            boolean success = saveNewsDataToDb(syncDataEvents);
            
            if(!success){
                ToastMaker.getInstance().createToast("Syncing failed !, try again");
            }
        }
        catch (JsonSyntaxException e) {
            ToastMaker.getInstance().createToast("The data sent from server is invalid, please try again");
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            ToastMaker.getInstance().createToast("Network call was interrupted, please try again");
            e.printStackTrace();
        }
        catch (ExecutionException e) {
            ToastMaker.getInstance().createToast("Couldn't connect to internet, please try again");
            e.printStackTrace();
        }

        final List<News> newsList = SnsDatabase.session().getNewsDao().loadAll();

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                mListViewContainer.setRefreshing(false);
                adapterNews.setDataSource(newsList);
            }
        });

    }

    private boolean saveNewsDataToDb(WeSyncData syncDataEvents) {
        try {
            SnsDatabase.session().deleteAll(News.class);

            SnsDatabase.db().beginTransaction();
            // Persist in the database
            // Save the CoinType
            for (int i = 0; i < syncDataEvents.getNews().size(); i++) {
                WeNews weNews = syncDataEvents.getNews().get(i);

                News news = new News(weNews.getId(), weNews.getNews_title()
                        , weNews.getNews_date(), weNews.getNews_time()
                        , weNews.getNews_details(), "");
                SnsDatabase.session().getNewsDao().insert(news);
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
}
