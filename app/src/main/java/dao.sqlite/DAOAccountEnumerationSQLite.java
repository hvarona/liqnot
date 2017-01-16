package dao.sqlite;

import android.database.Cursor;

import bo.Account;
import bo.Asset;

/**
 *
 * Created by henry on 12/01/2017.
 */

public class DAOAccountEnumerationSQLite extends DAOEnumerationSQLite<Account> {
    public DAOAccountEnumerationSQLite(DAOAccountSQLite dao, Cursor c, int start, int howMany) {
        super(dao, c, start, howMany);
    }
}
