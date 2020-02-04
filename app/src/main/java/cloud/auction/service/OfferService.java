package cloud.auction.service;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cloud.auction.model.ObjectResponse;
import cloud.auction.ultils.Constant;

public class OfferService {
    static final String TAG = "OfferService";
    static final ObjectMapper om = new ObjectMapper();
    static final String STORE_URL_API = Constant.HOST + "offer";

    public static void Offer(Context context, String biddingId, String money, final String token, final VolleyCallback callback) {

        final String URL = STORE_URL_API;
        RequestQueue requestQueue = VolleyManager.getInstance(context).getRequestQueue();
        final JSONObject json = new JSONObject();
        try {

            json.put("biddingId", biddingId);
            json.put("money", money);
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
                headers.put("Authorization", token);
                return headers;
            }
        };
        requestQueue.add(objectRequest);
    }
}
