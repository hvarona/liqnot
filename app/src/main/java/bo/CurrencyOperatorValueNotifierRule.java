package bo;

import com.henja.liqnot.ws.ApiFunction;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 *
 * Created by javier on 04/01/2017.
 */

public class CurrencyOperatorValueNotifierRule extends NotifierRule {

    private Account account;
    private NotifierCurrency baseCurrency;
    private NotifierCurrency quotedCurrency;
    private NotifierRuleOperator operator;
    private double value;

    public CurrencyOperatorValueNotifierRule(){
        this.account = null;
        this.baseCurrency = NotifierCurrency.UNKNOWN;
        this.quotedCurrency = NotifierCurrency.UNKNOWN;
        this.operator = NotifierRuleOperator.UNKNOWN;
        this.value = -1;
    }

    @Override
    public void loadFromJson(String json) {
        try {
            JSONObject jsonObj = new JSONObject(json);
            this.account = new Account(jsonObj.getString("account_name"),jsonObj.getString("account_id"));
            this.baseCurrency = NotifierCurrency.valueOf(jsonObj.getString("baseCurrency"));
            this.quotedCurrency = NotifierCurrency.valueOf(jsonObj.getString("quotedCurrency"));
            this.operator = NotifierRuleOperator.fromSymbol(jsonObj.getString("operator"));
            this.value = jsonObj.getDouble("value");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String toJson(){
        JSONObject json = new JSONObject();
        try {
            json.put("type", NotifierRuleFactory.getNotifierRuleTypeFromNotifierRule(this));
            json.put("account_name", this.account.getName());
            json.put("account_id", this.account.getId());
            json.put("baseCurrency", this.baseCurrency.toString());
            json.put("quotedCurrency", this.quotedCurrency.toString());
            json.put("operator", this.operator.toString());
            json.put("value", this.value);
            return json.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void setAccount(Account account){
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }

    public void setBaseCurrency(NotifierCurrency currency){
        this.baseCurrency = currency;
    }

    public void setQuotedCurrency(NotifierCurrency currency){
        this.quotedCurrency = currency;
    }

    public void setOperator(NotifierRuleOperator operator){
        this.operator = operator;
    }

    public void setValue(double value){
        this.value = value;
    }



    @Override
    public boolean evaluate() throws InvalidValuesException{
        AccountBalance balance = SharedDataCentral.getAccountBalance(this.account.getId());
        Asset baseAsset = SharedDataCentral.getAsset(this.baseCurrency.getName());
        Asset quotedAsset = SharedDataCentral.getAsset(this.quotedCurrency.getName());
        boolean result = false;

        if(balance.isValid() && baseAsset.isValid() && quotedAsset.isValid()){
            AssetEquivalentRate equivalentRate = SharedDataCentral.getEquivalentRate(baseAsset.getSymbol(),quotedAsset.getSymbol());

            if(equivalentRate.isValid()) {
                double baseBalancePrecise = balance.getAssetBalance(baseAsset.getId());
                double quotedBalance = baseBalancePrecise * equivalentRate.getValue();

                switch (this.operator) {
                    case LESS_THAN:
                        result = (quotedBalance < this.value);
                        break;
                        //return result;
                    case BIGGER_THAN:
                        result = (quotedBalance > this.value);
                        //return result;
                        break;
                }

            }else{
                throw new InvalidValuesException("Equivalents Value invalid");
            }

        }else{
            throw new InvalidValuesException("Account Balance or Assets invalids");
        }

        return result;
    }

    @Override
    public boolean isValid() {
        if (this.account == null) {
            return false;
        } else {
            if ((this.account.getId() == null) || (this.account.getId().equals(""))) {
                return false;
            }
        }

        return this.baseCurrency != NotifierCurrency.UNKNOWN
                && this.quotedCurrency != NotifierCurrency.UNKNOWN
                && this.operator != NotifierRuleOperator.UNKNOWN && this.value > 0;

    }

    @Override
    public String toHumanReadableString() {
        return "Alert will fire when "
                +this.baseCurrency.getName()
                +" from the account \""+this.account.getName()+"\" "
                +(this.operator == NotifierRuleOperator.LESS_THAN?"reachs lower values than":"reachs higher values than")+" "
                +this.value+" "+this.quotedCurrency.getName();
    }

    @Override
    public String triggerText() {
        return this.account.getName()+"'s "+ this.baseCurrency.getName()+ " "+
                (this.operator == NotifierRuleOperator.LESS_THAN?"low ":"high ")+" ";
                //+this.value+" "+this.quotedCurrency.getName();
    }

    @Override
    public ArrayList<ApiFunction> askData() {
        ArrayList<ApiFunction> apiFunctions = new ArrayList<>();

        AccountBalance balance = SharedDataCentral.getAccountBalance(this.account.getId());
        if(!balance.isValid()){
            apiFunctions.add(balance.getUpdateFunction());
        }

        Asset base = SharedDataCentral.getAsset(this.baseCurrency.getName());
        if(!base.isValid()){
            apiFunctions.add(base.getUpdateFunction());
        }

        Asset quoted = SharedDataCentral.getAsset(this.quotedCurrency.getName());
        if(!quoted.isValid()){
            apiFunctions.add(quoted.getUpdateFunction());
        }

        AssetEquivalentRate equivalentRate = SharedDataCentral.getEquivalentRate(base.getSymbol(),quoted.getSymbol());
        if(!equivalentRate.isValid()){
            apiFunctions.add(equivalentRate.getUpdateFunction());
        }

        return apiFunctions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CurrencyOperatorValueNotifierRule that = (CurrencyOperatorValueNotifierRule) o;

        if (Double.compare(that.value, value) != 0) return false;
        if (!account.equals(that.account)) return false;
        if (baseCurrency != that.baseCurrency) return false;
        if (quotedCurrency != that.quotedCurrency) return false;
        return operator == that.operator;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = account.hashCode();
        result = 31 * result + baseCurrency.hashCode();
        result = 31 * result + quotedCurrency.hashCode();
        result = 31 * result + operator.hashCode();
        temp = Double.doubleToLongBits(value);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
