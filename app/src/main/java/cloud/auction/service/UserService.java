package cloud.auction.service;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cloud.auction.model.ObjectResponse;
import cloud.auction.ultils.Constant;

public class UserService {
    static final String TAG = "UserService";
    static final String STORE_URL_API = Constant.HOST;
    private static ObjectMapper om = new ObjectMapper();

    public static void Register(Context context, String email, String fullname, String phone,
                                String password, final VolleyCallback callback) {

        final String URL = STORE_URL_API + "register";
        RequestQueue requestQueue = VolleyManager.getInstance(context).getRequestQueue();
        final JSONObject json = new JSONObject();
        try {
            json.put("email", email);
            json.put("fullName", fullname);
            json.put("phone", phone);
            json.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringRequest objectRequest = new StringRequest(
                Request.Method.POST,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            ObjectResponse objectResponse = om.readValue(response, ObjectResponse.class);
                            callback.onSuccess(objectResponse);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            Log.e(TAG, "onResponse: " + ex.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "onErrorResponse: " + new String(error.networkResponse.data));
                    }
                }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                return json.toString().getBytes();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        requestQueue.add(objectRequest);
    }

    public static void Update(Context context,
                              String address, final String token, final VolleyCallback callback) {

        final String URL = STORE_URL_API + "user";
        RequestQueue requestQueue = VolleyManager.getInstance(context).getRequestQueue();
        final JSONObject json = new JSONObject();
        try {
//            json.put("id", id);
//            json.put("email", email);
//            json.put("fullName", fullname);
//            json.put("phone", phone);
            json.put("active", true);
            json.put("address", address);
            json.put("roleId", 2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringRequest objectRequest = new StringRequest(
                Request.Method.PUT,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            ObjectResponse objectResponse = om.readValue(response, ObjectResponse.class);
                            callback.onSuccess(objectResponse);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            Log.e(TAG, "onResponse: " + ex.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "onErrorResponse: " + new String(error.networkResponse.data));
                    }
                }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                return json.toString().getBytes();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", token);
                return headers;
            }
        };
        requestQueue.add(objectRequest);
    }

    public static void Login(Context context, String username, String password, final VolleyCallback callback) {

        final String URL = STORE_URL_API + "login";
        final RequestQueue requestQueue = VolleyManager.getInstance(context).getRequestQueue();
        final JSONObject json = new JSONObject();
        try {

            json.put("email", username);
            json.put("password", password);
        } catch (Exception e) {
            Log.e(TAG, "Error in send body: " + e.getMessage());
        }

        StringRequest objectRequest = new StringRequest(
                Request.Method.POST,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            ObjectResponse objectResponse = om.readValue(response, ObjectResponse.class);
                            callback.onSuccess(objectResponse);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            Log.e(TAG, "onResponse: " + ex.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: " + error.toString());
            }
        }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                return json.toString().getBytes();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        requestQueue.add(objectRequest);
    }

    public static void getUserDetail(Context context, final String token, final VolleyCallback callback) {

        final String URL = STORE_URL_API+"user";
        RequestQueue requestQueue = VolleyManager.getInstance(context).getRequestQueue();

        StringRequest objectRequest = new StringRequest(
                Request.Method.GET,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            ObjectResponse objectResponse = om.readValue(response,ObjectResponse.class);
                            callback.onSuccess(objectResponse);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            Log.e(TAG, "onResponse: " + ex.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: " + error.toString());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", token);
                return headers;
            }
        };
        requestQueue.add(objectRequest);
    }
}
