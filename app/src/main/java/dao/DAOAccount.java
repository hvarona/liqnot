package dao;

import bo.Account;

/**
 * Created by henry on 12/01/2017.
 */

public interface DAOAccount extends DAO<Account> {
    public boolean insertAccount(Account account);

    public DAOEnumeration<DAO<Account>, Account> getAccount(int start, int howMany);
    public Account getAccountByName(String accountName);
}
