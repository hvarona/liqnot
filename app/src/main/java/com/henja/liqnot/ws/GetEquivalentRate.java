package com.henja.liqnot.ws;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import bo.Asset;

/**
 * Created by henry on 08/01/2017.
 */

public class GetEquivalentRate implements ApiFunction {

    private Asset base;
    private Asset quote;

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
    public void onResponse(JSONObject response){
        System.out.println("<<< "+response.toString());
        /*try {
            String response = frame.getPayloadText();
            GsonBuilder builder = new GsonBuilder();

            Type GetLimitOrdersResponse = new TypeToken<WitnessResponse<List<LimitOrder>>>() {}.getType();
            builder.registerTypeAdapter(AssetAmount.class, new AssetAmount.AssetDeserializer());
            builder.registerTypeAdapter(UserAccount.class, new UserAccount.UserAccountSimpleDeserializer());
            WitnessResponse<List<LimitOrder>> witnessResponse = builder.create().fromJson(response, GetLimitOrdersResponse);
            if (witnessResponse.error != null) {
                this.mListener.onError(witnessResponse.error);
            } else {
                this.mListener.onSuccess(witnessResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }
}
