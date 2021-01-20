package com.nvi0.pb.meetandplay.Utils;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class RequestsQueueSingleton {

    private static RequestsQueueSingleton instance;
    private RequestQueue requestQueue;
    private static Context context;

    private RequestsQueueSingleton(Context _context){
        context = _context;
        requestQueue = getRequestQueue();
    }


    public static synchronized RequestsQueueSingleton getInstance(Context context){

        if (instance == null){
            instance = new RequestsQueueSingleton(context);
        }

        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

}
