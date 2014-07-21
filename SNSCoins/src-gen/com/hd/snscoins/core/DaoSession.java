package com.hd.snscoins.core;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import com.hd.snscoins.core.CoinType;
import com.hd.snscoins.core.CoinSubType;
import com.hd.snscoins.core.Coin;
import com.hd.snscoins.core.Events;

import com.hd.snscoins.core.CoinTypeDao;
import com.hd.snscoins.core.CoinSubTypeDao;
import com.hd.snscoins.core.CoinDao;
import com.hd.snscoins.core.EventsDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig coinTypeDaoConfig;
    private final DaoConfig coinSubTypeDaoConfig;
    private final DaoConfig coinDaoConfig;
    private final DaoConfig eventsDaoConfig;

    private final CoinTypeDao coinTypeDao;
    private final CoinSubTypeDao coinSubTypeDao;
    private final CoinDao coinDao;
    private final EventsDao eventsDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        coinTypeDaoConfig = daoConfigMap.get(CoinTypeDao.class).clone();
        coinTypeDaoConfig.initIdentityScope(type);

        coinSubTypeDaoConfig = daoConfigMap.get(CoinSubTypeDao.class).clone();
        coinSubTypeDaoConfig.initIdentityScope(type);

        coinDaoConfig = daoConfigMap.get(CoinDao.class).clone();
        coinDaoConfig.initIdentityScope(type);

        eventsDaoConfig = daoConfigMap.get(EventsDao.class).clone();
        eventsDaoConfig.initIdentityScope(type);

        coinTypeDao = new CoinTypeDao(coinTypeDaoConfig, this);
        coinSubTypeDao = new CoinSubTypeDao(coinSubTypeDaoConfig, this);
        coinDao = new CoinDao(coinDaoConfig, this);
        eventsDao = new EventsDao(eventsDaoConfig, this);

        registerDao(CoinType.class, coinTypeDao);
        registerDao(CoinSubType.class, coinSubTypeDao);
        registerDao(Coin.class, coinDao);
        registerDao(Events.class, eventsDao);
    }
    
    public void clear() {
        coinTypeDaoConfig.getIdentityScope().clear();
        coinSubTypeDaoConfig.getIdentityScope().clear();
        coinDaoConfig.getIdentityScope().clear();
        eventsDaoConfig.getIdentityScope().clear();
    }

    public CoinTypeDao getCoinTypeDao() {
        return coinTypeDao;
    }

    public CoinSubTypeDao getCoinSubTypeDao() {
        return coinSubTypeDao;
    }

    public CoinDao getCoinDao() {
        return coinDao;
    }

    public EventsDao getEventsDao() {
        return eventsDao;
    }

}
