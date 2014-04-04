
package com.hardy.gifdecoder;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

public class GifDecoderView extends ImageView {

    private GifDecoder mGifDecoder;
    private Context mContext;
    private boolean isAnimationCompleted;
    private boolean isGiftSendAnimation;

    public final static String ANIMATION_COMPLETED = "animationCompleted";

    private Bitmap mTmpBitmap;

    public GifDecoderView(Context context, InputStream stream) {
        super(context);
        mContext = context;
        mGifDecoder = new GifDecoder();
        mGifDecoder.read(stream);
    }

    public void playGif(InputStream stream, boolean isGiftSendAnimation) {

        isAnimationCompleted = false;
        this.isGiftSendAnimation = isGiftSendAnimation;
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
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            if (mTmpBitmap != null && !mTmpBitmap.isRecycled()) {
                GifDecoderView.this.setImageBitmap(mTmpBitmap);
            }
            if (values[0] == (n - 1) && isGiftSendAnimation) {

                //TODO: Do something over here to notify user about animation being preparing
            }
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            setAnimationCompleted(true);

        }
    }
}
