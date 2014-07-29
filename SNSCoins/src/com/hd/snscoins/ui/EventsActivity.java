
package com.hd.snscoins.ui;

import java.util.ArrayList;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
import com.hd.snscoins.core.Events;
import com.hd.snscoins.db.SnsDatabase;
import com.hd.snscoins.network.NetworkController;
import com.hd.snscoins.utils.UrlConstants;
import com.hd.snscoins.webentities.WeEvent;
import com.hd.snscoins.webentities.WeSyncData;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

@EActivity(R.layout.activity_events)
public class EventsActivity extends Activity implements OnRefreshListener {

    public static final String TAG = CoinListActivity.class.getSimpleName();
    private EventAdapter adapterEvents;

    @ViewById(R.id.listView)
    ListView listView;

    SnSCoreSystem mAppContext;

    @ViewById(R.id.swipeRefreshLayout_listView)
    protected SwipeRefreshLayout mListViewContainer;

    @AfterViews
    protected void init() {
        mAppContext = (SnSCoreSystem) getApplicationContext();

        // SwipeRefreshLayout
        mListViewContainer.setOnRefreshListener(this);

        mListViewContainer.setColorScheme(
                android.R.color.holo_blue_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light,
                android.R.color.holo_red_light);

        adapterEvents = new EventAdapter(this);
        listView.setAdapter(adapterEvents);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topRowVerticalPosition =
                                             (listView == null || listView.getChildCount() == 0) ?
                                                                                                0 : listView.getChildAt(0).getTop();
                mListViewContainer.setEnabled(topRowVerticalPosition >= 0);
            }
        });

        loadData();
    }

    private void loadData() {
        List<Events> eventList = SnsDatabase.session().getEventsDao().loadAll();

        if (!eventList.isEmpty()) {
            adapterEvents.setDataSource(eventList);
        }
        else {
            mListViewContainer.setRefreshing(true);
            onRefresh();
        }
    }

    private class EventAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public ImageLoader imageLoader;
        DisplayImageOptions options;

        List<Events> eventsList;

        public EventAdapter(Context context) {
            super();
            eventsList = new ArrayList<Events>();
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
        public Events getItem(int position) {
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

            final Events event = getItem(position);
            String coinName = event.getTitle();
            String photoPath = event.getImage_path();

            viewHolder.name.setText(coinName);
            imageLoader.displayImage("file://" + photoPath, viewHolder.photo, options);

            convertView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    mAppContext.setTransientEvent(event);

                    Intent intent = new Intent(EventsActivity.this, EventDetailsActivity_.class);
                    startActivity(intent);
                }
            });
            return convertView;
        }

        public void setDataSource(List<Events> events) {
            this.eventsList = events;
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

            RequestFuture<JSONObject> futureEvents = RequestFuture.newFuture();
            JsonObjectRequest requestEvents = new JsonObjectRequest(UrlConstants.GET_EVENTS_URL, new JSONObject(), futureEvents, futureEvents);

            //Set the timeouts
            DefaultRetryPolicy defaultPolicy = new DefaultRetryPolicy(3000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            requestEvents.setRetryPolicy(defaultPolicy);

            NetworkController.getInstance().addToRequestQueue(requestEvents);
            JSONObject responseEvents = futureEvents.get(); // this will block

            Gson gson = new Gson();
            WeSyncData syncDataEvents = gson.fromJson(responseEvents.toString(), WeSyncData.class);
            boolean success = saveEventsDataToDb(syncDataEvents);
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

        final List<Events> eventList = SnsDatabase.session().getEventsDao().loadAll();

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                mListViewContainer.setRefreshing(false);
                adapterEvents.setDataSource(eventList);
            }
        });

    }

    private boolean saveEventsDataToDb(WeSyncData syncDataEvents) {
        try {
            SnsDatabase.session().deleteAll(Events.class);

            SnsDatabase.db().beginTransaction();
            // Persist in the database
            // Save the CoinType
            for (int i = 0; i < syncDataEvents.getEvent().size(); i++) {
                WeEvent weEvent = syncDataEvents.getEvent().get(i);

                Events event = new Events(
                        weEvent.getId(), weEvent.getEvent_title()
                        , weEvent.getEvent_start_date(), weEvent.getEvent_start_time()
                        , weEvent.getEvent_end_date(), weEvent.getEvent_end_time()
                        , weEvent.getEvent_venue(), weEvent.getEvent_details(), "", weEvent.getImage_url()
                               );
                SnsDatabase.session().getEventsDao().insert(event);
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
