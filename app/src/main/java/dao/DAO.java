package dao;

import bo.BO;

/**
 *
 * Created by javier on 03/01/2017.
 */

public interface DAO<X extends BO> {
    X rowToObject(Object row);
}
