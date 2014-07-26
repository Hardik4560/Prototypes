
package com.hd.snscoins.ui;

import java.io.File;
import java.io.FileOutputStream;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;
import com.hd.snscoins.R;
import com.hd.snscoins.application.SnSCoreSystem;
import com.hd.snscoins.core.News;
import com.hd.snscoins.utils.ImageUtils;
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

        if (savedInstanceState != null) {

        }
        else {
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

            txtDescription.setText(news.getDetails());

            String photoPath = news.getImg_path();

            if (photoPath.equals("")) {
                imageLoader.displayImage("http://www.free-pictogram.com/wp-content/uploads/2010/10/8_dollar_0.png", imgView, options);
                imageLoader.displayImage("http://www.free-pictogram.com/wp-content/uploads/2010/10/8_dollar_0.png", imgView, options, new ImageLoadingListener() {

                    @Override
                    public void onLoadingStarted(String imageUri, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        // TODO Auto-generated method stub

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
                        // TODO Auto-generated method stub

                    }
                });
            }
            else {
                imageLoader.displayImage("file://" + photoPath, imgView, options);
            }
        }
    }   
}
