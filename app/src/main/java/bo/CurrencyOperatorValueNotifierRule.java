package bo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by javier on 04/01/2017.
 */

public class CurrencyOperatorValueNotifierRule extends NotifierRule {

    Account account;
    NotifierCurrency baseCurrency;
    NotifierCurrency quotedCurrency;
    NotifierRuleOperator operator;
    double value;

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
    public boolean evaluate() {
        return false;
    }

    @Override
    public boolean isValid() {
        if (this.account == null){
            return false;
        }

        if (this.baseCurrency == NotifierCurrency.UNKNOWN){
            return false;
        }

        if (this.quotedCurrency == NotifierCurrency.UNKNOWN){
            return false;
        }

        if (this.operator == NotifierRuleOperator.UNKNOWN){
            return false;
        }

        if (this.value <= 0){
            return false;
        }

        return true;
    }

    @Override
    public String toHumanReadableString() {
        return "Alarm will fire when "
                +this.baseCurrency.getName()
                +" from the account \""+this.account.getName()+"\" "
                +(this.operator == NotifierRuleOperator.LESS_THAN?"reachs lower values than":"reachs higher values than")+" "
                +this.value
                +" in "+this.quotedCurrency.getName();
    }


}
