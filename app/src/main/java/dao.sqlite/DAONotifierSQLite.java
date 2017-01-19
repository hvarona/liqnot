package dao.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.Date;

import bo.Notifier;
import bo.NotifierRuleFactory;
import dao.DAO;
import dao.DAOEnumeration;
import dao.DAONotifier;

/**
 * Created by javier on 03/01/2017.
 */

public class DAONotifierSQLite extends SQLiteOpenHelper implements DAONotifier, DAOSQLite<Notifier> {

    public DAONotifierSQLite(Context context){
        super(context, "liqnotnotifier.db", null, 3);
    }

    public static abstract class NotifierTable implements BaseColumns {
        public static final String TABLE_NAME ="notifier";
        public static final String ID = "id";
        public static final String RULE = "rule";
        public static final String LAST_NOTIFICATION = "last_notification";
        public static final String ACTIVE = "is_active";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + NotifierTable.TABLE_NAME + " ("
                + NotifierTable.ID + " TEXT PRIMARY KEY NOT NULL, "
                + NotifierTable.RULE + " TEXT NOT NULL, "
                + NotifierTable.LAST_NOTIFICATION + " INTEGER, "
                + NotifierTable.ACTIVE + " INTEGER, "
                + "UNIQUE (" + NotifierTable.ID + "))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2){
            db.execSQL("ALTER TABLE " + NotifierTable.TABLE_NAME + " "
                + " ADD " + NotifierTable.LAST_NOTIFICATION + " INTEGER "
            );
        }
        if (oldVersion < 3){
            db.execSQL("ALTER TABLE " + NotifierTable.TABLE_NAME + " "
                    + " ADD " + NotifierTable.ACTIVE + " INTEGER "
            );
        }
    }

    public boolean insertNotifier(Notifier notifier){
        try {
            SQLiteDatabase db = getWritableDatabase();

            ContentValues newNotifier = new ContentValues();
            newNotifier.put(NotifierTable.ID, notifier.getId());
            newNotifier.put(NotifierTable.RULE, notifier.getRule().toJson());
            newNotifier.put(NotifierTable.LAST_NOTIFICATION, notifier.getLastNotifyDate().getTime()/60000);
            newNotifier.put(NotifierTable.ACTIVE, notifier.isActive()?1:0);

            return db.insert(NotifierTable.TABLE_NAME, null, newNotifier) != -1;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean modifyNotifier(Notifier notifier){
        try {
            SQLiteDatabase db = getWritableDatabase();

            String[] whereArgs = new String[] { notifier.getId() };

            ContentValues newNotifier = new ContentValues();
            if(notifier.getPendingRule()!= null) {
                newNotifier.put(NotifierTable.RULE, notifier.getPendingRule().toJson());
            }else{
                newNotifier.put(NotifierTable.RULE, notifier.getRule().toJson());
            }
            newNotifier.put(NotifierTable.LAST_NOTIFICATION, notifier.getLastNotifyDate().getTime()/60000);
            newNotifier.put(NotifierTable.ACTIVE, notifier.isActive()?1:0);

            return db.update(NotifierTable.TABLE_NAME, newNotifier, NotifierTable.ID+"=?", whereArgs) > 0;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeNotifier(Notifier notifier){
        try {
            SQLiteDatabase db = getWritableDatabase();

            String[] whereArgs = new String[] { notifier.getId() };

            return db.delete(NotifierTable.TABLE_NAME, NotifierTable.ID+"=?", whereArgs) > 0;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public Notifier rowToObject(Object row){
        Cursor c = ((Cursor)row);
        Notifier notifier = new Notifier(
                c.getString(c.getColumnIndex(NotifierTable.ID))
        );

        notifier.setRule(NotifierRuleFactory.importFromJson(c.getString(c.getColumnIndex(NotifierTable.RULE))));
        notifier.setLastNotifyDate(new Date(c.getLong(c.getColumnIndex(NotifierTable.LAST_NOTIFICATION))*60000));
        notifier.setActive(c.getInt(c.getColumnIndex(NotifierTable.ACTIVE))==1);

        return notifier;
    }

    public DAONotifierEnumerationSQLite getNotifiers(int start, int howMany){
        SQLiteDatabase db = getReadableDatabase();

        Cursor result = db.query(NotifierTable.TABLE_NAME, null, null, null, null, null, null, (howMany >= 0?""+howMany:null));

        return new DAONotifierEnumerationSQLite(this, result, start, howMany);
    }
}
