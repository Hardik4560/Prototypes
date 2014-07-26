
package com.hardy.gifplayer;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.InputStream;
import java.lang.ref.WeakReference;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hardy.gifdecoder.GifDecoderView;
import com.hardy.view.GifPlayer;

public class GifPlayerActivity extends Activity implements PropertyChangeListener {

    private static final String TAG = GifPlayerActivity.class.getSimpleName();

    Context mActivityContext;

    //The Reference for decoder;
    private static GifDecoderView gdView;

    private static Dialog gifDialog;
    private static LinearLayout giftLayout;

    private GifPlayer gifPlayerView;

    Drawable mDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnAnimate = (Button) findViewById(R.id.btnAnimation);
        btnAnimate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                new ShowGifAnimation(GifPlayerActivity.this, R.raw.kiss).execute();
            }
        });

        Button btnToggle = (Button) findViewById(R.id.btnToggle);
        btnToggle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                gifPlayerView.setSrc("kiss.gif");
                gifPlayerView.play(-1);
            }
        });

        Button btnPause = (Button) findViewById(R.id.btnPause);
        btnPause.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                gifPlayerView.pause();
            }
        });

        Button btnResume = (Button) findViewById(R.id.btnResume);
        btnResume.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                gifPlayerView.resume();
            }
        });

        mDrawable = getResources().getDrawable(R.drawable.ic_launcher);
        gifPlayerView = (GifPlayer) findViewById(R.id.gifPlayer1);
        mActivityContext = this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     *Async task for showing gif animation
     * @author Hardik
     */
    static class ShowGifAnimation extends AsyncTask<Void, Void, Void> {

        private WeakReference<GifPlayerActivity> mContext;
        private Dialog dialogProgress;
        private InputStream stream;

        public ShowGifAnimation(GifPlayerActivity mainActivity, int giftResource) {

            mContext = new WeakReference<GifPlayerActivity>(mainActivity);

            dialogProgress = new Dialog(mContext.get(), android.R.style.Theme_Translucent);
            dialogProgress.setCancelable(false);
            dialogProgress.getWindow().getAttributes().dimAmount = 0.7f;
            dialogProgress.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            dialogProgress.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

            TextView txt = new TextView(mContext.get().mActivityContext);
            txt.setText("   Preparing animation..");

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            lp.gravity = Gravity.CENTER;

            LinearLayout dummyLayout = new LinearLayout(mContext.get());
            dummyLayout.setGravity(Gravity.CENTER);
            ProgressBar pb = new ProgressBar(mContext.get());
            pb.setScrollBarStyle(ProgressDialog.STYLE_HORIZONTAL);
            dummyLayout.addView(pb);
            dummyLayout.addView(txt);

            dialogProgress.addContentView(dummyLayout, lp);

            // stream = getAssets().open("newgift.gif");
            stream = mContext.get().getResources().openRawResource(giftResource);
        }

        protected void onPreExecute() {

            super.onPreExecute();
            dialogProgress.show();
        }

        protected Void doInBackground(Void... params) {
            //Initial loading The gif animation.
            gdView = new GifDecoderView(mContext.get().mActivityContext, stream, 5);
            return null;
        }

        protected void onPostExecute(Void result) {

            dialogProgress.dismiss();

            //Add the property change listener which will fire the GifDecoderView.ANIMATION_COMPLETED property.
            gdView.addPropertyChangeListener(mContext.get());

            if (gdView.getParent() != null)
                ((LinearLayout) gdView.getParent()).removeAllViews();

            giftLayout = new LinearLayout(mContext.get().mActivityContext);
            giftLayout.setGravity(Gravity.CENTER);
            giftLayout.addView(gdView);

            gifDialog = new Dialog(mContext.get(), android.R.style.Theme_Translucent);

            gifDialog.setCancelable(false);
            gifDialog.getWindow().getAttributes().dimAmount = 0.7f;
            gifDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            gifDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            gifDialog.addContentView(giftLayout, lp);
            gifDialog.show();
            gdView.playGif();

            super.onPostExecute(result);
        }
    }

    /**
     * Handle the post Gif animation things. Would be fired when the animation
     * is completed i.e., from the firePropertyChanged mehtod
     * @author Hardik
     */
    private void postGifAnimation() {

        if (gifDialog != null && gifDialog.isShowing()) {
            //TODO: Do something when gif animation is completed.
            gifDialog.dismiss();
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        final String propertyName = event.getPropertyName();

        if (propertyName.equals(GifDecoderView.ANIMATION_COMPLETED)) {
            gdView.removePropertyChangeListener(this);
            postGifAnimation();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        gifPlayerView.recycle();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gifPlayerView.recycle();
    }
}
