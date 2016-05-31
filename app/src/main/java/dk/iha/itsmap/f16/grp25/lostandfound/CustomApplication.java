package dk.iha.itsmap.f16.grp25.lostandfound;

import android.app.Application;
import android.util.Log;

import dk.iha.itsmap.f16.grp25.lostandfound.Datacontainers.LocationSingleton;
import dk.iha.itsmap.f16.grp25.lostandfound.Datacontainers.SearchResultsSingleton;
import dk.iha.itsmap.f16.grp25.lostandfound.Datacontainers.VolleyQueueInstance;

/**
 * Created by Jacob on 27/05/2016.
 */
public class CustomApplication extends Application {

    @Override
    public void onCreate()
    {
        super.onCreate();

        // Initialize the singletons so their instances
        // are bound to the application process.
        initSingletons();
    }

    private void initSingletons()
    {
        Log.println(Log.DEBUG,"CustomApplication","initSingletons()");
        SearchResultsSingleton.initInstance();
        VolleyQueueInstance.initInstance(getApplicationContext());
        LocationSingleton.initInstance();
    }

}
