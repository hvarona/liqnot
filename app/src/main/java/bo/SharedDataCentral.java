package bo;

import java.util.HashMap;

/**
 * Created by henry on 09/01/2017.
 */

public abstract class SharedDataCentral {
    private static HashMap<String,Asset> assets = new HashMap();
    private static HashMap<String,AccountBalance> accountsBalances = new HashMap();
    private static HashMap<String,HashMap<String,AssetEquivalentRate>> equivalentsRates = new HashMap();

    public static AssetEquivalentRate getEquivalentRate(String base, String quote){
        if(!equivalentsRates.containsKey(base)){
            equivalentsRates.put(base,new HashMap());
        }
        if(!equivalentsRates.get(base).containsKey(quote)){
            equivalentsRates.get(base).put(quote,new AssetEquivalentRate(getAsset(base),getAsset(quote)));
        }
        return equivalentsRates.get(base).get(quote);
    }

    public static void putEquivalentsRate(AssetEquivalentRate equivalentRate){
        String base = equivalentRate.getBaseCurrency().getSymbol();
        String quote = equivalentRate.getQuotedCurrency().getSymbol();
        if(!equivalentsRates.containsKey(base)){
            equivalentsRates.put(base,new HashMap());
        }
        equivalentsRates.get(base).put(quote,equivalentRate);
    }

    public static Asset getAsset(String assetName){
        if(!assets.containsKey(assetName)){
            Asset newAsset = new Asset(assetName);

            //TODO this has to be removed, it's used only for testing
            if (assetName == "BTS"){//TODO this has to be removed, it's used only for testing
                newAsset.setId("1.3.0");//TODO this has to be removed, it's used only for testing
                newAsset.setPrecision(5);//TODO this has to be removed, it's used only for testing
            } else if (assetName == "BLOCKPAY"){//TODO this has to be removed, it's used only for testing
                newAsset.setId("1.3.1072");//TODO this has to be removed, it's used only for testing
                newAsset.setPrecision(4);//TODO this has to be removed, it's used only for testing
            } else if (assetName == "USD"){//TODO this has to be removed, it's used only for testing
                newAsset.setId("1.3.121");//TODO this has to be removed, it's used only for testing
                newAsset.setPrecision(4);//TODO this has to be removed, it's used only for testing
            }

            assets.put(assetName,newAsset);
        }
        return assets.get(assetName);
    }

    public static void putAsset(Asset asset){
        assets.put(asset.getSymbol(),asset);
    }

    public static AccountBalance getAccountBalance(String accountID){
        if(!accountsBalances.containsKey(accountID)){
            accountsBalances.put(accountID,new AccountBalance(accountID));
        }
        return accountsBalances.get(accountID);
    }

    public static void putAccountBalance(String accountID, AccountBalance balance){
        accountsBalances.put(accountID,balance);
    }
}
