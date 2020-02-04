package cloud.auction.service;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import cloud.auction.model.ObjectResponse;
import cloud.auction.ultils.Constant;

public class CategoryService {
    static final String TAG = "CategoryService"  ;
    static final ObjectMapper om= new ObjectMapper() ;
    static final String STORE_URL_API = Constant.HOST+"category";

    public static void getAll(Context context, final VolleyCallback callback) {

        final String URL = STORE_URL_API;
        final RequestQueue requestQueue = VolleyManager.getInstance(context).getRequestQueue();

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
        });
        requestQueue.add(objectRequest);
    }


}
