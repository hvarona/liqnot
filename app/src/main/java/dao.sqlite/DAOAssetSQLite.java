package dao.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import bo.Asset;
import dao.DAO;
import dao.DAOAsset;
import dao.DAOEnumeration;


/**
 * Created by henry on 12/01/2017.
 */

public class DAOAssetSQLite extends SQLiteOpenHelper implements DAOAsset, DAOSQLite<Asset> {

    public DAOAssetSQLite(Context context){
        super(context, "liqnotasset.db", null, 1);
    }

    public static abstract class AssetTable implements BaseColumns {
        public static final String TABLE_NAME ="asset";
        public static final String ID = "id";
        public static final String SYMBOL = "symbol";
        public static final String PRECISION = "precision";
        public static final String TYPE = "type";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + AssetTable.TABLE_NAME + " ("
                + AssetTable.ID + " TEXT PRIMARY KEY NOT NULL, "
                + AssetTable.SYMBOL + " TEXT NOT NULL, "
                + AssetTable.PRECISION + " INTEGER NOT NULL, "
                + AssetTable.TYPE + " TEXT, "
                + "UNIQUE (" + AssetTable.ID + "))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //
    }

    public boolean insertAsset(Asset asset){
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues newAsset = new ContentValues();
            newAsset.put(AssetTable.ID, asset.getId());
            newAsset.put(AssetTable.SYMBOL, asset.getSymbol());
            newAsset.put(AssetTable.PRECISION, asset.getPrecision());
            newAsset.put(AssetTable.TYPE, asset.getType());
            boolean answer = db.insertWithOnConflict(AssetTable.TABLE_NAME, null, newAsset,SQLiteDatabase.CONFLICT_REPLACE) != -1;
            db.close();
            return answer;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public Asset rowToObject(Object row){
        Cursor c = ((Cursor)row);
        String id = c.getString(c.getColumnIndex(AssetTable.ID));
        String symbol = c.getString(c.getColumnIndex(AssetTable.SYMBOL));
        int precision = c.getInt(c.getColumnIndex(AssetTable.PRECISION));
        String type = c.getString(c.getColumnIndex(AssetTable.TYPE));
        Asset asset = new Asset(id,symbol,precision,type);
        return asset;
    }

    public DAOAssetEnumerationSQLite getAsset(int start, int howMany){
        SQLiteDatabase db = getReadableDatabase();
        Cursor result = db.query(AssetTable.TABLE_NAME, null, null, null, null, null, null, (howMany >= 0?""+howMany:null));
        return new DAOAssetEnumerationSQLite(this, result, start, howMany);
    }

    @Override
    public Asset getAssetByName(String assetName) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor result = db.query(AssetTable.TABLE_NAME,null,"?s = ?s",new String[]{AssetTable.SYMBOL,assetName},null,null,null,null);
        return rowToObject(result);
    }
}
