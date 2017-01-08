package dao.sqlite;

import android.database.Cursor;

import bo.Notifier;
import dao.DAOEnumeration;
import dao.DAONotifier;

/**
 * Created by javier on 03/01/2017.
 */
public class DAONotifierEnumerationSQLite extends DAOEnumerationSQLite<Notifier> {
    public DAONotifierEnumerationSQLite(DAONotifierSQLite dao, Cursor c, int start, int howMany) {
        super(dao, c, start, howMany);
    }
}
