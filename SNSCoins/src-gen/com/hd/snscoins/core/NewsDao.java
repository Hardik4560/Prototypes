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

import com.hd.snscoins.core.News;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table news.
*/
public class NewsDao extends AbstractDao<News, Long> {

    public static final String TABLENAME = "news";

    /**
     * Properties of entity News.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Title = new Property(1, String.class, "title", false, "TITLE");
        public final static Property Date = new Property(2, String.class, "date", false, "DATE");
        public final static Property Time = new Property(3, String.class, "time", false, "TIME");
        public final static Property Details = new Property(4, String.class, "details", false, "DETAILS");
        public final static Property Id_category = new Property(5, long.class, "id_category", false, "ID_CATEGORY");
        public final static Property Image_url = new Property(6, String.class, "image_url", false, "IMAGE_URL");
        public final static Property Image_path = new Property(7, String.class, "image_path", false, "IMAGE_PATH");
    };

    private DaoSession daoSession;

    private Query<News> newsCategory_NewsListQuery;

    public NewsDao(DaoConfig config) {
        super(config);
    }
    
    public NewsDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'news' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'TITLE' TEXT NOT NULL ," + // 1: title
                "'DATE' TEXT," + // 2: date
                "'TIME' TEXT," + // 3: time
                "'DETAILS' TEXT," + // 4: details
                "'ID_CATEGORY' INTEGER NOT NULL ," + // 5: id_category
                "'IMAGE_URL' TEXT," + // 6: image_url
                "'IMAGE_PATH' TEXT);"); // 7: image_path
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'news'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, News entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getTitle());
 
        String date = entity.getDate();
        if (date != null) {
            stmt.bindString(3, date);
        }
 
        String time = entity.getTime();
        if (time != null) {
            stmt.bindString(4, time);
        }
 
        String details = entity.getDetails();
        if (details != null) {
            stmt.bindString(5, details);
        }
        stmt.bindLong(6, entity.getId_category());
 
        String image_url = entity.getImage_url();
        if (image_url != null) {
            stmt.bindString(7, image_url);
        }
 
        String image_path = entity.getImage_path();
        if (image_path != null) {
            stmt.bindString(8, image_path);
        }
    }

    @Override
    protected void attachEntity(News entity) {
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
    public News readEntity(Cursor cursor, int offset) {
        News entity = new News( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // title
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // date
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // time
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // details
            cursor.getLong(offset + 5), // id_category
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // image_url
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7) // image_path
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, News entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setTitle(cursor.getString(offset + 1));
        entity.setDate(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setTime(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setDetails(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setId_category(cursor.getLong(offset + 5));
        entity.setImage_url(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setImage_path(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(News entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(News entity) {
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
    
    /** Internal query to resolve the "newsList" to-many relationship of NewsCategory. */
    public List<News> _queryNewsCategory_NewsList(long id_category) {
        synchronized (this) {
            if (newsCategory_NewsListQuery == null) {
                QueryBuilder<News> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.Id_category.eq(null));
                newsCategory_NewsListQuery = queryBuilder.build();
            }
        }
        Query<News> query = newsCategory_NewsListQuery.forCurrentThread();
        query.setParameter(0, id_category);
        return query.list();
    }

    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getNewsCategoryDao().getAllColumns());
            builder.append(" FROM news T");
            builder.append(" LEFT JOIN news_type T0 ON T.'ID_CATEGORY'=T0.'_id'");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected News loadCurrentDeep(Cursor cursor, boolean lock) {
        News entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        NewsCategory newsCategory = loadCurrentOther(daoSession.getNewsCategoryDao(), cursor, offset);
         if(newsCategory != null) {
            entity.setNewsCategory(newsCategory);
        }

        return entity;    
    }

    public News loadDeep(Long key) {
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
    public List<News> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<News> list = new ArrayList<News>(count);
        
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
    
    protected List<News> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<News> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
