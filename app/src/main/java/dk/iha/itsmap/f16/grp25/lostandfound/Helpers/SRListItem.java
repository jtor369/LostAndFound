package dk.iha.itsmap.f16.grp25.lostandfound.Helpers;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import dk.iha.itsmap.f16.grp25.lostandfound.Datacontainers.Item;
import dk.iha.itsmap.f16.grp25.lostandfound.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Jacob on 16/05/2016.
 */
public class SRListItem {
    private SRListItem(){}

    public static void populateView(View view, Item item)
    {
        ImageView thumbnail;
        TextView txtDescription;
        TextView txtTags;
        TextView txtTime;

        thumbnail = (ImageView) view.findViewById(R.id.imgThumbnail);
        txtDescription = (TextView) view.findViewById(R.id.txtDescriptionFinal);
        txtTags = (TextView) view.findViewById(R.id.txtTags);
        txtTime = (TextView) view.findViewById(R.id.txtTime);

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
    }
}
