
package com.hd.snscoins.ui;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;
import com.hd.snscoins.R;

@EActivity(R.layout.activity_country_list)
public class CountryListActivity extends Activity {

    @ViewById(R.id.lst_countries)
    protected ListView lstCountries;

    private CountryAdapter adapterCountry;

    @AfterViews
    protected void init() {
        adapterCountry = new CountryAdapter();
        lstCountries.setAdapter(adapterCountry);
    }

    private class CountryAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Object getItem(int arg0) {
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.e_lst_country, null);
            }
            return convertView;
        }
    }
}
