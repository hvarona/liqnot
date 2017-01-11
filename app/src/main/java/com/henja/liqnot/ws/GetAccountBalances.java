package com.henja.liqnot.ws;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by henry on 08/01/2017.
 */

public class GetAccountBalances implements ApiFunction {
    public String accountId;
    public ArrayList<String> assetIds =new ArrayList();

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
    public void onResponse(JSONObject response) {
        System.out.println("GetAccount Balance <<< "+response.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GetAccountBalances that = (GetAccountBalances) o;

        if (!accountId.equals(that.accountId)) return false;
        return assetIds != null ? assetIds.equals(that.assetIds) : that.assetIds == null;

    }

    @Override
    public int hashCode() {
        int result = accountId.hashCode();
        result = 31 * result + (assetIds != null ? assetIds.hashCode() : 0);
        return result;
    }
}
