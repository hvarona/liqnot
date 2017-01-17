package bo;

import com.henja.liqnot.ws.ApiFunction;

import java.util.ArrayList;

/**
 *
 * Created by javier on 04/01/2017.
 */
public abstract class NotifierRule {

    public abstract void loadFromJson(String json);

    public abstract String toJson();

    public abstract boolean evaluate() throws InvalidValuesException;

    public abstract boolean isValid();

    public abstract String toHumanReadableString();

    public abstract String triggerText();

    public abstract ArrayList<ApiFunction> askData();

    public abstract NotifierRule clone();
}
