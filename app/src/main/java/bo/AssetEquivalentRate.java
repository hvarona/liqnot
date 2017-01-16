package bo;

import com.henja.liqnot.ws.ApiFunction;
import com.henja.liqnot.ws.GetEquivalentRate;

import java.util.Date;

/**
 * Class that holds the equivalent rate of two Assets
 *
 * Created by henry on 09/01/2017.
 */

public class AssetEquivalentRate implements SharedData {
    private static final int EXPIRATION_TIME = 300000; //5 minutes

    private Asset baseCurrency;
    private Asset quotedCurrency;
    private double value = 0;
    private Date expirationDate;

    AssetEquivalentRate(Asset baseCurrency, Asset quotedCurrency) {
        this.baseCurrency = baseCurrency;
        this.quotedCurrency = quotedCurrency;
        expirationDate = new Date(0);
    }

    Asset getBaseCurrency() {
        return baseCurrency;
    }

    Asset getQuotedCurrency() {
        return quotedCurrency;
    }

    void setBaseCurrency(Asset baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    void setQuotedCurrency(Asset quotedCurrency) {
        this.quotedCurrency = quotedCurrency;
    }

    @Override
    public boolean isValid() {
        return expirationDate.after(new Date());
    }

    @Override
    public ApiFunction getUpdateFunction() {
        return new GetEquivalentRate(baseCurrency, quotedCurrency);
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
        expirationDate = new Date(System.currentTimeMillis()+EXPIRATION_TIME);
    }
}
