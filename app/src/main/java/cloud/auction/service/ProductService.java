package cloud.auction.service;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

public class ProductService {

    static final String TAG = "ProductService";
    static final ObjectMapper om = new ObjectMapper();
    static final String STORE_URL_API = "http://18.138.221.174/bid/B001234";
    private static String[] images={};

    public static void getAll(Context context, final VolleyCallback callback) {

        final String URL = STORE_URL_API;
        RequestQueue requestQueue = VolleyManager.getInstance(context).getRequestQueue();

        StringRequest objectRequest = new StringRequest(
                Request.Method.GET,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject responseJsonObject = new JSONObject(response);
//                            Product Product = new Product(responseJsonObject.getJSONObject("data").getJSONObject("bidding").getJSONObject("product").getString("id"),
//                                    responseJsonObject.getJSONObject("data").getJSONObject("bidding").getJSONObject("product").getString("name"),
//                                    responseJsonObject.getJSONObject("data").getJSONObject("bidding").getJSONObject("product").getString("price"),
//                                    responseJsonObject.getJSONObject("data").getJSONObject("bidding").getJSONObject("product").getString("description"),
//                                    responseJsonObject.getJSONObject("data").getJSONObject("bidding").getJSONObject("product").getString("active"),
//                                    images,
//                                    responseJsonObject.getJSONObject("data").getJSONObject("bidding").getJSONObject("product").getString("bids"));
//                            callback.onSuccess(Product);
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
