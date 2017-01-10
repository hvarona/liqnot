package com.henja.liqnot.ws;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by henry on 09/01/2017.
 */

public class GetAsset implements ApiFunction {

    public String assetName;

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
    public void onResponse(JSONObject response) {
        System.out.println("GetAsset <<< "+response.toString());
    }
}
