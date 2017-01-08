package dao.sqlite;

import android.content.Context;

import dao.DAOFactory;
import dao.DAONotifier;

/**
 * Created by javier on 03/01/2017.
 */

public class DAOFactorySQLite extends DAOFactory{

    Context context;

    public DAOFactorySQLite(Context context){
        this.context = context;
    }

    @Override
    public DAONotifier getNotifierDAO() {
        return new DAONotifierSQLite(this.context);
    }
}
