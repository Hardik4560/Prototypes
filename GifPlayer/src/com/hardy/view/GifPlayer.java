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

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.hardy.gifdecoder.GifDecoderView;
import com.hardy.gifplayer.R;
import com.hardy.log.Logger;

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

    LoadGif mLoadGif;

    public GifPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.GifPlayer, 0, 0);

        String fileName = a.getString(R.styleable.GifPlayer_src);
        mRepeatCount = a.getInt(R.styleable.GifPlayer_repeatCount, 0);

        if (fileName != null) {
            //Set the file name and start loading the gif animation.
            setFileName(fileName);
            play();
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
        mProgressBar.setVisibility(View.GONE);

        //Add the Progress Bar.
        addView(mProgressBar);
    }

    private void setFileName(String fileName) {
        this.mFileName = fileName;
    }

    public void setSrc(String srcName) {
        setFileName(srcName);
    }

    /**
     * Play the gif animation with the specified name. The file should be present in the asset folder.
     * @author Hardik
     * @since 1.0
     */
    public void play() {
        play(mRepeatCount);
    }

    /**
     * Play the gif animation with the specified name. The file should be present in the asset folder.
     * @param repeatCount - The number of time the gif should be played, -1 would make it play forever.
     * @author Hardik
     * @since 1.0
     */
    public void play(int repeatCount) {
        if (mFileName != null) {
            try {
                if (mLoadGif != null && mLoadGif.getStatus() == Status.RUNNING) {
                    mLoadGif.cancel(true);
                }

                Log.d(TAG, "Playing Gif");
                mLoadGif = new LoadGif(mFileName, repeatCount);
                mLoadGif.execute();

            }
            catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, mFileName + ", Not found in the asset, Have you miss adding the file in the asset ?");
            }
        }
        else {
            throw new RuntimeException("Src not found ! You must specify the source before calling play()");
        }
    }

    public void pause() {
        onPause();
    }

    public void resume() {
        onResume();
    }

    private void onPause() {
        if (mLoadGif != null) {
            Log.d(TAG, "Trying to pause the animation");
            mGifDecoderView.pauseAnimating();
        }
    }

    private void onResume() {
        if (mLoadGif != null) {
            /*if (mLoadGif.getStatus() == Status.RUNNING) {
                while (!mLoadGif.isCancelled()) {
                    mLoadGif.cancel(true);
                }
            }
            else if (mLoadGif.getStatus() == Status.PENDING) {
                mLoadGif.cancel(true);
            }*/
            Log.d(TAG, "Trying to resume the animation");
            mGifDecoderView.resumeAnimating();
        }
    }

    /**
     *Async task for showing gif animation
     * @author Hardik
     */
    class LoadGif extends AsyncTask<Void, Void, Void> implements PropertyChangeListener {

        private InputStream stream;
        private int repeatCount;

        public LoadGif(String giftResource, int repeatCount) throws IOException {
            //Get the inputStream from the asset.
            //stream = getResources().openRawResource(giftResource);
            stream = getResources().getAssets().open(giftResource);

            this.repeatCount = repeatCount;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Make the progressBar visible.
            mProgressBar.setVisibility(View.VISIBLE);
            invalidate();
            requestLayout();
        }

        protected Void doInBackground(Void... params) {
            Log.d(TAG, "Loading Gif");

            //Initial loading The gif animation.
            mGifDecoderView = new GifDecoderView(getContext(), stream, repeatCount);
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            addView(mGifDecoderView);
            Log.d(TAG, "Adding View to the parent");

            //Add the property change listener which will fire the GifDecoderView.ANIMATION_COMPLETED property.
            mGifDecoderView.addPropertyChangeListener(this);
            mGifDecoderView.playGif();

            mProgressBar.setVisibility(View.GONE);
            invalidate();
            requestLayout();

            recycle();
        }

        private void recycle() {
            try {
                Logger.d(TAG, "Closing the inputStream");
                stream.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.d(TAG, "OnCancelled, GifPlayer");
            recycle();
        }

        @Override
        public void propertyChange(PropertyChangeEvent event) {
            //Something when the animation is completed.
        }
    }

    @Deprecated
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

    @Deprecated
    private static InputStream bitmapToInputStream(Bitmap bitmap) {
        int size = bitmap.getHeight() * bitmap.getRowBytes();
        ByteBuffer buffer = ByteBuffer.allocate(size);
        bitmap.copyPixelsToBuffer(buffer);
        return new ByteArrayInputStream(buffer.array());
    }

    @Deprecated
    private InputStream drawableToInputStream(Drawable drawable) {
        if (drawable != null) {
            Bitmap bitmap = drawableToBitmap(drawable);
            return bitmapToInputStream(bitmap);
        }
        return null;
    }

    public void recycle() {
        if (mLoadGif != null) {
            while (!mLoadGif.isCancelled()) {
                Log.d(TAG, "Recycling GifPlayer");
                mLoadGif.cancel(true);
            }
        }
        if (mGifDecoderView != null) {
            mGifDecoderView.destroy();
        }
    }
}
