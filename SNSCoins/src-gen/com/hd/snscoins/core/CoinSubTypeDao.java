package com.hd.snscoins.core;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.SqlUtils;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

import com.hd.snscoins.core.CoinSubType;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table sub_type.
*/
public class CoinSubTypeDao extends AbstractDao<CoinSubType, Long> {

    public static final String TABLENAME = "sub_type";

    /**
     * Properties of entity CoinSubType.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Type = new Property(1, String.class, "type", false, "TYPE");
        public final static Property Id_type = new Property(2, long.class, "id_type", false, "ID_TYPE");
    };

    private DaoSession daoSession;

    private Query<CoinSubType> coinType_SubTypeListQuery;

    public CoinSubTypeDao(DaoConfig config) {
        super(config);
    }
    
    public CoinSubTypeDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'sub_type' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'TYPE' TEXT," + // 1: type
                "'ID_TYPE' INTEGER NOT NULL );"); // 2: id_type
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'sub_type'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, CoinSubType entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String type = entity.getType();
        if (type != null) {
            stmt.bindString(2, type);
        }
        stmt.bindLong(3, entity.getId_type());
    }

    @Override
    protected void attachEntity(CoinSubType entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public CoinSubType readEntity(Cursor cursor, int offset) {
        CoinSubType entity = new CoinSubType( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // type
            cursor.getLong(offset + 2) // id_type
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, CoinSubType entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setType(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setId_type(cursor.getLong(offset + 2));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(CoinSubType entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(CoinSubType entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "subTypeList" to-many relationship of CoinType. */
    public List<CoinSubType> _queryCoinType_SubTypeList(long id_type) {
        synchronized (this) {
            if (coinType_SubTypeListQuery == null) {
                QueryBuilder<CoinSubType> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.Id_type.eq(null));
                coinType_SubTypeListQuery = queryBuilder.build();
            }
        }
        Query<CoinSubType> query = coinType_SubTypeListQuery.forCurrentThread();
        query.setParameter(0, id_type);
        return query.list();
    }

    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getCoinTypeDao().getAllColumns());
            builder.append(" FROM sub_type T");
            builder.append(" LEFT JOIN type T0 ON T.'ID_TYPE'=T0.'_id'");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected CoinSubType loadCurrentDeep(Cursor cursor, boolean lock) {
        CoinSubType entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        CoinType coinType = loadCurrentOther(daoSession.getCoinTypeDao(), cursor, offset);
         if(coinType != null) {
            entity.setCoinType(coinType);
        }

        return entity;    
    }

    public CoinSubType loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<CoinSubType> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<CoinSubType> list = new ArrayList<CoinSubType>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<CoinSubType> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<CoinSubType> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
