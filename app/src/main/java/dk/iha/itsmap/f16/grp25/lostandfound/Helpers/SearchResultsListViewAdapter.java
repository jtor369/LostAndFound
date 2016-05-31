package dk.iha.itsmap.f16.grp25.lostandfound.Helpers;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import dk.iha.itsmap.f16.grp25.lostandfound.Datacontainers.Item;
import dk.iha.itsmap.f16.grp25.lostandfound.R;

import java.util.List;

/**
 * Created by Jacob on 16/05/2016.
 */
public class SearchResultsListViewAdapter extends BaseAdapter{
    private List<Item> items;
    private Context context;
    private ListView container;
    private Item item;
    private int selected;

    public SearchResultsListViewAdapter(ListView container, Context context, List<Item> items)
    {
        this.context = context;
        this.items = items;
        this.container = container;
        this.selected = -1;

    }

    @Override
    public int getCount() {
        if (items != null)
        {
            return items.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (items != null && position < items.size())
        {
            return items.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public void setSelected(int i)
    {
        if (this.selected != i) {
            this.selected = i;
            //Toast.makeText(this.context, "Selected " + Integer.toString(this.selected), Toast.LENGTH_SHORT).show();
        }else {
            //Toast.makeText(this.context, "De-Selected " + Integer.toString(this.selected), Toast.LENGTH_SHORT).show();
            this.selected = -1;
        }
        }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int layout_id = R.layout.search_result_listitem;
        Log.println(Log.DEBUG,"adapter","Rendering "+Integer.toString(position) + ", selected: "+Integer.toString(this.selected));
        //Toast.makeText(this.context,"Rendering "+Integer.toString(position),Toast.LENGTH_SHORT).show();

        if (position == this.selected)
        {
            layout_id = R.layout.search_result_listitem_selected;
        }

        //if (convertView == null) {
            LayoutInflater inflator = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflator.inflate(layout_id, null);

        //}

        item = this.items.get(position);
        if(item!=null){
            switch(layout_id)
            {
                case R.layout.search_result_listitem:
                    SRListItem.populateView(convertView,item);
                    break;

                case R.layout.search_result_listitem_selected:
                    SRListItemSelected.populateView(position,context,convertView,item);
                    break;

                default:
                    break;
            }

        }
        return convertView;
    }

    public int getSelected() {
        return this.selected;
    }
}
