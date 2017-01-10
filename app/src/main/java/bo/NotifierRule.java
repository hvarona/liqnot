package bo;

import org.json.JSONException;

/**
 * Created by javier on 04/01/2017.
 */
public abstract class NotifierRule {

    public abstract void loadFromJson(String json);

    public abstract String toJson();

    public abstract boolean evaluate();

    public abstract boolean isValid();

    public abstract String toHumanReadableString();
}
