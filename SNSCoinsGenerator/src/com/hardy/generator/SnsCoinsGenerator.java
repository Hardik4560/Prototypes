
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
        addEvents(schema);
        addNews(schema);

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

        coin.addStringProperty("image_url");
        coin.addStringProperty("image_path");

        /* //Relation with coinType
         Property coin_type_id = coin.addLongProperty("id_type").notNull().getProperty();
         coin.addToOne(type, coin_type_id);*/

        Property coin_sub_type_id = coin.addLongProperty("id_sub_type").notNull().getProperty();
        coin.addToOne(sub_type, coin_sub_type_id);

        ToMany coinTypeToCoin = sub_type.addToMany(coin, coin_sub_type_id);
        coinTypeToCoin.setName("coinList");

        //Create the year, product will have the list of year and each year should have list of mint.
        Entity year = schema.addEntity("Year");
        year.setTableName(SnsDatabase.TABLE_YEAR);
        year.addIdProperty();
        year.addStringProperty("title");

        // add the relation.
        Property year_coin_id = year.addLongProperty("id_coin").notNull().getProperty();
        year.addToOne(coin, year_coin_id);

        ToMany coinToYears = coin.addToMany(year, year_coin_id);
        coinToYears.setName("yearList");

        //Create the year, product will have the list of year and each year should have list of mint.
        Entity mint = schema.addEntity("Mint");
        mint.setTableName(SnsDatabase.TABLE_MINT);
        mint.addIdProperty();
        mint.addStringProperty("title");
        mint.addIntProperty("rare");

        Property mint_year_id = mint.addLongProperty("id_year").notNull().getProperty();
        mint.addToOne(year, mint_year_id);

        ToMany yearToMint = year.addToMany(mint, mint_year_id);
        yearToMint.setName("mintList");
    }

    private static void addEvents(Schema schema) {
        Entity event = schema.addEntity("Events");
        event.setTableName(SnsDatabase.TABLE_EVENTS); // "ORDER" is a reserved keyword
        event.addIdProperty();
        event.addStringProperty("title").notNull();
        event.addStringProperty("start_date");
        event.addStringProperty("start_time");
        event.addStringProperty("end_date");
        event.addStringProperty("end_time");
        event.addStringProperty("venue");
        event.addStringProperty("details");
        event.addStringProperty("image_url");
        event.addStringProperty("image_path");
    }

    private static void addNews(Schema schema) {
        //CoinType will have toMany with Coins;
        Entity type = schema.addEntity("NewsCategory");
        type.setTableName(SnsDatabase.TABLE_NEWS_TYPE);
        type.addIdProperty();
        type.addStringProperty("title");

        Entity news = schema.addEntity("News");
        news.setTableName(SnsDatabase.TABLE_NEWS);
        news.addIdProperty();
        news.addStringProperty("title").notNull();
        news.addStringProperty("date");
        news.addStringProperty("time");
        news.addStringProperty("details");

        Property news_category_id = news.addLongProperty("id_category").notNull().getProperty();
        news.addToOne(type, news_category_id);

        ToMany categoryToNew = type.addToMany(news, news_category_id);
        categoryToNew.setName("newsList");

        news.addStringProperty("image_url");
        news.addStringProperty("image_path");
    }
}
