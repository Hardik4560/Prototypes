
package com.hd.snscoins.core;

import com.hd.snscoins.db.SnsDbConstants;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.neebal.android.core.model.Model;

@DatabaseTable(tableName = SnsDbConstants.TABLE_COINS)
public class SnsCoin extends Model<SnsCoin> {

    private final static String COLUMN_NAME = "name";
    private static final String COLUMN_ICON_PATH = "icon_location";
    public static final String COLUMN_ID_COIN_GROUP = "id_coin_group";

    @DatabaseField(columnName = COLUMN_NAME)
    private String name;

    @DatabaseField(columnName = COLUMN_ICON_PATH, canBeNull = true)
    private String iconLocation;

    @DatabaseField(columnName = COLUMN_ID_COIN_GROUP, canBeNull = false, foreign = true, foreignAutoCreate = false, foreignAutoRefresh = false)
    private CoinGroup coinGroup;
}
