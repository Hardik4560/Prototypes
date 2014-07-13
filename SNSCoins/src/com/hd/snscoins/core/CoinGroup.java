
package com.hd.snscoins.core;

import java.sql.SQLException;
import java.util.List;

import com.hd.snscoins.db.SnsDbConstants;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.table.DatabaseTable;
import com.neebal.android.core.model.Model;

@DatabaseTable(tableName = SnsDbConstants.TABLE_COIN_GROUPS)
public class CoinGroup extends Model<CoinGroup> {

    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_BANNER_PATH = "banner_location";

    @DatabaseField(columnName = COLUMN_NAME, canBeNull = false)
    private String name;

    @DatabaseField(columnName = COLUMN_BANNER_PATH, canBeNull = true)
    private String bannerLocation;

    private SnsCoinList coinList;

    public CoinGroup() {
        super();
        coinList = new SnsCoinList();
    }

    public CoinGroup(String name, String bannerLocation, SnsCoinList coinList) {
        super();
        this.name = name;
        this.bannerLocation = bannerLocation;
        this.coinList = coinList;
    }

    public synchronized void addToCoinList(SnsCoin coin, boolean flushToDb) throws SQLException {
        coinList.buildModelList(coin);

        if (flushToDb) {
            coin.persist();
        }
    }

    @Override
    public void persist() throws SQLException {
        super.persist();

        for (int i = 0; i < coinList.size(); i++) {
            coinList.get(i).persist();
        }
    }

    @Override
    public synchronized boolean loadCustomAttributes(int stageNo) throws SQLException {
        Where where = getWhere(SnsCoin.class);
        where.eq(SnsCoin.COLUMN_ID_COIN_GROUP, this.getId());

        List<SnsCoin> coins = findAll(SnsCoin.class, where);

        return super.loadCustomAttributes(stageNo);
    }
}
