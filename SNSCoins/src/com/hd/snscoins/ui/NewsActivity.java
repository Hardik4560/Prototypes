
package com.hd.snscoins.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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
import com.hd.snscoins.application.SnSCoreSystem;
import com.hd.snscoins.core.News;
import com.hd.snscoins.core.NewsCategory;
import com.hd.snscoins.db.SnsDatabase;
import com.hd.snscoins.network.NetworkController;
import com.hd.snscoins.utils.ImageUtils;
import com.hd.snscoins.webentities.WeNews;
import com.hd.snscoins.webentities.WeSyncData;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

@EActivity(R.layout.activity_events)
public class NewsActivity extends Activity implements OnRefreshListener {

    public static final String TAG = CoinListActivity.class.getSimpleName();
    private NewsAdapter adapterNews;

    @ViewById(R.id.listView)
    ListView listView;

    @ViewById(R.id.empty_view)
    TextView emptyView;

    @ViewById(R.id.swipeRefreshLayout_listView)
    protected SwipeRefreshLayout mListViewContainer;

    SnSCoreSystem mAppContext;

    NewsCategory newsCategory;

    @AfterViews
    protected void init() {
        mAppContext = (SnSCoreSystem) getApplicationContext();

        newsCategory = mAppContext.getTransientNewsCategory();
        mAppContext.setTransientNewsCategory(null);

        // SwipeRefreshLayout
        mListViewContainer.setOnRefreshListener(this);

        mListViewContainer.setColorScheme(
                android.R.color.holo_blue_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light,
                android.R.color.holo_red_light);

        adapterNews = new NewsAdapter(this);
        listView.setAdapter(adapterNews);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topRowVerticalPosition = (listView == null || listView.getChildCount() == 0) ?
                                                                                                0 : listView.getChildAt(0).getTop();
                mListViewContainer.setEnabled(topRowVerticalPosition >= 0);
            }
        });
        loadData();
    }

    private void loadData() {
        if (newsCategory == null) {
            emptyView.setVisibility(View.VISIBLE);
        }
        else {
            emptyView.setVisibility(View.GONE);

            List<News> newsList = newsCategory.getNewsList();

            if (!newsList.isEmpty()) {
                adapterNews.setDataSource(newsList);
            }
            else {
                mListViewContainer.setRefreshing(true);
                onRefresh();
            }
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

            final News news = getItem(position);

            String coinName = news.getTitle();
            String photoPath = news.getImg_path();

            viewHolder.name.setText(coinName);
            if (photoPath.equals("")) {
                imageLoader.displayImage("http://www.free-pictogram.com/wp-content/uploads/2010/10/8_dollar_0.png", viewHolder.photo, options, new ImageLoadingListener() {

                    @Override
                    public void onLoadingStarted(String imageUri, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        //Save the image in file system.
                        String image = ImageUtils.saveToInternalSorage(getApplicationContext(), loadedImage);
                        news.setImg_path(image);
                        news.update();
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {

                    }
                });
            }
            else {
                imageLoader.displayImage("file://" +  photoPath, viewHolder.photo, options);
            }

            convertView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    mAppContext.setTransientNews(news);

                    Intent intent = new Intent(NewsActivity.this, NewsDetailsActivity_.class);
                    startActivity(intent);
                }
            });
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
            final String GET_NEWS_URL = "http://demo.iccgnews.com/mobile/get_news.php?id=" + newsCategory.getId();

            RequestFuture<JSONObject> futureEvents = RequestFuture.newFuture();
            JsonObjectRequest requestEvents = new JsonObjectRequest(GET_NEWS_URL, new JSONObject(), futureEvents, futureEvents);

            //Set the timeouts
            DefaultRetryPolicy defaultPolicy = new DefaultRetryPolicy(3000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            requestEvents.setRetryPolicy(defaultPolicy);

            NetworkController.getInstance().addToRequestQueue(requestEvents);
            JSONObject responseEvents = futureEvents.get(); // this will block

            Gson gson = new Gson();
            WeSyncData syncDataNews = gson.fromJson(responseEvents.toString(), WeSyncData.class);
            boolean success = saveNewsDataToDb(syncDataNews);

            if (!success) {
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

        newsCategory.resetNewsList();
        final List<News> newsList = newsCategory.getNewsList();

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                mListViewContainer.setRefreshing(false);
                adapterNews.setDataSource(newsList);

                if (newsList.isEmpty()) {
                    emptyView.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private boolean saveNewsDataToDb(WeSyncData syncDataEvents) {
        try {
            List<News> newsList = newsCategory.getNewsList();
            for (Iterator<News> iterator = newsList.iterator(); iterator.hasNext();) {
                News news2 = iterator.next();
                news2.delete();
            }

            // Persist in the database
            // Save the CoinType
            for (int i = 0; i < syncDataEvents.getNews().size(); i++) {
                WeNews weNews = syncDataEvents.getNews().get(i);

                News news = new News(weNews.getId(), weNews.getNews_title()
                        , weNews.getNews_date(), weNews.getNews_time()
                        , weNews.getNews_details(), "", newsCategory.getId());
                SnsDatabase.session().getNewsDao().insert(news);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            // exception handling
            return false;
        }
        return true;
    }
}
