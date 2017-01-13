package bo;

import com.henja.liqnot.ws.ApiFunction;
import com.henja.liqnot.ws.GetAccountInfo;

/**
 * Account Bussiness Object, holds the name and the id of a BitShare Account
 *
 * Created by javier on 10/01/2017.
 */

public class Account extends BO {
    private String name;
    private String id;

    public Account(String name){
        this.name = name;
        this.id = "";
    }

    public Account(String name, String id){
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (!name.equals(this.name)){
            if (!this.name.equals("")) {//This is to prevent an incorrect name/id pair
                this.id = "";
            }
            this.name = name;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (!id.equals(this.id)){
            if (!this.id.equals("")) {//This is to prevent an incorrect name/id pair
                this.name = "";
            }
            this.id = id;
        }
    }

    public ApiFunction getUpdateFunction(){
        return new GetAccountInfo(this.name);
    }
}
