package dao.sqlite;

import android.content.Context;

import dao.DAOAccount;
import dao.DAOAsset;
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

    @Override
    public DAOAsset getAssetDAO() {
        return new DAOAssetSQLite(this.context);
    }

    @Override
    public DAOAccount getAccountDAO() {
        return new DAOAccountSQLite(this.context);
    }
}
