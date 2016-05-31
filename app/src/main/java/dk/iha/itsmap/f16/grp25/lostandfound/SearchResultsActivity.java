package dk.iha.itsmap.f16.grp25.lostandfound;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.CharacterPickerDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import dk.iha.itsmap.f16.grp25.lostandfound.Datacontainers.Item;
import dk.iha.itsmap.f16.grp25.lostandfound.Datacontainers.SearchResultsSingleton;
import dk.iha.itsmap.f16.grp25.lostandfound.Helpers.SearchResultsListViewAdapter;
import dk.iha.itsmap.f16.grp25.lostandfound.Helpers.ServerService;

import java.util.List;

public class SearchResultsActivity extends AppCompatActivity {
    private static final String moduleName = "SearchResultsActivity";

    private static final String POSITIONINT = "SRPositionInt";

    private ListView resultsList;
    private TextView resultsCountText;
    private SearchResultsListViewAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        resultsList = (ListView) findViewById(R.id.resultsList);
        resultsCountText = (TextView) findViewById(R.id.resultsCountText);




        //TODO: receive service completed notification


        //TODO: get item list from service using interface and present in listview.
        updateList();
        if (savedInstanceState != null)
        {
            int position = savedInstanceState.getInt(POSITIONINT );
            if (adapter != null)
            {
                adapter.setSelected(position);
            }

        }

        resultsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setSelected(position);
                adapter.notifyDataSetChanged();
            }
        });

     }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(POSITIONINT, adapter.getSelected());
        super.onSaveInstanceState(outState);
    }

    public void updateList()
    {
        adapter = new SearchResultsListViewAdapter(resultsList, this, ServerService.getResults() );
        /*SearchResultsSingleton.getInstance().getSearchResults()*/
        resultsCountText.setText(Integer.toString(adapter.getCount()));
        resultsList.setAdapter(adapter);
    }

    private void cleanup() //clears reference to itemlist in application singleton
    {
        Log.println(Log.DEBUG,moduleName,"Running Cleanup");
        ServerService.clearItems();
    }

    @Override
    protected void onDestroy() {
        Log.println(Log.DEBUG,moduleName,"onDestroy()");
        if (isFinishing()) {
            cleanup();
        }
        super.onDestroy();

    }
}
