package bo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by javier on 04/01/2017.
 */

public class CurrencyOperatorValueNotifierRule extends NotifierRule {

    NotifierCurrency currency;
    NotifierRuleOperator operator;
    int value;

    @Override
    public void loadFromJson(String json) {
        try {
            JSONObject jsonObj = new JSONObject(json);
            this.currency = NotifierCurrency.valueOf(jsonObj.getString("currency"));
            this.operator = NotifierRuleOperator.valueOf(jsonObj.getString("operator"));
            this.value = jsonObj.getInt("value");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String toJson(){
        JSONObject json = new JSONObject();
        try {
            json.put("currency", this.currency.toString());
            json.put("operator", this.operator.toString());
            json.put("value", this.value);
            return json.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean evaluate() {
        return false;
    }



}
