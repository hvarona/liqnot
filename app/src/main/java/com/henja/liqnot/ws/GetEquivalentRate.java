package com.henja.liqnot.ws;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import bo.Asset;
import bo.AssetEquivalentRate;
import bo.SharedDataCentral;

/**
 *
 * Created by henry on 08/01/2017.
 */

public class GetEquivalentRate implements ApiFunction {

    private Asset base;
    private Asset quote;

    private final Object SYNC = new Object();

    public GetEquivalentRate(Asset base, Asset quote) {
        this.base = base;
        this.quote = quote;
    }


    @Override
    public int getApiID() {
        return 0;
    }

    @Override
    public String getMethodToCall() {
        return "get_limit_orders";
    }

    @Override
    public String getMethod() {
        return "call";
    }

    @Override
    public List<Serializable> getParams() {
        ArrayList<Serializable> accountParams = new ArrayList<>();
        accountParams.add(this.base.getId());
        accountParams.add(this.quote.getId());
        accountParams.add(1);
        return accountParams;
    }

    @Override
    public void onResponse(JSONArray response){
        for(int i = 0; i < response.length();i++){
            JSONObject eqObject;
            try {
                eqObject = (JSONObject) response.get(i);
                JSONObject sellPriceObject =(JSONObject)eqObject.get("sell_price");
                JSONObject baseObject =(JSONObject)sellPriceObject.get("base");
                JSONObject quoteObject =(JSONObject)sellPriceObject.get("quote");
                if(baseObject.get("asset_id").toString().equals(quote.getId())){
                    double value = Double.parseDouble(quoteObject.get("amount").toString())/Double.parseDouble(baseObject.get("amount").toString());
                    value = 1/value;
                    value = value * Math.pow(10,base.getPrecision()-quote.getPrecision());
                    AssetEquivalentRate eqRate = SharedDataCentral.getEquivalentRate(base.getSymbol(),quote.getSymbol());
                    eqRate.setValue(value);
                    SharedDataCentral.putEquivalentsRate(eqRate);
                    return;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(!base.getType().equalsIgnoreCase("core") && !quote.getType().equalsIgnoreCase("core")){
            System.out.println("Getting indirect rate");
            getRateIndirect();

            synchronized (SYNC){
                try {
                    SYNC.wait(60000);
                } catch (InterruptedException e) {                }
            }
        }
    }

    @Override
    public void onResponse(JSONObject response) {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GetEquivalentRate that = (GetEquivalentRate) o;

        return base.equals(that.base) && quote.equals(that.quote);

    }

    @Override
    public int hashCode() {
        int result = base.hashCode();
        result = 31 * result + quote.hashCode();
        return result;
    }

    public void getRateIndirect(){
        System.out.println("In indirect rate fo " + base.getSymbol() + " to " + quote.getSymbol());
        try {
            ApiCalls apiCalls = new ApiCalls();
            Asset core = SharedDataCentral.getCoreAsset();
            if (!core.isValid()) {
                apiCalls.addFunction(core.getUpdateFunction());
                apiCalls.addListener(new ApiCalls.ApiCallsListener() {
                    @Override
                    public void OnAllDataReceived() {
                        getRateIndirect();
                        synchronized (SYNC) {
                            SYNC.notifyAll();
                        }
                    }

                    @Override
                    public void OnError(ApiFunction errorFunction) {
                        synchronized (SYNC) {
                            SYNC.notifyAll();
                        }
                    }

                    @Override
                    public void OnConnectError() {
                        synchronized (SYNC) {
                            SYNC.notifyAll();
                        }
                    }
                });
                WebsocketWorkerThread thread = new WebsocketWorkerThread(apiCalls);
                thread.start();
            } else {
                AssetEquivalentRate eqRateBase = SharedDataCentral.getEquivalentRate(base.getSymbol(),core.getSymbol());
                if(!eqRateBase.isValid()){
                    apiCalls.addFunction(eqRateBase.getUpdateFunction());
                }
                AssetEquivalentRate eqRateQuoted = SharedDataCentral.getEquivalentRate(core.getSymbol(),quote.getSymbol());
                if(!eqRateQuoted.isValid()){
                    apiCalls.addFunction(eqRateQuoted.getUpdateFunction());
                }

                if(apiCalls.hasFunctions()){
                    apiCalls.addListener(new ApiCalls.ApiCallsListener() {
                        @Override
                        public void OnAllDataReceived() {
                            getRateIndirect();
                            synchronized (SYNC) {
                                SYNC.notifyAll();
                            }
                        }

                        @Override
                        public void OnError(ApiFunction errorFunction) {
                            synchronized (SYNC) {
                                SYNC.notifyAll();
                            }
                        }

                        @Override
                        public void OnConnectError() {
                            synchronized (SYNC) {
                                SYNC.notifyAll();
                            }
                        }
                    });
                    WebsocketWorkerThread thread = new WebsocketWorkerThread(apiCalls);
                    thread.start();
                }else{
                    AssetEquivalentRate eqRate = SharedDataCentral.getEquivalentRate(base.getSymbol(),quote.getSymbol());
                    eqRate.setValue(eqRateBase.getValue()*eqRateQuoted.getValue());
                    SharedDataCentral.putEquivalentsRate(eqRate);
                    synchronized (SYNC) {
                        SYNC.notifyAll();
                    }
                }
            }
        } catch (ConnectionException e) {
            e.printStackTrace();
            synchronized (SYNC) {
                SYNC.notifyAll();
            }
        }
    }
}
