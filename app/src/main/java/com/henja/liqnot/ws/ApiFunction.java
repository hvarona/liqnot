package com.henja.liqnot.ws;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketFrame;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * Created by henry on 08/01/2017.
 */

public interface ApiFunction {
    public int getApiID();
    public String getMethodToCall();
    public String getMethod();
    public List<Serializable> getParams();
    public void onResponse(JSONArray response);
}
