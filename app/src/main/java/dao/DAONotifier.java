package dao;

import bo.Notifier;

/**
 * Created by javier on 03/01/2017.
 */

public interface DAONotifier extends DAO<Notifier>{
    public boolean insertNotifier(Notifier notifier);

    public boolean removeNotifier(Notifier notifier);

    public DAOEnumeration<DAO<Notifier>, Notifier> getNotifiers(int start, int howMany);
}
