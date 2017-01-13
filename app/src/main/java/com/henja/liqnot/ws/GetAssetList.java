package com.henja.liqnot.ws;

import com.henja.liqnot.app.LiqNotApp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import bo.Asset;
import bo.NotifierDirector;
import bo.SharedDataCentral;

/**
 *
 * Created by henry on 11/01/2017.
 */

public class GetAssetList implements ApiFunction {
    private String startingAssetSymbol;
    private NotifierDirector director;

    public GetAssetList(String startingAssetSymbol,NotifierDirector director) {
        this.startingAssetSymbol = startingAssetSymbol;
        this.director = director;
    }

    public static void getAllAssets(NotifierDirector director){
        ApiCalls apiCalls = new ApiCalls();
        apiCalls.addFunction(new GetAssetList("",director));
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
        ArrayList<Serializable> params = new ArrayList<>();
        params.add(startingAssetSymbol);
        params.add("100");
        return params;
    }

    @Override
    public void onResponse(JSONArray response) {
        System.out.println("En getAssetList con " + startingAssetSymbol);
        String finalAsset = "";
        for(int i = 0; i < response.length();i++) {
            JSONObject eqObject;
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
                if(director != null) {
                    director.addAsset(asset);
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        if(response.length()>= 100 && !finalAsset.isEmpty()){
            ApiCalls apiCalls = new ApiCalls();
            apiCalls.addFunction(new GetAssetList(finalAsset,this.director));
            WebsocketWorkerThread thread = new WebsocketWorkerThread(apiCalls);
            thread.start();
        }
    }

    @Override
    public void onResponse(JSONObject response) {

    }
}
