package bo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

/**
 *
 * Created by henry on 09/01/2017.
 */

public abstract class SharedDataCentral {
    private static HashMap<String,Asset> assets = new HashMap<>();
    private static HashMap<String,AccountBalance> accountsBalances = new HashMap<>();
    private static HashMap<String,HashMap<String,AssetEquivalentRate>> equivalentsRates = new HashMap<>();
    private static HashMap<String,Account> accounts = new HashMap<>();

    public static AssetEquivalentRate getEquivalentRate(String base, String quote){
        if(!equivalentsRates.containsKey(base)){
            equivalentsRates.put(base,new HashMap<String,AssetEquivalentRate>());
        }
        if(!equivalentsRates.get(base).containsKey(quote)){
            equivalentsRates.get(base).put(quote,new AssetEquivalentRate(getAsset(base),getAsset(quote)));
        }
        AssetEquivalentRate eqRate = equivalentsRates.get(base).get(quote);
        eqRate.setBaseCurrency(getAsset(base));
        eqRate.setQuotedCurrency(getAsset(quote));

        if (base.equals(quote)){
            eqRate.setValue(1);
        }

        return eqRate;
    }

    public static void putEquivalentsRate(AssetEquivalentRate equivalentRate){
        String base = equivalentRate.getBaseCurrency().getSymbol();
        String quote = equivalentRate.getQuotedCurrency().getSymbol();
        if(!equivalentsRates.containsKey(base)){
            equivalentsRates.put(base,new HashMap<String,AssetEquivalentRate>());
        }
        if(equivalentsRates.get(base).containsKey(quote)){
            equivalentsRates.get(base).remove(quote);
        }
        equivalentsRates.get(base).put(quote,equivalentRate);
    }

    static Asset getAsset(String assetName){
            if (!assets.containsKey(assetName)) {
                Asset newAsset = new Asset(assetName);
                assets.put(assetName, newAsset);
            }
        return assets.get(assetName);
    }

    public static Asset getAssetByID(String assetID){
        for(Asset asset : assets.values()){
            if(asset != null && asset.getId().equals(assetID)){
                return asset;
            }
        }
        return new Asset(assetID);
    }

    public static Asset getAssetBySymbol(String symbol) {
        for(Asset asset : assets.values()){
            if(asset != null && asset.getSymbol().equals(symbol)){
                return asset;
            }
        }
        return null;
    }

    public static void putAsset(Asset asset){
        if(asset != null){
        if (assets.containsKey(asset.getSymbol())) {
            assets.remove(asset.getSymbol());
        }
        assets.put(asset.getSymbol(), asset);
        }
    }

    public static int getAssetsCount(){
        return assets.size();
    }

    public static AccountBalance getAccountBalance(String accountID){
        if(!accountsBalances.containsKey(accountID)){
            accountsBalances.put(accountID,new AccountBalance(accountID));
        }
        return accountsBalances.get(accountID);
    }

    public static void putAccountBalance(String accountID, AccountBalance balance){
        if(accountsBalances.containsKey(accountID)){
            accountsBalances.remove(accountID);
        }
        accountsBalances.put(accountID,balance);
    }

    public static ArrayList<String> getAssetsList(){
        ArrayList<String> answer = new ArrayList<>();
        for(Asset asset : assets.values()){
            answer.add(asset.getSymbol());
        }
        Collections.sort(answer);
        return answer;
    }

    public static ArrayList<String> getSmartcoinAssesList(){
        ArrayList<String> answer = new ArrayList<>();
        for(Asset asset : assets.values()){
            if(asset.getType().equalsIgnoreCase("smartcoin")){
                answer.add(asset.getSymbol());
            }
        }
        Collections.sort(answer);
        return answer;
    }

    public static void putAccount(Account account){
        accounts.put(account.getName(),account);
    }

    public static Account getAccount(String accountName){
        return accounts.get(accountName);
    }

}
