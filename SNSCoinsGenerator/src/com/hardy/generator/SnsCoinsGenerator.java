
package com.hardy.generator;

import java.io.IOException;

import com.hd.snscoins.db.SnsDatabase;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

public class SnsCoinsGenerator {
    private static final String defaultJavaPackage = "com.hd.snscoins.core";

    public static void main(String[] args) throws IOException, Exception {
        Schema schema = new Schema(1, defaultJavaPackage);
        schema.enableKeepSectionsByDefault();

        addCoins(schema);

        new DaoGenerator().generateAll(schema, "../SNSCoins/src-gen");
    }

    private static void addCoins(Schema schema) {
        //CoinType will have toMany with Coins;
        Entity type = schema.addEntity("CoinType");
        type.setTableName(SnsDatabase.TABLE_COIN_TYPE); // "ORDER" is a reserved keyword
        type.addIdProperty();
        type.addStringProperty("type");

        //Relation of subtype and type.
        Entity sub_type = schema.addEntity("CoinSubType");
        sub_type.implementsSerializable();
        sub_type.setTableName(SnsDatabase.TABLE_COIN_SUB_TYPE); // "ORDER" is a reserved keyword
        sub_type.addIdProperty();
        sub_type.addStringProperty("type");

        //Relation with coinType
        Property parent_type_id = sub_type.addLongProperty("id_type").notNull().getProperty();
        sub_type.addToOne(type, parent_type_id);

        ToMany typeToSubType = type.addToMany(sub_type, parent_type_id);
        typeToSubType.setName("subTypeList");

        //Coins will have oneToOne with CoinTypes
        Entity coin = schema.addEntity("Coin");
        coin.setTableName(SnsDatabase.TABLE_COIN);
        coin.addIdProperty();
        coin.addStringProperty("name").notNull();
        coin.addStringProperty("icon_location");

        /* //Relation with coinType
         Property coin_type_id = coin.addLongProperty("id_type").notNull().getProperty();
         coin.addToOne(type, coin_type_id);*/

        Property coin_sub_type_id = coin.addLongProperty("id_sub_type").notNull().getProperty();
        coin.addToOne(sub_type, coin_sub_type_id);

        ToMany coinTypeToCoin = sub_type.addToMany(coin, coin_sub_type_id);
        coinTypeToCoin.setName("coinList");
    }
}
