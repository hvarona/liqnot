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
 *
 * Created by henry on 09/01/2017.
 */

public class GetAsset implements ApiFunction {

    private String assetName;

    public GetAsset(String assetName) {
        this.assetName = assetName;
    }

    @Override
    public int getApiID() {
        return 0;
    }

    @Override
    public String getMethodToCall() {
        return "lookup_asset_symbols";
    }

    @Override
    public String getMethod() {
        return "call";
    }

    @Override
    public List<Serializable> getParams() {
        ArrayList<Serializable> params = new ArrayList<>();
        ArrayList<String> subArray = new ArrayList<>();
        subArray.add(assetName);
        params.add(subArray);
        return params;
    }

    @Override
    public void onResponse(JSONArray response) {
        System.out.println("GetAsset <<< "+response.toString());
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
                    type="SMARTCOIN";
                }
                Asset asset = new Asset(id,symbol,precision,type);
                SharedDataCentral.putAsset(asset);
            }catch (JSONException e){
                e.printStackTrace();
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

        GetAsset getAsset = (GetAsset) o;

        return assetName.equals(getAsset.assetName);

    }

    @Override
    public int hashCode() {
        return assetName.hashCode();
    }
}
