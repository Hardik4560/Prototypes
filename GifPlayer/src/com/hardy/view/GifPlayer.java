/**
 * @author Hardik
 */

package com.hardy.view;

import com.hardy.gifdecoder.GifDecoderView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

/**
 * @author Hardik
 *
 */
public class GifPlayer extends RelativeLayout {

    ProgressBar mProgressBar;
    GifDecoderView mGifDecoderView;

    Context mContext;

    public GifPlayer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init(context);
    }

    public GifPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
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

    }
}
