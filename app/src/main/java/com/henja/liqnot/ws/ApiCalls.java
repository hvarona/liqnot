package com.henja.liqnot.ws;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFrame;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by henry on 08/01/2017.
 */

public class ApiCalls extends WebSocketAdapter {

    private Map<Integer,ApiFunction> functions = new HashMap();
    private int lastIndex = 0;
    private ArrayList<ApiCallsListener> listeners;


    public ApiCalls() {
        this.listeners = new ArrayList<ApiCallsListener>();
    }

    public void addFunction(ApiFunction function){
        if(!functions.containsValue(function)) {
            ++lastIndex;
            functions.put(lastIndex, function);
        }
    }

    public void addListener(ApiCallsListener listener){
        listeners.add(listener);
    }

    public String sendMessage(WebSocket webSocket) {

        for(int index : functions.keySet()){
            ApiFunction function = functions.get(index);
            try {
                webSocket.sendText(new ApiCall(function.getApiID(),function.getMethodToCall(),function.getParams(),"2.0").toJsonObject(index).toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return "";
    }

    @Override
    public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
        sendMessage(websocket);
    }

    @Override
    public void onTextFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {
        System.out.println(frame.getPayloadText());
        JSONObject incoming = new JSONObject(frame.getPayloadText());
        int index = incoming.getInt("id");
        try {
            Object unknowJsonClass = incoming.get("result");

            if (unknowJsonClass instanceof JSONObject){
                JSONObject jsonObject = (JSONObject)unknowJsonClass;
                functions.get(index).onResponse(jsonObject);
            } else if (unknowJsonClass instanceof JSONArray){
                JSONArray jsonArray = (JSONArray)unknowJsonClass;
                functions.get(index).onResponse(jsonArray);
            }
        }catch(Exception e){
            //TODO manage error call
            e.printStackTrace();
        }
        functions.remove(index);
        if(functions.isEmpty()){
            websocket.disconnect();
            for(ApiCallsListener listener : listeners){
                listener.OnAllDataReceived();
            }
        }
    }

    @Override
    public void onFrameSent(WebSocket websocket, WebSocketFrame frame) throws Exception {
        super.onFrameSent(websocket, frame);
    }

    @Override
    public void onError(WebSocket websocket, WebSocketException cause) throws Exception {
        super.onError(websocket, cause);
    }

    @Override
    public void handleCallbackError(WebSocket websocket, Throwable cause) throws Exception {
        super.handleCallbackError(websocket, cause);
    }

    @Override
    public void onConnectError(WebSocket websocket, WebSocketException exception) throws Exception {
        super.onConnectError(websocket, exception);
    }


    //METHODS FOR LISTENERS
    /*public void addApiCallsListener(ApiCallsListener listener){
        this.listeners.add(listener);
    }*/

    public void triggerOnAllDataReceived(){
        for(ApiCallsListener listener : listeners){
            listener.OnAllDataReceived();
        }
    }

    public void addFunctions(ArrayList<ApiFunction> apiFunctions) {
        for(ApiFunction apiFunction : apiFunctions){
            addFunction(apiFunction);
        }
    }

    public boolean hasFunctions() {
        return !functions.isEmpty();
    }

    public interface ApiCallsListener{
        public void OnAllDataReceived();
    }
}
