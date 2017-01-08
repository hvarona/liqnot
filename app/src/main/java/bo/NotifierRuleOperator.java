package bo;

/**
 * Created by javier on 04/01/2017.
 */
public enum NotifierRuleOperator {
    LESS_THAN("<"),
    BIGGER_THAN(">");

    private final String symbol;
    NotifierRuleOperator(String symbol){
        this.symbol = symbol;
    }

    public String toString(){
        return this.symbol;
    }
}
