package bo;

import java.util.Date;
import java.util.UUID;

/**
 *
 * Created by javier on 03/01/2017.
 */

public class Notifier extends BO{

    private String id;
    private NotifierRule rule;
    private NotifierRule pendingRule;
    private boolean isActive = false;
    private Date lastNotifyDate;

    public Notifier(String id){
        if (id.equals("")) {
            this.id = UUID.randomUUID().toString();
        } else {
            this.id = id;
        }
    }

    public String getId(){
        return this.id;
    }

    public NotifierRule getRule(){
        return this.rule;
    }

    public NotifierRule getPendingRule(){
        return this.pendingRule;
    }

    public void setRule(NotifierRule rule){
        this.rule = rule;
    }

    public void setPendingRule(NotifierRule rule){
        this.pendingRule = rule;
    }

    public void setRuleJson(String json){
        this.rule = rule;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Notifier notifier = (Notifier) o;

        return id.equals(notifier.id) && (rule != null ? rule.equals(notifier.rule) : notifier.rule == null);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (rule != null ? rule.hashCode() : 0);
        return result;
    }

    boolean isActive() {
        return isActive;
    }

    Date getLastNotifyDate() {
        return lastNotifyDate;
    }

    void setActive(){
        isActive = true;
        lastNotifyDate = new Date();
    }

    void setInactive(){
        isActive = false;
    }

    void resolvePending(){
        this.rule = this.pendingRule;
    }

}
