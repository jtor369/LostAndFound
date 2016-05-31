package dk.iha.itsmap.f16.grp25.lostandfound.Helpers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import dk.iha.itsmap.f16.grp25.lostandfound.Datacontainers.Item;
import dk.iha.itsmap.f16.grp25.lostandfound.R;
import dk.iha.itsmap.f16.grp25.lostandfound.SettingsActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Jacob on 16/05/2016.
 */
public class SRListItemSelected {
    /*
    private ImageView thumbnail;
    private TextView txtDescription;
    private TextView txtTags;
    private TextView txtTime;
    */

    private SRListItemSelected(){}


    public static void populateView(int position, Context context, View view, Item item)
    {

        ImageView thumbnail;
        TextView txtDescription;
        TextView txtTags;
        TextView txtTime;
        TextView txtLocation;
        Button btnClick;

        thumbnail = (ImageView) view.findViewById(R.id.imgThumbnail);
        txtDescription = (TextView) view.findViewById(R.id.txtDescriptionFinal);
        txtTags = (TextView) view.findViewById(R.id.txtTags);
        txtTime = (TextView) view.findViewById(R.id.txtTime);
        txtLocation = (TextView) view.findViewById(R.id.txtLocation);
        btnClick = (Button) view.findViewById(R.id.btnClick);

        if (item.thumbnail != null) {
            thumbnail.setImageBitmap(item.thumbnail);
        }
        txtDescription.setText(item.description);
        txtTags.setText(item.tags.toString());

        long unixSeconds = item.timestamp;
        Date date = new Date(unixSeconds*1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"); // the format of your date
        sdf.setTimeZone(TimeZone.getDefault()); // give a timezone reference for formating (see comment at the bottom
        String formattedDate = sdf.format(date);

        txtTime.setText(formattedDate);

        String locationText = String.format("Lat: %f; Lon: %f;",item.location.getLatitude(), item.location.getLongitude());
        txtLocation.setText(locationText);

        final Item it = item;
        final Context cont = context;
        final int pos = position;

        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.println(Log.DEBUG,"Item Button","Button Clicked! " + it.id);
                Toast.makeText(cont, "Button clicked " + pos, Toast.LENGTH_SHORT).show();
                /*
                cont.startActivity(new Intent(cont, SettingsActivity.class));
                Log.d("SpecialButton","Should have started an activity");
                */
            }
        });
    }
}
