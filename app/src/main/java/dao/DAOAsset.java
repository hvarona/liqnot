package dao;

import bo.Asset;

/**
 * Created by henry on 12/01/2017.
 */

public interface DAOAsset extends DAO<Asset> {
    public boolean insertAsset(Asset asset);

    public DAOEnumeration<DAO<Asset>, Asset> getAsset(int start, int howMany);
    public Asset getAssetByName(String assetName);
}
