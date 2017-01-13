package com.henja.liqnot.ws;

import com.henja.liqnot.app.LiqNotApp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import bo.Account;
import bo.Asset;
import bo.SharedDataCentral;

/**
 * Created by henry on 11/01/2017.
 */

public class GetAccountInfo implements ApiFunction {

    private String accountId;

    public GetAccountInfo(String accountId) {
        this.accountId = accountId;
    }

    @Override
    public int getApiID() {
        return 0;
    }

    @Override
    public String getMethodToCall() {
        return "get_account_by_name";
    }

    @Override
    public String getMethod() {
        return "call";
    }

    @Override
    public List<Serializable> getParams() {
        ArrayList<Serializable> params = new ArrayList();
        params.add(accountId);
        return params;
    }

    @Override
    public void onResponse(JSONArray response) {

    }

    @Override
    public void onResponse(JSONObject response) {
        try {
            String id = response.get("id").toString();
            String name = response.get("name").toString();

            Account account = new Account(name,id);
            SharedDataCentral.putAccount(account);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}
