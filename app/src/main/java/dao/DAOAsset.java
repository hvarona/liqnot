package dao;

import bo.Asset;

/**
 *
 * Created by henry on 12/01/2017.
 */

public interface DAOAsset extends DAO<Asset> {
    boolean insertAsset(Asset asset);

    DAOEnumeration<DAO<Asset>, Asset> getAsset(int start, int howMany);
    Asset getAssetByName(String assetName);
}
