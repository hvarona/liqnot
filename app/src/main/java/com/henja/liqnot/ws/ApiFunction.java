package com.henja.liqnot.ws;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketFrame;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 *
 * Created by henry on 08/01/2017.
 */

public interface ApiFunction {
    int getApiID();
    String getMethodToCall();
    String getMethod();
    List<Serializable> getParams();
    void onResponse(JSONArray response);
    void onResponse(JSONObject response);
}
