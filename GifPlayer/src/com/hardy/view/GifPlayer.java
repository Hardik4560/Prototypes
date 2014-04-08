/**
 * @author Hardik
 */

package com.hardy.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.hardy.gifdecoder.GifDecoderView;
import com.hardy.gifplayer.R;

/**
 * 
 * @author Hardik
 */
public class GifPlayer extends RelativeLayout {

    private static final String TAG = GifPlayer.class.getSimpleName();
    ProgressBar mProgressBar;
    GifDecoderView mGifDecoderView;

    Context mContext;

    String mFileName;
    int mRepeatCount;

    public GifPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.GifPlayer, 0, 0);

        String fileName = a.getString(R.styleable.GifPlayer_src);
        mRepeatCount = a.getInt(R.styleable.GifPlayer_repeatCount, 1);

        if (fileName != null) {
            setFileName(fileName);
        }
    }

    public GifPlayer(Context context) {
        super(context);

        init(context);
    }

    private void init(Context context) {
        this.mContext = context;

        mProgressBar = new ProgressBar(mContext);

        LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        mProgressBar.setLayoutParams(params);

        //Add the Progress Bar.
        addView(mProgressBar);
    }

    @SuppressLint("NewApi")
    private void setFileName(String fileName) {
        if (fileName != null) {
            try {
                this.mFileName = fileName;

                new LoadGif(fileName).execute();
            }
            catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, fileName + ", Not found in the asset");
            }
        }
    }

    /**
     *Async task for showing gif animation
     * @author Hardik
     */
    class LoadGif extends AsyncTask<Void, Void, Void> implements PropertyChangeListener {

        private InputStream stream;

        public LoadGif(String giftResource) throws IOException {
            stream = getResources().getAssets().open(giftResource);
            //stream = getResources().openRawResource(giftResource);
        }

        protected Void doInBackground(Void... params) {
            Log.d(TAG, "Loading Gif");

            //Initial loading The gif animation.
            mGifDecoderView = new GifDecoderView(getContext(), stream, mRepeatCount);
            return null;
        }

        protected void onPostExecute(Void result) {
            addView(mGifDecoderView);
            Log.d(TAG, "Adding View to the parent");

            //Add the property change listener which will fire the GifDecoderView.ANIMATION_COMPLETED property.
            mGifDecoderView.addPropertyChangeListener(this);
            mGifDecoderView.playGif(stream, false);

            mProgressBar.setVisibility(View.GONE);

            super.onPostExecute(result);
        }

        @Override
        public void propertyChange(PropertyChangeEvent event) {
            //Something when the animation is completed.
        }
    }

    private static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    private static InputStream bitmapToInputStream(Bitmap bitmap) {
        int size = bitmap.getHeight() * bitmap.getRowBytes();
        ByteBuffer buffer = ByteBuffer.allocate(size);
        bitmap.copyPixelsToBuffer(buffer);
        return new ByteArrayInputStream(buffer.array());
    }

    private InputStream drawableToInputStream(Drawable drawable) {
        if (drawable != null) {
            Bitmap bitmap = drawableToBitmap(drawable);
            return bitmapToInputStream(bitmap);
        }
        return null;
    }

}
