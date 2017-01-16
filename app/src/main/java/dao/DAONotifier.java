package dao;

import bo.Notifier;

/**
 *
 * Created by javier on 03/01/2017.
 */

public interface DAONotifier extends DAO<Notifier>{
    boolean insertNotifier(Notifier notifier);
    boolean removeNotifier(Notifier notifier);
    DAOEnumeration<DAO<Notifier>, Notifier> getNotifiers(int start, int howMany);
}
