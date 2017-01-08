package bo;

import java.util.UUID;

import static java.util.UUID.*;

/**
 * Created by javier on 03/01/2017.
 */

public class Notifier extends BO{

    String id;
    NotifierRule rule;

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
}
