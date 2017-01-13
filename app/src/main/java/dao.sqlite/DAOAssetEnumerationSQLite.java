package dao.sqlite;

import android.database.Cursor;

import bo.Asset;

/**
 * Created by henry on 12/01/2017.
 */

public class DAOAssetEnumerationSQLite extends DAOEnumerationSQLite<Asset> {
    public DAOAssetEnumerationSQLite(DAOAssetSQLite dao, Cursor c, int start, int howMany) {
        super(dao, c, start, howMany);
    }
}
