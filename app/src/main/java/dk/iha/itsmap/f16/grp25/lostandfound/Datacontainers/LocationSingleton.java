package dk.iha.itsmap.f16.grp25.lostandfound.Datacontainers;

import android.location.Location;
import android.util.Log;

/**
 * Created by Bertrand on 29-05-2016.
 */
public class LocationSingleton {
    private static final String moduleName = "LocationSingleton";
    private static LocationSingleton ourInstance = new LocationSingleton();
    private Location location;

    public static void initInstance() {
        if (ourInstance == null) {
            Log.println(Log.DEBUG, moduleName, "Creating new " + moduleName + " Instance");
            ourInstance = new LocationSingleton();
        }
    }

    public static LocationSingleton getInstance() {

        if (ourInstance == null) {
            Log.println(Log.ERROR, moduleName, "getInstance() => null !");

        }
        return ourInstance;
    }

    public Location getLocation(){
        return location;
    }
    
    public void setLocation(Location location)
    {
        this.location = location;
    }

    private LocationSingleton() {
    }
}
