package bo;

/**
 * Created by javier on 09/01/2017.
 */

public enum NotifierRuleType {
    UNKNOWN("UNKNOWN"),
    CURRENCY_OPERATOR_VALUE("CUR_OP_VAL");

    private final String abbreviation;
    NotifierRuleType(String abbreviation){
        this.abbreviation = abbreviation;
    }

    public String getAbbreviation(){
        return this.abbreviation;
    }

    public String toString(){
        return this.abbreviation;
    }

    public static NotifierRuleType fromAbbreviation(String abbreviation) {
        if (abbreviation != null) {
            for (NotifierRuleType nrt : NotifierRuleType.values()) {
                if (abbreviation.equalsIgnoreCase(nrt.abbreviation)) {
                    return nrt;
                }
            }
        }
        return null;
    }
}
