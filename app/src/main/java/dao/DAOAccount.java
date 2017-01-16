package dao;

import bo.Account;

/**
 *
 * Created by henry on 12/01/2017.
 */

public interface DAOAccount extends DAO<Account> {
    boolean insertAccount(Account account);

    DAOEnumeration<DAO<Account>, Account> getAccount(int start, int howMany);
    Account getAccountByName(String accountName);
}
