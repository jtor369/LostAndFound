package dk.iha.itsmap.f16.grp25.lostandfound;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import dk.iha.itsmap.f16.grp25.lostandfound.Datacontainers.Item;
import dk.iha.itsmap.f16.grp25.lostandfound.Datacontainers.LocationSingleton;
import dk.iha.itsmap.f16.grp25.lostandfound.Helpers.ServerService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
//todo: implement "Login" via SharedPreferences - ask them to create a username at first login, then save the username locally and use it in this activity to say hello
    private static final String moduleName = "MainActivity";

    public final int THUMBNAIL = 667;

    private Button lostButton;
    private Button foundButton;
    private Button searchButton;
    private Button requestButton;
    private Button uploadButton;
    private Button settingsButton;
    private Button getlocationButton;

    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedpreferences = getSharedPreferences(ProjectConstants.SharedPreferencesID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        if (!sharedpreferences.contains(ProjectConstants.SharedPrefs_ServerAddress))
        {
            editor.putString(ProjectConstants.SharedPrefs_ServerAddress,getString(R.string.server_address));
            editor.commit();
        }

        context = this;
        lostButton  = (Button) findViewById(R.id.lostButton);
        foundButton = (Button) findViewById(R.id.foundButton);
        searchButton = (Button) findViewById(R.id.searchButton);

        requestButton = (Button) findViewById(R.id.btnRequest);
        uploadButton = (Button) findViewById(R.id.btnUpload);
        settingsButton  = (Button) findViewById(R.id.btnSettings);

        getlocationButton = (Button) findViewById(R.id.btnLocation);

        lostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, LostActivity.class);
                startActivity(i);
            }
        });
        foundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, FoundActivity.class);
                startActivity(i);
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SearchResultsActivity.class);
                startActivity(i);
            }
        });

        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ServerService a = new ServerService();
                //public Item(int id, String description, Location location, int userid, int timestamp, List<String> tags, Bitmap thumbnail)

                ServerService.searchFor(context, new Item(10, "", Item.JSONLocationParse("{\"lat\":56.0, \"lon\":10.0,\"radius\":1000}"), 10, 0, new ArrayList<String>(), null));
                                                 //new Item(10, "", Item.JSONLocationParse("{\"lat\":56.0, \"lon\":10.0,\"radius\":1000}"), 10, time, new ArrayList<String>(), null);
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
/** /
                    long time = System.currentTimeMillis()/1000;
                    ServerService.storeItem(context, new Item(10, "Custom Thumbnail", Item.JSONLocationParse("{\"lat\":56.0, \"lon\":10.0,\"radius\":1000}"), 10, time, new ArrayList<String>(), null));
            /**/
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


                if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(cameraIntent, THUMBNAIL);
                }else
                {
                    Toast.makeText(MainActivity.this, "Image request denied!", Toast.LENGTH_LONG).show();
                }
            /**/
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }

            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toSettingsIntent = new Intent(MainActivity.this,SettingsActivity.class);
                startActivity(toSettingsIntent);
            }
        });

        getlocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTracking();
            }
        });

        //SetupBroadcastReceivers();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode)
        {
            case THUMBNAIL:
                if (resultCode == RESULT_OK)
                {
                    Bundle extras = data.getExtras();
                    Bitmap tn = (Bitmap) extras.get("data");
                    int width = tn.getWidth();
                    int height = tn.getHeight();
                    double newWidth = 100;
                    double newHeight = height * newWidth/width;

                    Bitmap newBitmap = Bitmap.createScaledBitmap(tn, (int)newWidth, (int)newHeight,false);

                    List<String> tags = new ArrayList<String>();

                    Random rnd = new Random();
                    int count = (int)rnd.nextInt(5);

                    for (int k = 0; k < count; k++)
                    {
                        tags.add("Tag"+Integer.toString(rnd.nextInt(100)));
                    }

                    long time = System.currentTimeMillis()/1000;
                    /**
                    if (LocationSingleton.getInstance().getLocation() != null)
                    {

                    }else {
                        /**/
                Item it = new Item(10, "Custom Thumbnail", Item.JSONLocationParse("{\"lat\":56.0, \"lon\":10.0,\"radius\":1000}"), 10, time, tags, null);
                    Toast.makeText(getApplicationContext(),it.toJSONString(),Toast.LENGTH_LONG).show();
                    ServerService.storeItem(context, new Item(10, "Custom Thumbnail", Item.JSONLocationParse("{\"lat\":56.0, \"lon\":10.0,\"radius\":1000}"), 10, time, tags, newBitmap));
                    //ServerService.storeItem(context, it);
                    //}

                }
                break;
            default:
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void SetupBroadcastReceivers()
    {
        LocalBroadcastManager lbcm = LocalBroadcastManager.getInstance(this);
        lbcm.registerReceiver(uploadSuccessReceiver, new IntentFilter(ProjectConstants.BroadcastUploadSuccessAction));
        lbcm.registerReceiver(uploadFailReceiver, new IntentFilter(ProjectConstants.BroadcastUploadFailAction));
        lbcm.registerReceiver(searchSuccessReceiver, new IntentFilter(ProjectConstants.BroadcastSearchResultsSuccessAction));
        lbcm.registerReceiver(searchFailReceiver, new IntentFilter(ProjectConstants.BroadcastSearchResultsFailAction));
        lbcm.registerReceiver(locationUpdateReceiver, new IntentFilter(ProjectConstants.BroadcastLocationUpdateAction));
    }

    private void UnregisterBroadcastReceivers()
    {
        LocalBroadcastManager lbcm = LocalBroadcastManager.getInstance(this);
        lbcm.unregisterReceiver(uploadSuccessReceiver);
        lbcm.unregisterReceiver(uploadFailReceiver);
        lbcm.unregisterReceiver(searchSuccessReceiver);
        lbcm.unregisterReceiver(searchFailReceiver);
        lbcm.unregisterReceiver(locationUpdateReceiver);

    }

    private BroadcastReceiver uploadSuccessReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context,"Successfully uploaded item refid:" + Integer.toString(intent.getIntExtra( ProjectConstants.BroadcastUploadRefid, -1 )),Toast.LENGTH_SHORT).show();
            Log.println(Log.DEBUG,moduleName,"uploadSuccessReceiver Received broadcast");

        }
    };

    private BroadcastReceiver uploadFailReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context,"Failed to upload item",Toast.LENGTH_SHORT).show();
            Log.println(Log.DEBUG,moduleName,"uploadFailReceiver Received broadcast");

        }
    };

    private BroadcastReceiver searchSuccessReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Log.println(Log.DEBUG,moduleName,"searchSuccessReceiver Received broadcast");

            Intent toSearchResultsIntent = new Intent(context, SearchResultsActivity.class);
            startActivity(toSearchResultsIntent);

        }
    };

    private BroadcastReceiver searchFailReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context,"Failed to retreive search results",Toast.LENGTH_SHORT).show();
            Log.println(Log.DEBUG,moduleName,"searchFailReceiver Received broadcast");

        }
    };




    //LOCATION STUFF....

    private BroadcastReceiver locationUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context,"locationUpdateReceiver got "+Item.JSONLocationGEN(LocationSingleton.getInstance().getLocation()),Toast.LENGTH_SHORT).show();
            Log.println(Log.DEBUG,moduleName,"locationUpdateReceiver Received broadcast");
            stopTracking();

        }
    };

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Log.d(moduleName,"onLocationChanged();");
            LocationSingleton.getInstance().setLocation(location);
            //userLocation = location;
            //updateStatus();'

            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(ProjectConstants.BroadcastLocationUpdateAction));
            //broadcastLocationUpdate(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };


    //Følgende kode kopieret fra eksempel kode der ligger på Kursets Blackboard side.
    //COPY BEGIN (1)
    private LocationManager locationManager;
    private static final long MIN_TIME_BETWEEN_LOCATION_UPDATES = 5 * 1000;    // milisecs
    private static final float MIN_DISTANCE_MOVED_BETWEEN_LOCATION_UPDATES = 1;  // meter
    private boolean isTracking = false;

    private boolean startTracking(){
        Log.d(moduleName,"startTracking();");
        try {
            if (locationManager == null) {
                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            }

            long minTime = MIN_TIME_BETWEEN_LOCATION_UPDATES;
            float minDistance = MIN_DISTANCE_MOVED_BETWEEN_LOCATION_UPDATES;
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            criteria.setPowerRequirement(Criteria.POWER_MEDIUM);

            if (locationManager != null) {
                try {
                    //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, locationListener);         //for specifying GPS provider
                    //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance, locationListener);     //for specifying Network provider
                    locationManager.requestLocationUpdates(minTime, minDistance, criteria, locationListener, null);
                    //Use criteria to chose best provider
                } catch (SecurityException ex) {
                    //TODO: user have disabled location permission - need to validate this permission for newer versions
                }
            } else {
                return false;
            }
            isTracking = true;
            return true;
        } catch (Exception ex) {
            //things can go wrong
            Log.e("TRACKER", "Error during start", ex);
            return false;
        }
    }

    private boolean stopTracking(){
        Log.d(moduleName,"stopTracking();");
        try {
            try{
                locationManager.removeUpdates(locationListener);
                isTracking = false;
            } catch (SecurityException ex) {
                //TODO: user have disabled location permission - need to validate this permission for newer versions
            }

            return true;
        } catch (Exception ex) {
            //things can go wrong here as well (listener is null)
            Log.e("TRACKER", "Error during stop", ex);
            return false;
        }
    }

    //COPY END(1)

    @Override
    protected void onPause() {
        UnregisterBroadcastReceivers();
        super.onPause();
    }

    @Override
    protected void onResume() {
        SetupBroadcastReceivers();
        super.onResume();
    }

    //Broadcast Receiver decouple

    @Override
    protected void onDestroy() {
        UnregisterBroadcastReceivers();
        super.onDestroy();
    }
}
