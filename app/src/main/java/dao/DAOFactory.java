package dao;

import android.content.Context;

import dao.sqlite.DAOFactorySQLite;

/**
 * Created by javier on 03/01/2017.
 */

public abstract class DAOFactory {

    public abstract DAONotifier getNotifierDAO();
    public abstract DAOAsset getAssetDAO();
    public abstract DAOAccount getAccountDAO();

    public static DAOFactorySQLite getSQLiteFactory(Context context){
        return new DAOFactorySQLite(context);
    }
}
