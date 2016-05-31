package dk.iha.itsmap.f16.grp25.lostandfound.Datacontainers;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Bertrand on 29-05-2016.
 */
public class VolleyQueueInstance {
    private static VolleyQueueInstance ourInstance;// = new VolleyQueueInstance();
    private static final String moduleName = "VolleyQueueInstance";

    private RequestQueue requestQueue;

    public static void initInstance(Context context)
    {
        if (ourInstance == null) {
            Log.println(Log.DEBUG,moduleName, "Creating new VolleyQueueInstance Instance");
            ourInstance = new VolleyQueueInstance(context);
        }
    }

    public static VolleyQueueInstance getInstance() {
        if (ourInstance == null)
        {
            Log.println(Log.ERROR,moduleName,"getInstance() => null !");

        }

        return ourInstance;
    }

    private VolleyQueueInstance(Context context) {
        this.requestQueue = Volley.newRequestQueue(context);
    }

    public RequestQueue getRequestQueue()
    {
        return this.requestQueue;
    }
}
