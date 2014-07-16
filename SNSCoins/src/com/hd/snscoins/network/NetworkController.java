
package com.hd.snscoins.network;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.hardy.exceptions.ObjectNotInitializedException;
import com.hd.snscoins.ui.HomeScreenActivity;

public class NetworkController {
    private static final String TAG = NetworkController.class.getSimpleName();

    private RequestQueue mRequestQueue;

    private static NetworkController mNetworkController;
    private Context mContext;

    private NetworkController(Context context) {
        this.mContext = context;
    }

    public static NetworkController initialize(Context context) {
        if (mNetworkController == null) {
            mNetworkController = new NetworkController(context);
        }
        return mNetworkController;
    }

    public static NetworkController getInstance() {
        if (mNetworkController == null) {
            throw new ObjectNotInitializedException(NetworkController.class);
        }
        return mNetworkController;
    }

    public synchronized RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext);
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        // set the default tag if tag is empty
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
