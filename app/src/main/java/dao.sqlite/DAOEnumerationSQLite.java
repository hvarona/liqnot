package dao.sqlite;

import android.database.Cursor;

import bo.BO;
import dao.DAO;
import dao.DAOEnumeration;

/**
 * Created by javier on 03/01/2017.
 */

public abstract class DAOEnumerationSQLite<X extends BO> extends DAOEnumeration<DAO<X>, X> {

    public DAOEnumerationSQLite(DAOSQLite dao, Cursor c, int start, int howMany){
        super(dao, c, start, howMany);
        c.moveToPosition(start);
    }

    public int count(){
        return ((Cursor)this.resultHandler).getCount();
    }

    public X next(){
        X result = this.dao.rowToObject(this.resultHandler);
        ((Cursor)this.resultHandler).moveToNext();
        return result;
    }

    public boolean hasNext(){
        return !((Cursor)this.resultHandler).isAfterLast();
    }
}
