
package com.hardy.gifdecoder;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.InputStream;

import com.hardy.log.Logger;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.util.Log;
import android.widget.ImageView;

public class GifDecoderView extends ImageView {

    private GifDecoder mGifDecoder;
    private boolean isAnimationCompleted;

    public final static String ANIMATION_COMPLETED = "animationCompleted";
    private static final String TAG = GifDecoderView.class.getSimpleName();

    private Bitmap mTmpBitmap;

    private int mRepeatCount;

    private PlayAnimation mPlayAnimationLoader;

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

    public void playGif() {

        isAnimationCompleted = false;

        if (mPlayAnimationLoader != null) {
            mPlayAnimationLoader.cancel(true);
        }

        mPlayAnimationLoader = new PlayAnimation();
        mPlayAnimationLoader.execute();
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

    class PlayAnimation extends AsyncTask<Void, Integer, Void> {
        final int n = mGifDecoder.getFrameCount();
        boolean isPaused = true;
        String test = "Test";

        @Override
        protected Void doInBackground(Void... params) {
            Log.d("Gif", "RepeatCount = " + mRepeatCount);
            isPaused = false;

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

                    synchronized (test) {
                        if (isPaused) {

                            try {
                                Logger.d(TAG, "Waiting");
                                test.wait();
                            }
                            catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }

                if (mRepeatCount > 0) {
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

        public void pause() {
            synchronized (test) {
                Logger.d(TAG, "onPaused");
                isPaused = true;
            }
        }

        public void resume() {
            synchronized (test) {
                Logger.d(TAG, "onResume, notifying");
                isPaused = false;
                test.notify();
            }
        }
    }

    public void destroy() {
        if (mPlayAnimationLoader != null) {
            mPlayAnimationLoader.cancel(true);
            mGifDecoder.destroy();

            Log.d(TAG, "GifDecoderView destroyed");
        }
    }

    public void pauseAnimating() {
        if (mPlayAnimationLoader != null && mPlayAnimationLoader.getStatus() == Status.RUNNING) {
            mPlayAnimationLoader.pause();
        }
    }

    public void resumeAnimating() {
        if (mPlayAnimationLoader != null && mPlayAnimationLoader.getStatus() == Status.RUNNING) {
            mPlayAnimationLoader.resume();
        }
    }
}
