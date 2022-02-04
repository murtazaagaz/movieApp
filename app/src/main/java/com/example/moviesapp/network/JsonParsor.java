package com.example.moviesapp.network;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonParsor {
    public static String simpleParser(String response) throws JSONException {
        JSONObject obj = new JSONObject(response);
        return obj.getString("Error");
    }
    public static boolean isReqSuccesful(String response) throws JSONException {
        JSONObject object = new JSONObject(response);
        return object.getString("Response").equalsIgnoreCase("true");
    }

}

