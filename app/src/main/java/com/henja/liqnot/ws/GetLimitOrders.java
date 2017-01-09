package com.henja.liqnot.ws;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFrame;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by henry on 08/01/2017.
 */

public class GetLimitOrders implements ApiFunction {

    private String a;
    private String b;
    private int limit;
    //private WitnessResponseListener mListener;

    public GetLimitOrders(String a, String b, int limit/*, WitnessResponseListener mListener*/) {
        this.a = a;
        this.b = b;
        this.limit = limit;
        //this.mListener = mListener;
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
        accountParams.add(this.a);
        accountParams.add(this.b);
        accountParams.add(this.limit);
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
