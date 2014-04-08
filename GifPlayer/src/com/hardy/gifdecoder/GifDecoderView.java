
package com.hardy.gifdecoder;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class GifDecoderView extends ImageView {

    private GifDecoder mGifDecoder;
    private boolean isAnimationCompleted;

    public final static String ANIMATION_COMPLETED = "animationCompleted";

    private Bitmap mTmpBitmap;

    private int mRepeatCount;

    /**
     * Create a view that will play the gif animation.
     * @param context - The activity context
     * @param stream - The input stream to be played
     * @param repeatCount - Number of time the input stream should be played. If -1 is supplied, it will be played forever. 
     * @author Hardik
     */
    public GifDecoderView(Context context, InputStream stream, int repeatCount) {
        super(context);
        this.mRepeatCount = repeatCount;

        mGifDecoder = new GifDecoder();
        mGifDecoder.read(stream);
    }

    public void playGif(InputStream stream, boolean isGiftSendAnimation) {

        isAnimationCompleted = false;
        new StartAnimation().execute((Void[]) null);
    }

    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(
            this);

    public void addPropertyChangeListener(
            PropertyChangeListener propertyChangeListener) {
        propertyChangeSupport.addPropertyChangeListener(propertyChangeListener);
    }

    public void removePropertyChangeListener(
            PropertyChangeListener propertyChangeListener) {
        propertyChangeSupport
                .removePropertyChangeListener(propertyChangeListener);
    }

    protected void setAnimationCompleted(boolean b) {
        boolean isAnimationCompletedOld = isAnimationCompleted;
        isAnimationCompleted = b;

        propertyChangeSupport.firePropertyChange(ANIMATION_COMPLETED, isAnimationCompletedOld, isAnimationCompleted);
    }

    class StartAnimation extends AsyncTask<Void, Integer, Void> {
        final int n = mGifDecoder.getFrameCount();

        @Override
        protected Void doInBackground(Void... params) {
            Log.d("Gif", "RepeatCount = " + mRepeatCount);
            do {
                for (int i = 0; i < n; i++) {
                    mTmpBitmap = mGifDecoder.getFrame(i);
                    final int t = mGifDecoder.getDelay(i);
                    publishProgress(i);
                    try {
                        Thread.sleep(t);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                
                if (mRepeatCount != -1) {
                    mRepeatCount--;
                }
            } while (mRepeatCount > 0 || mRepeatCount == -1);

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            if (mTmpBitmap != null && !mTmpBitmap.isRecycled()) {
                GifDecoderView.this.setImageBitmap(mTmpBitmap);
            }
            if (values[0] == (n - 1)) {
                //TODO: Do something over here to notify about animation being preparing
            }
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            setAnimationCompleted(true);

        }
    }
}
