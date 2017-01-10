package bo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by javier on 09/01/2017.
 */

public class NotifierRuleFactory {

    public static NotifierRule importFromJson(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            String type = jsonObject.getString("type");
            NotifierRuleType nrt = NotifierRuleType.fromAbbreviation(type);

            switch(nrt){
                case CURRENCY_OPERATOR_VALUE:
                    CurrencyOperatorValueNotifierRule rule = new CurrencyOperatorValueNotifierRule();
                    rule.loadFromJson(json);
                    return rule;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static NotifierRuleType getNotifierRuleTypeFromNotifierRule(NotifierRule notifierRule){
        if (notifierRule instanceof CurrencyOperatorValueNotifierRule){
            return NotifierRuleType.CURRENCY_OPERATOR_VALUE;
        } else {
            return NotifierRuleType.UNKNOWN;
        }
    }
}
