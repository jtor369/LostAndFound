package dk.iha.itsmap.f16.grp25.lostandfound;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import dk.iha.itsmap.f16.grp25.lostandfound.Datacontainers.Item;
import dk.iha.itsmap.f16.grp25.lostandfound.Datacontainers.LocationSingleton;
import dk.iha.itsmap.f16.grp25.lostandfound.Helpers.ServerService;

import java.util.ArrayList;
import java.util.List;

public class LostActivity extends Activity {

    final static String SAVED_DESCRIPTION = "savedDescription";
    private static final String moduleName = "LostActivity";
    private Button search;
    private Button cancel;
    private Button locater;
    public final int THUMBNAIL = 667;
    private ImageView imageButton;
    private EditText description;
    private EditText latView;
    private EditText lonView;
    private EditText radView;
    private Location userLocation;
    private Bitmap thumbnail;
    private List<String> tags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(moduleName,"onCreate();");
        userLocation = null;
        List<String> tags = new ArrayList<String>();
        tags.add("sort");
        tags.add("sofa");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost);



        search = (Button) findViewById(R.id.lostSearchButton);
        cancel = (Button) findViewById(R.id.lostCancelButton);
        locater = (Button) findViewById(R.id.locater);
        latView = (EditText) findViewById(R.id.txtLat);
        lonView = (EditText) findViewById(R.id.txtLon);
        radView = (EditText) findViewById(R.id.txtRad);
        description = (EditText) findViewById(R.id.editLostDescriptionText);
        imageButton = (ImageView) findViewById(R.id.imageButton);
        search.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                senddata();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        locater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTracking();
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(cameraIntent, THUMBNAIL);
                    }else {
                        Toast.makeText(LostActivity.this, "Image request denied!", Toast.LENGTH_LONG).show();
                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        if(savedInstanceState != null && description != null){
            description.setText(savedInstanceState.getString(SAVED_DESCRIPTION));
        }
        SetupBroadcastReceivers();
    }

    //Følgende kode kopieret fra eksempel kode der ligger på Kursets Blackboard side.
    //COPY BEGIN (1)

    private LocationManager locationManager;
    private static final long MIN_TIME_BETWEEN_LOCATION_UPDATES = 5 * 1000;    // milisecs
    private static final float MIN_DISTANCE_MOVED_BETWEEN_LOCATION_UPDATES = 1;  // meter
    private boolean isTracking = false;

    private boolean startTracking() {
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
                    locationManager.requestLocationUpdates(minTime, minDistance, criteria, locationListener, null);
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

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Log.d(moduleName,"onLocationChanged();");
            LocationSingleton.getInstance().setLocation(location);


            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(ProjectConstants.BroadcastLocationUpdateAction));
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
    private void SetupBroadcastReceivers()
    {
        LocalBroadcastManager lbcm = LocalBroadcastManager.getInstance(this);
        /**
        lbcm.registerReceiver(uploadSuccessReceiver, new IntentFilter(ProjectConstants.BroadcastUploadSuccessAction));
        lbcm.registerReceiver(uploadFailReceiver, new IntentFilter(ProjectConstants.BroadcastUploadFailAction));
         /**/
        lbcm.registerReceiver(searchSuccessReceiver, new IntentFilter(ProjectConstants.BroadcastSearchResultsSuccessAction));
        lbcm.registerReceiver(searchFailReceiver, new IntentFilter(ProjectConstants.BroadcastSearchResultsFailAction));
        lbcm.registerReceiver(locationUpdateReceiver, new IntentFilter(ProjectConstants.BroadcastLocationUpdateAction));
    }

    private void UnregisterBroadcastReceivers()
    {
        LocalBroadcastManager lbcm = LocalBroadcastManager.getInstance(this);
        /**
        lbcm.unregisterReceiver(uploadSuccessReceiver);
        lbcm.unregisterReceiver(uploadFailReceiver);
         /**/
        lbcm.unregisterReceiver(searchSuccessReceiver);
        lbcm.unregisterReceiver(searchFailReceiver);
        lbcm.unregisterReceiver(locationUpdateReceiver);
    }

    /**/
    private BroadcastReceiver locationUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context,"locationUpdateReceiver got "+ Item.JSONLocationGEN(LocationSingleton.getInstance().getLocation()),Toast.LENGTH_SHORT).show();
            stopTracking();
            userLocation = LocationSingleton.getInstance().getLocation();
            showLocation(userLocation);
        }
    };

    /**
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
     /**/

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

                    long time = System.currentTimeMillis()/1000;

                    thumbnail = newBitmap;
                    imageButton.setImageBitmap(thumbnail);
                }
                break;
            default:
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showLocation(Location l) {
        lonView.setText(Double.toString(l.getLongitude()));
        latView.setText(Double.toString(l.getLatitude()));
        radView.setText(Float.toString(l.getAccuracy()));

    }

    private void getLocationFromText()
    {
        try{
            if (userLocation == null)
            {
                userLocation = new Location("userlocation");
            }
            userLocation.setLatitude(Double.parseDouble(latView.getText().toString()));
            userLocation.setLongitude(Double.parseDouble(lonView.getText().toString()));
            userLocation.setAccuracy(Float.parseFloat(radView.getText().toString()));

        }catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    private void senddata() {
        Log.d(moduleName,"senddata();");
        //inserts dummy data: for :id, userId and timestamp
        int id = 01;
        int userId = 01;
        Long timestamp = System.currentTimeMillis()/1000;
        getLocationFromText();
        Item item = new Item(id, "" /*description.getText().toString()*/, userLocation, userId, timestamp, tags, null/*thumbnail*/);
        ServerService.searchFor(this,item);
    }
    public void onSaveInstanceState(Bundle savedInstanceState){
        Log.d(moduleName,"onSaveInstanceState();");
        super.onSaveInstanceState(savedInstanceState);
        //Save the fragment's instance
        //savedInstanceState.putString(SAVED_DESCRIPTION, description.getText().toString());
    }
    @Override
    protected void onDestroy() {
        UnregisterBroadcastReceivers();
        super.onDestroy();
    }

}
