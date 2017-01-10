package bo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by javier on 04/01/2017.
 */

public class CurrencyOperatorValueNotifierRule extends NotifierRule {

    //Account account;
    NotifierCurrency currency;
    NotifierRuleOperator operator;
    double value;

    public CurrencyOperatorValueNotifierRule(){
        this.currency = NotifierCurrency.UNKNOWN;
        this.operator = NotifierRuleOperator.UNKNOWN;
        this.value = -1;
    }

    @Override
    public void loadFromJson(String json) {
        try {
            JSONObject jsonObj = new JSONObject(json);
            this.currency = NotifierCurrency.valueOf(jsonObj.getString("currency"));
            this.operator = NotifierRuleOperator.valueOf(jsonObj.getString("operator"));
            this.value = jsonObj.getDouble("value");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String toJson(){
        JSONObject json = new JSONObject();
        try {
            json.put("type", NotifierRuleFactory.getNotifierRuleTypeFromNotifierRule(this));
            json.put("currency", this.currency.toString());
            json.put("operator", this.operator.toString());
            json.put("value", this.value);
            return json.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void setCurrency(NotifierCurrency currency){
        this.currency = currency;
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
        if (this.currency == NotifierCurrency.UNKNOWN){
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
                +this.currency.getName()+" "
                +(this.operator == NotifierRuleOperator.LESS_THAN?"reachs lower values than":"reachs higher values than")+" "
                +this.value;
    }


}
