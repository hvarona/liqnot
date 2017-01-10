package bo;

import com.henja.liqnot.ws.ApiFunction;
import com.henja.liqnot.ws.GetAccountBalances;

import java.util.HashMap;

/**
 * Created by henry on 09/01/2017.
 */

public class AccountBalance implements SharedData {

    private String accountId;
    private HashMap<String,Double> balances = new HashMap();

    public AccountBalance(String accountId) {
        this.accountId = accountId;
    }

    public void setBalances(HashMap<String, Double> balances) {
        this.balances = balances;
    }

    public Double getAssetBalance(String assetId){
        return balances.get(assetId);
    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public ApiFunction getUpdateFunction() {
        return new GetAccountBalances(accountId);
    }
}
