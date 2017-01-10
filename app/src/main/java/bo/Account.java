package bo;

/**
 * Created by javier on 10/01/2017.
 */

public class Account {
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
        if (name != this.name){
            if (this.name != "") {//This is to prevent an incorrect name/id pair
                this.id = "";
            }
            this.name = name;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (id != this.id){
            if (this.id != "") {//This is to prevent an incorrect name/id pair
                this.name = "";
            }
            this.id = id;
        }
    }
}
