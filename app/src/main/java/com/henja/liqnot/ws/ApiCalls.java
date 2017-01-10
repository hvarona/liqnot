package com.henja.liqnot.ws;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFrame;

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
    private ArrayList<ApiCallsListener> listeners; //TODO call triggerOnAllDataReceived() to trigger event


    public ApiCalls() {
        this.listeners = new ArrayList<ApiCallsListener>();
    }

    public void addFunction(ApiFunction function){
        ++lastIndex;
        functions.put(lastIndex,function);
    }

    public String toJsonString() {
        StringBuilder answer = new StringBuilder();

        for(int index : functions.keySet()){
            ApiFunction function = functions.get(index);
            try {
                answer.append(new ApiCall(function.getApiID(),function.getMethodToCall(),function.getParams(),"2.0").toJsonObject(index)).append("\r\n");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return answer.toString();
    }

    @Override
    public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
        websocket.sendText(this.toJsonString());
    }

    @Override
    public void onTextFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {
        System.out.println(frame.getPayloadText());
        JSONObject incoming = new JSONObject(frame.getPayloadText());
        int index = incoming.getInt("id");
        try {
            functions.get(index).onResponse((JSONObject) incoming.get("result"));
        }catch(Exception e){
            e.printStackTrace();
        }
        functions.remove(index);
        if(functions.isEmpty()){
            websocket.disconnect();
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
    public void addApiCallsListener(ApiCallsListener listener){
        this.listeners.add(listener);
    }

    public void triggerOnAllDataReceived(){
        for(ApiCallsListener listener : listeners){
            listener.OnAllDataReceived();
        }
    }

    public interface ApiCallsListener{
        public void OnAllDataReceived();
    }
}
