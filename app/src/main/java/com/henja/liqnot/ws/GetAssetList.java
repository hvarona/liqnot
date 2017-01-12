package com.henja.liqnot.ws;

import com.henja.liqnot.app.LiqNotApp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import bo.Asset;
import bo.SharedDataCentral;

/**
 * Created by henry on 11/01/2017.
 */

public class GetAssetList implements ApiFunction {
    private String startingAssetSymbol;

    public GetAssetList(String startingAssetSymbol) {
        this.startingAssetSymbol = startingAssetSymbol;
    }

    public static void getAllAssets(){
        ApiCalls apiCalls = new ApiCalls();
        apiCalls.addFunction(new GetAssetList(""));
        WebsocketWorkerThread thread = new WebsocketWorkerThread(apiCalls);
        thread.start();
    }

    @Override
    public int getApiID() {
        return 0;
    }

    @Override
    public String getMethodToCall() {
        return "list_assets";
    }

    @Override
    public String getMethod() {
        return "call";
    }

    @Override
    public List<Serializable> getParams() {
        ArrayList<Serializable> params = new ArrayList();
        params.add(startingAssetSymbol);
        params.add("100");
        return params;
    }

    @Override
    public void onResponse(JSONArray response) {
        String finalAsset = "";
        for(int i = 0; i < response.length();i++) {
            JSONObject eqObject = null;
            try {
                eqObject = (JSONObject) response.get(i);
                String id = eqObject.get("id").toString();
                String symbol = eqObject.get("symbol").toString();
                int precision = Integer.parseInt(eqObject.get("precision").toString());
                String type = "UIA";
                if(symbol.equals("BTS")){
                    type="core";
                }else if(LiqNotApp.SMARTCOINS.contains(symbol)){
                    type="smatcoin";
                }
                Asset asset = new Asset(id,symbol,precision,type);
                finalAsset = asset.getSymbol();
                SharedDataCentral.putAsset(asset);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        if(response.length()>= 100 && !finalAsset.isEmpty()){
            ApiCalls apiCalls = new ApiCalls();
            apiCalls.addFunction(new GetAssetList(finalAsset));
            WebsocketWorkerThread thread = new WebsocketWorkerThread(apiCalls);
            thread.start();
        }
    }
}
