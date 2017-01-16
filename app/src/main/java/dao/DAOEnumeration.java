package dao;

import bo.BO;

/**
 *
 * Created by javier on 03/01/2017.
 */
public abstract class DAOEnumeration<X extends DAO<Y>, Y extends BO> {

    public X dao;
    public int start;
    public int howMany;
    protected Object resultHandler;

    public DAOEnumeration(X dao, Object resultHandler, int start, int howMany){
        this.dao = dao;
        this.start = start;
        this.howMany = howMany;
        this.resultHandler = resultHandler;
    }

    /*public boolean hasNext(){
        this.dao.
    }*/

    public abstract int count();

    public Y next(){
        return this.dao.rowToObject(resultHandler);
    }

    public abstract boolean hasNext();
}
