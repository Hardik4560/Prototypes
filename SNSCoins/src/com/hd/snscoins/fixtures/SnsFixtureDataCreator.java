
package com.hd.snscoins.fixtures;

import java.util.List;

import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.api.Scope;
import com.hardy.utils.SharedPrefs;
import com.hd.snscoins.application.SnSCoreSystem;
import com.hd.snscoins.core.Coin;
import com.hd.snscoins.core.CoinSubType;
import com.hd.snscoins.core.CoinType;
import com.hd.snscoins.db.SnsDatabase;

/**
 * The files is highly unstable, don't use it. Only kept for reference purpose.
 * @author Hardik Shah
 *
 */
@EBean(scope = Scope.Singleton)
public class SnsFixtureDataCreator {

    private static final String TAG = SnsFixtureDataCreator.class.getSimpleName();
    private SnSCoreSystem mAppContext;
    private boolean saveInDb = true;

    public SnsFixtureDataCreator() {
        super();
    }

    @Background
    public void createFixtureData(SnSCoreSystem mAppContext) {
        this.mAppContext = mAppContext;

        //Read from the files and store it in the class member variables;
        SnsFixtureDataReader.init(mAppContext);
        SnsFixtureDataReader.getInstance().initialDataReadingProcess();

        //Create fixture data

        List<String> coinNames = SnsFixtureDataReader.getCoins();
        List<String> coinTypeNames = SnsFixtureDataReader.getCoinTypes();
        List<String> coinSubTypeNames = SnsFixtureDataReader.getCoinSubTypes();

        Long coinTypeCount = 1L;
        Long coinSubTypeCount = 1L;
        Long coinCount = 0L;

        //Save the coinType in the database.
        for (int i = 0; i < coinTypeNames.size(); i++) {
            String coinTypeName = coinTypeNames.get(i).trim();

            CoinType coinType = new CoinType(coinTypeCount, coinTypeName);
            SnsDatabase.session().getCoinTypeDao().insert(coinType);

            coinTypeCount++;
        }

        //Save the coinSubType in the database.
        for (int i = 0; i < coinSubTypeNames.size(); i++) {
            String coinSubTypeName = coinSubTypeNames.get(i).trim();

            CoinSubType coinSubType = new CoinSubType(coinSubTypeCount++, coinSubTypeName, 1);
            SnsDatabase.session().getCoinSubTypeDao().insert(coinSubType);

            coinSubTypeCount++;
        }

        //Create some coins
        for (int i = 0; i < coinNames.size(); i++) {
            String[] names = coinNames.get(i).split(">");

            String coinName = names[0].trim();
            Long coinSubTypeId = Long.valueOf(names[2].trim());

            Coin coin = new Coin(coinCount, coinName, "", coinSubTypeId);
            SnsDatabase.session().getCoinDao().insert(coin);

            coinCount++;
        }

        SharedPrefs.getInstance().add(SnSCoreSystem.DATABASE_INITIALIZED, true);
    }
}
