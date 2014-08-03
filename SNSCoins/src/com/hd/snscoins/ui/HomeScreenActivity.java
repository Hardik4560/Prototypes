
package com.hd.snscoins.ui;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.googlecode.androidannotations.annotations.EActivity;
import com.hardy.utils.SharedPrefs;
import com.hardy.utils.ToastMaker;
import com.hd.snscoins.R;
import com.hd.snscoins.application.SnSCoreSystem;
import com.hd.snscoins.constants.SnsConstants;
import com.hd.snscoins.core.NewsCategory;
import com.hd.snscoins.db.SnsDatabase;
import com.hd.snscoins.webentities.WeSyncData;

@EActivity(R.layout.activity_home)
public class HomeScreenActivity extends Activity {
    private static final String TAG = HomeScreenActivity.class.getSimpleName();

    protected SnSCoreSystem mAppContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAppContext = (SnSCoreSystem) getApplicationContext();
    }

    public void onCoinsClicked(View v) {
        if (checkDataSynced()) {
            Intent intent = new Intent(this, CoinsActivity_.class);
            startActivity(intent);
        }
    }

    public void onEventsClicked(View v) {
        Intent intent = new Intent(this, EventsActivity_.class);
        startActivity(intent);
    }

    public void onBookmarksClicked(View v) {
        Intent intent = new Intent(this, BookmarkListActivity_.class);
        startActivity(intent);
    }

    public void onNewsClicked(View v) {
        final List<NewsCategory> newsCategoryList = SnsDatabase.session().getNewsCategoryDao().loadAll();

        ArrayAdapter<NewsCategory> adapter = new ArrayAdapter<NewsCategory>(this, android.R.layout.simple_list_item_single_choice, newsCategoryList);

        AlertDialog.Builder dialogAlert = new AlertDialog.Builder(this);
        dialogAlert.setAdapter(adapter, new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                ToastMaker.getInstance().createToast(newsCategoryList.get(which).toString());
                Intent intent = new Intent(getApplicationContext(), NewsActivity_.class);

                mAppContext.setTransientNewsCategory(newsCategoryList.get(which));
                startActivity(intent);
            }
        });

        dialogAlert.create().show();

    }

    private boolean checkDataSynced() {
        return (Boolean) SharedPrefs.getInstance().get(SnsConstants.DATA_SYNCED, false);
    }

    public void onCurrencyClicked(View v) {
        if (checkDataSynced()) {
            Intent intent = new Intent(this, CurrenciesActivity_.class);
            startActivity(intent);
        }
    }

    public interface SyncDataInterface {
        @GET("/repos/{owner}/{repo}/contributors")
        WeSyncData syncdata(@Path("owner") String owner, @Path("repo") String repo);
    }
}
