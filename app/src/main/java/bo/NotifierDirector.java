package bo;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.henja.liqnot.R;
import com.henja.liqnot.ws.ApiCalls;
import com.henja.liqnot.ws.GetAssetList;
import com.henja.liqnot.ws.ApiFunction;
import com.henja.liqnot.ws.WebsocketWorkerThread;

import java.util.ArrayList;

import dao.DAO;
import dao.DAOAccount;
import dao.DAOAsset;
import dao.DAOEnumeration;
import dao.DAOFactory;
import dao.DAONotifier;
import dao.sqlite.DAOFactorySQLite;

/**
 *
 * Created by javier on 08/01/2017.
 */

public class NotifierDirector {

    private ArrayList<Notifier> notifiers;
    private Context context;
    private ArrayList<NotifierDirectorListener> listeners;
    private DAOFactorySQLite db;

    public NotifierDirector(Context context){
        this.notifiers = new ArrayList<>();
        this.listeners = new ArrayList<>();
        this.context = context;
        this.db = DAOFactory.getSQLiteFactory(this.context);
        DAOAsset daoAsset = this.db.getAssetDAO();
        DAOEnumeration<DAO<Asset>, Asset> assets = daoAsset.getAsset(0,-1);
        System.out.println("Assets count : " + assets.count());
        if(assets.count()<=0){
            GetAssetList.getAllAssets(this);
        }else {
            Asset nextAsset;
            while (assets.hasNext()) {
                nextAsset = assets.next();
                SharedDataCentral.putAsset(nextAsset);
            }
        }

        DAOAccount daoAccount = this.db.getAccountDAO();
        DAOEnumeration<DAO<Account>,Account> accounts = daoAccount.getAccount(0,-1);

        Account nextAccount;
        while(accounts.hasNext()){
            nextAccount = accounts.next();
            SharedDataCentral.putAccount(nextAccount);
        }

        DAONotifier daoNotifier = this.db.getNotifierDAO();
        DAOEnumeration<DAO<Notifier>, Notifier> notifiers = daoNotifier.getNotifiers(0,-1);

        Notifier nextNotifier;
        while(notifiers.hasNext()){
            nextNotifier = notifiers.next();
            this.notifiers.add(nextNotifier);
        }
    }

    public void addNotifier(Notifier notifier){
        DAONotifier daoNotifier = this.db.getNotifierDAO();

        if (daoNotifier.insertNotifier(notifier)) {
            this.notifiers.add(notifier);
            tellNewNotifierToListeners(notifier);
        } else {
            System.err.println("error adding notifier");
            //TODO should throw an error
        }
    }

    public void addAsset(Asset asset){
        DAOAsset daoAsset = this.db.getAssetDAO();
        if(!daoAsset.insertAsset(asset))
            System.err.println("error inserting asset "); //TODO handle error
    }

    private void tellNewNotifierToListeners(Notifier notifier){
        for (NotifierDirectorListener listener: this.listeners){
            listener.OnNewNotifier(notifier);
        }
    }

    public void addAccount(Account account){
        DAOAccount daoAccount = this.db.getAccountDAO();
        daoAccount.insertAccount(account);//TODO handle error
    }

    public void execute(){
        ApiCalls apiCalls = new ApiCalls();
        for(Notifier not : notifiers){
            NotifierRule notifierRule = not.getRule();
            apiCalls.addFunctions(notifierRule.askData());
        }
        if(apiCalls.hasFunctions()) {
            apiCalls.addListener(new ApiCalls.ApiCallsListener() {
                @Override
                public void OnAllDataReceived() {
                    evaluateAllNotifiers();
                }

                @Override
                public void OnError(ApiFunction errorFunction) {

                }

                @Override
                public void OnConnectError() {

                }
            });
            WebsocketWorkerThread wsthread = new WebsocketWorkerThread(apiCalls);
            wsthread.start();
        }
    }

    private void evaluateAllNotifiers(){
        NotificationManager NM = (NotificationManager)
                this.context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this.context);
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        notificationBuilder.setContentTitle("LiqNot Notification");

        for(Notifier not : notifiers){
            if (not.getRule().evaluate()){
                notificationBuilder.setContentText(not.getRule().triggerText());
                Notification notification = notificationBuilder.build();
                NM.notify(0, notification);
                //TODO TRIGGER ALARM OR NOTIFICATION TO THE USER!!!
            }
        }
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
