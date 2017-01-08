package dao.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import bo.Notifier;
import dao.DAO;
import dao.DAOEnumeration;
import dao.DAONotifier;

/**
 * Created by javier on 03/01/2017.
 */

public class DAONotifierSQLite extends SQLiteOpenHelper implements DAONotifier, DAOSQLite<Notifier> {

    public DAONotifierSQLite(Context context){
        super(context, "liqnot.db", null, 1);
    }

    public static abstract class NotifierTable implements BaseColumns {
        public static final String TABLE_NAME ="notifier";
        public static final String ID = "id";
        public static final String RULE = "rule";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + NotifierTable.TABLE_NAME + " ("
                + NotifierTable.ID + " TEXT PRIMARY KEY NOT NULL, "
                + NotifierTable.RULE + " TEXT NOT NULL, "
                + "UNIQUE (" + NotifierTable.ID + "))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //
    }

    public boolean insertNotifier(Notifier notifier){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues newNotifier = new ContentValues();
        newNotifier.put(NotifierTable.ID, notifier.getId());
        newNotifier.put(NotifierTable.RULE, notifier.getRule().toJson());

        return db.insert(NotifierTable.TABLE_NAME, null, newNotifier) != -1;
    }

    public Notifier rowToObject(Object row){
        Cursor c = ((Cursor)row);
        return new Notifier(
                c.getString(c.getColumnIndex(NotifierTable.ID))
        );
    }

    public DAONotifierEnumerationSQLite getNotifiers(int start, int howMany){
        SQLiteDatabase db = getReadableDatabase();

        Cursor result = db.query(NotifierTable.TABLE_NAME, null, null, null, null, null, null, ""+howMany);

        return new DAONotifierEnumerationSQLite(this, result, start, howMany);
    }
}
