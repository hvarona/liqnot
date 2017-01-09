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
    public ArrayList<String> assetIds;

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
        if(this.assetIds != null) {
            answer.add(this.assetIds);
        }
        return answer;
    }

    @Override
    public void onResponse(JSONObject response) {
        System.out.println("<<< "+response.toString());
    }
}
