package dao.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import bo.Account;
import dao.DAOAccount;


/**
 * Created by henry on 12/01/2017.
 */

public class DAOAccountSQLite extends SQLiteOpenHelper implements DAOAccount, DAOSQLite<Account> {

    public DAOAccountSQLite(Context context){
        super(context, "liqnotaccount.db", null, 1);
    }

    public static abstract class AccountTable implements BaseColumns {
        public static final String TABLE_NAME ="account";
        public static final String ID = "id";
        public static final String NAME = "name";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + AccountTable.TABLE_NAME + " ("
                + AccountTable.ID + " TEXT PRIMARY KEY NOT NULL, "
                + AccountTable.NAME + " TEXT NOT NULL, "
                + "UNIQUE (" + AccountTable.ID + "))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //
    }

    public boolean insertAccount(Account account){
        try {
            SQLiteDatabase db = getWritableDatabase();

            ContentValues newAccount = new ContentValues();
            newAccount.put(AccountTable.ID, account.getId());
            newAccount.put(AccountTable.NAME, account.getName());

            return db.insert(AccountTable.TABLE_NAME, null, newAccount) != -1;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public Account rowToObject(Object row){
        Cursor c = ((Cursor)row);
        String id = c.getString(c.getColumnIndex(AccountTable.ID));
        String name = c.getString(c.getColumnIndex(AccountTable.NAME));
        Account account = new Account(id,name);
        return account;
    }

    public DAOAccountEnumerationSQLite getAccount(int start, int howMany){
        SQLiteDatabase db = getReadableDatabase();

        Cursor result = db.query(AccountTable.TABLE_NAME, null, null, null, null, null, null, (howMany >= 0?""+howMany:null));

        return new DAOAccountEnumerationSQLite(this, result, start, howMany);
    }

    @Override
    public Account getAccountByName(String accountName) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor result = db.query(AccountTable.TABLE_NAME,null,"?s = ?s",new String[]{AccountTable.NAME,accountName},null,null,null,null);
        return rowToObject(result);
    }
}
