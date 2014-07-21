package com.hd.snscoins.core;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.hd.snscoins.core.Events;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table events.
*/
public class EventsDao extends AbstractDao<Events, Long> {

    public static final String TABLENAME = "events";

    /**
     * Properties of entity Events.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Title = new Property(1, String.class, "title", false, "TITLE");
        public final static Property Start_date = new Property(2, String.class, "start_date", false, "START_DATE");
        public final static Property Start_time = new Property(3, String.class, "start_time", false, "START_TIME");
        public final static Property End_date = new Property(4, String.class, "end_date", false, "END_DATE");
        public final static Property End_time = new Property(5, String.class, "end_time", false, "END_TIME");
        public final static Property Venue = new Property(6, String.class, "venue", false, "VENUE");
        public final static Property Details = new Property(7, String.class, "details", false, "DETAILS");
        public final static Property Img_path = new Property(8, String.class, "img_path", false, "IMG_PATH");
    };


    public EventsDao(DaoConfig config) {
        super(config);
    }
    
    public EventsDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'events' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'TITLE' TEXT NOT NULL ," + // 1: title
                "'START_DATE' TEXT," + // 2: start_date
                "'START_TIME' TEXT," + // 3: start_time
                "'END_DATE' TEXT," + // 4: end_date
                "'END_TIME' TEXT," + // 5: end_time
                "'VENUE' TEXT," + // 6: venue
                "'DETAILS' TEXT," + // 7: details
                "'IMG_PATH' TEXT);"); // 8: img_path
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'events'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Events entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getTitle());
 
        String start_date = entity.getStart_date();
        if (start_date != null) {
            stmt.bindString(3, start_date);
        }
 
        String start_time = entity.getStart_time();
        if (start_time != null) {
            stmt.bindString(4, start_time);
        }
 
        String end_date = entity.getEnd_date();
        if (end_date != null) {
            stmt.bindString(5, end_date);
        }
 
        String end_time = entity.getEnd_time();
        if (end_time != null) {
            stmt.bindString(6, end_time);
        }
 
        String venue = entity.getVenue();
        if (venue != null) {
            stmt.bindString(7, venue);
        }
 
        String details = entity.getDetails();
        if (details != null) {
            stmt.bindString(8, details);
        }
 
        String img_path = entity.getImg_path();
        if (img_path != null) {
            stmt.bindString(9, img_path);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Events readEntity(Cursor cursor, int offset) {
        Events entity = new Events( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // title
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // start_date
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // start_time
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // end_date
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // end_time
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // venue
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // details
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8) // img_path
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Events entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setTitle(cursor.getString(offset + 1));
        entity.setStart_date(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setStart_time(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setEnd_date(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setEnd_time(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setVenue(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setDetails(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setImg_path(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Events entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Events entity) {
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
    
}
