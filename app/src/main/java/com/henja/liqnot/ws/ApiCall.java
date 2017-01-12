package com.henja.liqnot.ws;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by henry on 08/01/2017.
 */

public class ApiCall {
    public String method;
    public String methodToCall;
    public String jsonrpc;
    public List<Serializable> params;
    public int apiId;

    public ApiCall(int apiId, String methodToCall, List<Serializable> params, String jsonrpc){

        this.apiId = apiId;
        this.method = "call";
        this.methodToCall = methodToCall;
        this.jsonrpc = jsonrpc;
        this.params = params;
    }

    public ApiCall(int apiId, String method, String methodToCall, List<Serializable> params, String jsonrpc){
        this.apiId = apiId;
        this.method = method;
        this.methodToCall = methodToCall;
        this.jsonrpc = jsonrpc;
        this.params = params;
    }

    public JSONObject toJsonObject(int seq) throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("id", seq);
        obj.put("method", this.method);
        JSONArray paramsArray = new JSONArray();
        paramsArray.put(this.apiId);
        paramsArray.put(this.methodToCall);
        JSONArray methodParams = new JSONArray();

        for(int i = 0; i < this.params.size(); i++){
            if (Number.class.isInstance(this.params.get(i))){
                methodParams.put( (Number) this.params.get(i));
            }else if(this.params.get(i) instanceof String || this.params.get(i) == null){
                methodParams.put((String) this.params.get(i));
            }else if(this.params.get(i) instanceof ArrayList){
                JSONArray array = new JSONArray();
                ArrayList<Serializable> listArgument = (ArrayList<Serializable>) this.params.get(i);
                for(int l = 0; l < listArgument.size(); l++){
                    Serializable element = listArgument.get(l);
                    if(element instanceof String){
                        array.put((String) element);
                    }
                }
                methodParams.put(array);
            }else{
                //System.out.println("Skipping parameter of type: "+this.params.get(i).getClass());
            }
        }
        paramsArray.put(methodParams);
        obj.put("params", paramsArray);
        obj.put("jsonrpc", this.jsonrpc);
        return obj;
    }
}
