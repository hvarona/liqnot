package bo;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.henja.liqnot.R;
import com.henja.liqnot.ws.ApiCalls;
import com.henja.liqnot.ws.ConnectionException;
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
    private boolean processingAssets = false;

    private static final long NOTIFIES_TIMEOUT = 86400000;

    public NotifierDirector(Context context){
        this.notifiers = new ArrayList<>();
        this.listeners = new ArrayList<>();
        this.context = context;
        this.db = DAOFactory.getSQLiteFactory(this.context);
        checkAssetList();

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

    private boolean checkAssetList(){
        if(SharedDataCentral.getAssetsList().size()<=0){
            DAOAsset daoAsset = this.db.getAssetDAO();
            DAOEnumeration<DAO<Asset>, Asset> assets = daoAsset.getAsset(0,-1);
            System.out.println("Assets count : " + assets.count());
            if(assets.count()<=0){
                if(!processingAssets) {
                    processingAssets = true;
                    try {
                        GetAssetList.getAllAssets(this, new ApiCalls.ApiCallsListener() {
                            @Override
                            public void OnAllDataReceived() {
                                //TODO enable add notifier button
                                processingAssets = false;
                                tellAssetsLoadedToListeners();
                            }

                            @Override
                            public void OnError(ApiFunction errorFunction) {
                                processingAssets = false;
                            }

                            @Override
                            public void OnConnectError() {
                                processingAssets = false;
                            }
                        });
                    } catch (ConnectionException e) {
                        e.printStackTrace();
                        processingAssets = false;
                        //TODO no hay conexion
                    }
                }
            }else {
                Asset nextAsset;
                while (assets.hasNext()) {
                    nextAsset = assets.next();
                    SharedDataCentral.putAsset(nextAsset);
                }
            }
            return false;
        }else{
            return true;
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

    public void removeNotifier(Notifier notifier){
        DAONotifier daoNotifier = this.db.getNotifierDAO();

        if (daoNotifier.removeNotifier(notifier)) {
            this.notifiers.remove(notifier);
            tellNotifierRemovedToListeners(notifier);
        } else {
            System.err.println("error deleting notifier");
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

    private void tellNotifierRemovedToListeners(Notifier notifier){
        for (NotifierDirectorListener listener: this.listeners){
            listener.OnNotifierRemoved(notifier);
        }
    }

    private void tellAssetsLoadedToListeners(){
        for (NotifierDirectorListener listener: this.listeners){
            listener.OnAssetsLoaded();
        }
    }

    public void addAccount(Account account){
        DAOAccount daoAccount = this.db.getAccountDAO();
        daoAccount.insertAccount(account);//TODO handle error
    }

    public void execute(){
        if(checkAssetList()) {
            ApiCalls apiCalls = new ApiCalls();
            for (Notifier not : notifiers) {
                NotifierRule notifierRule = not.getRule();
                apiCalls.addFunctions(notifierRule.askData());
            }
            if (apiCalls.hasFunctions()) {
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
                WebsocketWorkerThread wsthread = null;
                try {
                    wsthread = new WebsocketWorkerThread(apiCalls);
                    wsthread.start();
                } catch (ConnectionException e) {
                    e.printStackTrace();
                    //TODO no hay conexion
                }
            }
        }
    }

    private void evaluateAllNotifiers(){
        NotificationManager NM = (NotificationManager)
                this.context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this.context);
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        notificationBuilder.setContentTitle("LiqNot Notification");

        for(Notifier not : notifiers){
            try {
                if (not.getRule().evaluate()) {
                    if(!not.isActive()
                            || (System.currentTimeMillis() - not.getLastNotifyDate().getTime()) >= NOTIFIES_TIMEOUT) {
                        not.setActive();
                        notificationBuilder.setContentText(not.getRule().triggerText());
                        Notification notification = notificationBuilder.build();
                        NM.notify(not.hashCode(), notification);
                        //TODO TRIGGER ALARM OR NOTIFICATION TO THE USER!!!
                    }

                }else{
                    not.setInactive();
                    NM.cancel(not.hashCode());
                    //TODO Notificitation stop
                }
            }catch(InvalidValuesException e){
                e.printStackTrace();
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

        public void OnNotifierRemoved(Notifier notifier);

        public void OnAssetsLoaded();
    }

    public Context getContext() {
        return context;
    }

    public ArrayList<Notifier> getNotifiers(){
        return this.notifiers;
    }
}
