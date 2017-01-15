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

    public void setRule(NotifierRule rule){
        this.rule = rule;
    }

    public void setRuleJson(String json){
        this.rule = rule;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Notifier notifier = (Notifier) o;

        if (!id.equals(notifier.id)) return false;
        return rule != null ? rule.equals(notifier.rule) : notifier.rule == null;

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (rule != null ? rule.hashCode() : 0);
        return result;
    }

    public boolean isActive() {
        return isActive;
    }

    public Date getLastNotifyDate() {
        return lastNotifyDate;
    }

    public void setActive(){
        isActive = true;
        lastNotifyDate = new Date();
    }

    public void setInactive(){
        isActive = false;
    }

}
