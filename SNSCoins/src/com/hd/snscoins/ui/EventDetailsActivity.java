
package com.hd.snscoins.ui;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;
import com.hd.snscoins.R;
import com.hd.snscoins.application.SnSCoreSystem;
import com.hd.snscoins.core.Events;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

@EActivity(R.layout.activity_event_detail)
public class EventDetailsActivity extends Activity {

    @ViewById(R.id.txtTitle)
    protected TextView txtTitle;

    @ViewById(R.id.txtDate)
    protected TextView txtStartDate;

    @ViewById(R.id.txtVenue)
    protected TextView txtVenue;

    @ViewById(R.id.txtDescription)
    protected TextView txtDescription;

    @ViewById(R.id.img_view)
    protected ImageView imgView;

    private Events event;

    public ImageLoader imageLoader;
    DisplayImageOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {

        }
        else {
            event = ((SnSCoreSystem) getApplicationContext()).getTransientEvent();
            ((SnSCoreSystem) getApplicationContext()).setTransientEvent(event);

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
    }

    @AfterViews
    protected void init() {
        if (event != null) {
            txtTitle.setText(event.getTitle());
            txtStartDate.setText(event.getStart_date());
            txtVenue.setText(event.getVenue());
            txtDescription.setText(Html.fromHtml(event.getDetails()));

            String photoPath = event.getImg_path();

            imageLoader.displayImage("file://" + photoPath, imgView, options);
        }
    }
}
