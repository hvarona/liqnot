package bo;

/**
 * Created by javier on 04/01/2017.
 */
public enum NotifierRuleOperator {
    UNKNOWN("UNKNOWN"),
    LESS_THAN("<"),
    BIGGER_THAN(">");

    private final String symbol;
    NotifierRuleOperator(String symbol){
        this.symbol = symbol;
    }

    public String toString(){
        return this.symbol;
    }

    public static NotifierRuleOperator fromSymbol(String symbol) {
        if (symbol != null) {
            for (NotifierRuleOperator nro : NotifierRuleOperator.values()) {
                if (symbol.equalsIgnoreCase(nro.symbol)) {
                    return nro;
                }
            }
        }
        return null;
    }
}
