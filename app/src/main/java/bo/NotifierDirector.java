package bo;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.henja.liqnot.ws.ApiCalls;
import com.henja.liqnot.ws.WebsocketWorkerThread;

import java.io.Serializable;
import java.util.ArrayList;

import dao.DAO;
import dao.DAOEnumeration;
import dao.DAOFactory;
import dao.DAONotifier;
import dao.sqlite.DAOFactorySQLite;
import dao.sqlite.DAONotifierEnumerationSQLite;
import dao.sqlite.DAONotifierSQLite;


/**
 * Created by javier on 08/01/2017.
 */

public class NotifierDirector {

    private ArrayList<Notifier> notifiers;
    private Context context;
    private ArrayList<NotifierDirectorListener> listeners;
    private DAOFactorySQLite db;

    public NotifierDirector(Context context){
        this.notifiers = new ArrayList<Notifier>();
        this.listeners = new ArrayList<NotifierDirectorListener>();
        this.context = context;
        this.db = DAOFactory.getSQLiteFactory(this.context);
        DAONotifier daoNotifier = this.db.getNotifierDAO();
        DAOEnumeration<DAO<Notifier>, Notifier> notifiers = daoNotifier.getNotifiers(0,-1);

        Notifier nextNotifier;
        while(notifiers.hasNext()){
            nextNotifier = notifiers.next();
            this.notifiers.add(nextNotifier);
        }
    }

    public void addNotifier(Notifier notifier){
        this.db = DAOFactory.getSQLiteFactory(this.context);
        DAONotifier daoNotifier = this.db.getNotifierDAO();

        if (daoNotifier.insertNotifier(notifier)) {
            this.notifiers.add(notifier);
            tellNewNotifierToListeners(notifier);
        } else {
            //TODO should throw an error
        }
    }

    public void tellNewNotifierToListeners(Notifier notifier){
        for (NotifierDirectorListener listener: this.listeners){
            listener.OnNewNotifier(notifier);
        }
    }

    public void execute(){
        ApiCalls apiCalls = new ApiCalls();
        for(Notifier not : notifiers){
            CurrencyOperatorValueNotifierRule currencyNotifier = (CurrencyOperatorValueNotifierRule) not.getRule();
            String accountID = "1.2.143552"; //currencyNotifier.getAccount().getID();
            AccountBalance balance = SharedDataCentral.getAccountBalance(accountID);
            if(!balance.isValid()){
                apiCalls.addFunction(balance.getUpdateFunction());
            }
            Asset base = SharedDataCentral.getAsset(currencyNotifier.baseCurrency.getName());
            Asset quote = SharedDataCentral.getAsset("USD");
            AssetEquivalentRate equivalentRate = SharedDataCentral.getEquivalentRate(base.getSymbol(),quote.getSymbol());
            if(!equivalentRate.isValid()){
                apiCalls.addFunction(equivalentRate.getUpdateFunction());
            }
        }
        WebsocketWorkerThread wsthread = new WebsocketWorkerThread(apiCalls);
        wsthread.start();
    }

    public void addNotifierDirectorListener(NotifierDirectorListener listener){
        this.listeners.add(listener);
    }

    public void removeNotifierDirectorListener(NotifierDirectorListener listener){
        this.listeners.remove(listener);
    }

    public interface NotifierDirectorListener{
        public void OnNewNotifier(Notifier notifier);
    }

    public ArrayList<Notifier> getNotifiers(){
        return this.notifiers;
    }
}
