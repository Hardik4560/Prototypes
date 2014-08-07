
package com.hd.snscoins.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;
import com.hd.snscoins.R;
import com.hd.snscoins.application.SnSCoreSystem;
import com.hd.snscoins.core.News;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

@EActivity(R.layout.activity_event_detail)
public class NewsDetailsActivity extends Activity {

    @ViewById(R.id.txtTitle)
    protected TextView txtTitle;

    @ViewById(R.id.txtStartDate)
    protected TextView txtStartDate;

    @ViewById(R.id.txtStartTime)
    protected TextView txtStartTime;

    @ViewById(R.id.txtEndLabel)
    protected TextView txtEnd;

    @ViewById(R.id.txtEndDate)
    protected TextView txtEndDate;

    @ViewById(R.id.txtEndTime)
    protected TextView txtEndTime;

    @ViewById(R.id.txtVenue)
    protected TextView txtVenue;

    @ViewById(R.id.txtVenueLabel)
    protected TextView txtVenueLabel;

    @ViewById(R.id.txtDescription)
    protected TextView txtDescription;

    @ViewById(R.id.img_view)
    protected ImageView imgView;

    private News news;

    public ImageLoader imageLoader;
    DisplayImageOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        news = ((SnSCoreSystem) getApplicationContext()).getTransientNews();
        ((SnSCoreSystem) getApplicationContext()).setTransientNews(null);

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
        if (news != null) {
            txtTitle.setText(news.getTitle());
            txtStartDate.setText(news.getDate());
            txtStartTime.setText(news.getTime());

            txtEnd.setVisibility(View.GONE);
            txtEndDate.setVisibility(View.GONE);
            txtEndTime.setVisibility(View.GONE);
            txtVenue.setVisibility(View.GONE);
            txtVenueLabel.setVisibility(View.GONE);

            if (!TextUtils.isEmpty(news.getDetails())) {
                txtDescription.setText(Html.fromHtml(Html.fromHtml(news.getDetails()).toString()));
            }

            String photoPath = news.getImage_path();

            if (TextUtils.isEmpty(photoPath)) {
                String url = news.getImage_url();
                imageLoader.displayImage(url, imgView, options, new ImageLoadingListener() {

                    @Override
                    public void onLoadingStarted(String imageUri, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        //Save the image in file system.
                        news.setImage_path(imageUri);
                        news.update();
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {

                    }
                });
            }
            else {
                imageLoader.displayImage("file://" + photoPath, imgView, options);
            }
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
