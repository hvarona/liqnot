package bo;

import com.henja.liqnot.ws.ApiFunction;
import com.henja.liqnot.ws.GetAccountBalances;

import java.util.Date;
import java.util.HashMap;

/**
 * Has each asset balance of an account
 *
 * Created by henry on 09/01/2017.
 */

public class AccountBalance implements SharedData {
    private static final int EXPIRATION_TIME = 60000; //1 minute

    private String accountId;
    private HashMap<String,Double> balances;
    private Date expirationDate;

    AccountBalance(String accountId) {
        this.accountId = accountId;
        this.expirationDate = new Date(0);
        balances = new HashMap<>();
    }

    public void setBalances(HashMap<String, Double> balances) {
        this.balances = balances;
        this.expirationDate = new Date(System.currentTimeMillis()+EXPIRATION_TIME);
    }

    Double getAssetBalance(String assetId){
        return balances.get(assetId);
    }

    @Override
    public boolean isValid() {
        return expirationDate.after(new Date());
    }

    @Override
    public ApiFunction getUpdateFunction() {
        return new GetAccountBalances(accountId);
    }
}
