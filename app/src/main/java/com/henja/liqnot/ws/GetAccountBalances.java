package com.henja.liqnot.ws;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import bo.AccountBalance;
import bo.Asset;
import bo.SharedDataCentral;

/**
 *
 * Created by henry on 08/01/2017.
 */

public class GetAccountBalances implements ApiFunction {
    private String accountId;
    private ArrayList<String> assetIds =new ArrayList<>();

    public GetAccountBalances(String accountId) {
        this.accountId = accountId;
    }

    public GetAccountBalances(String accountId, ArrayList<String> assetIds) {
        this.accountId = accountId;
        this.assetIds = assetIds;
    }

    @Override
    public int getApiID() {
        return 0;
    }

    @Override
    public String getMethodToCall() {
        return "get_account_balances";
    }

    @Override
    public String getMethod() {
        return "call";
    }

    @Override
    public List<Serializable> getParams() {
        ArrayList<Serializable> answer = new ArrayList<>();
        answer.add(this.accountId);
        answer.add(this.assetIds);
        return answer;
    }

    @Override
    public void onResponse(JSONArray response) {
        System.out.println("GetAccount Balance <<< "+response.toString());
        AccountBalance balance = SharedDataCentral.getAccountBalance(accountId);
        HashMap<String,Double> balances = new HashMap<>();
        for(int i = 0; i < response.length();i++) {
            JSONObject eqObject;
            try {
                eqObject = (JSONObject) response.get(i);
                Asset asset = SharedDataCentral.getAssetByID(eqObject.get("asset_id").toString());
                if(asset.isValid()) {
                    balances.put(eqObject.get("asset_id").toString(), Double.parseDouble(eqObject.get("amount").toString())*Math.pow(10,-(asset.getPrecision())));
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
        balance.setBalances(balances);
    }

    @Override
    public void onResponse(JSONObject response) {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GetAccountBalances that = (GetAccountBalances) o;

        return accountId.equals(that.accountId)
                && (assetIds != null ? assetIds.equals(that.assetIds) : that.assetIds == null);

    }

    @Override
    public int hashCode() {
        int result = accountId.hashCode();
        result = 31 * result + (assetIds != null ? assetIds.hashCode() : 0);
        return result;
    }
}
