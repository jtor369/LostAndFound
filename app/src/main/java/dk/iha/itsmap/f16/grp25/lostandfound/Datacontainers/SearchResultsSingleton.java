package dk.iha.itsmap.f16.grp25.lostandfound.Datacontainers;

import android.util.Log;

import dk.iha.itsmap.f16.grp25.lostandfound.Helpers.SearchResultsListViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jacob on 27/05/2016.
 */
public class SearchResultsSingleton {
    private static SearchResultsSingleton ourInstance;// = new SearchResultsSingleton();
    private static final String moduleName = "SearchResultsSingleton";

    private List<Item> SearchResults;

    public static void initInstance()
    {
        if (ourInstance == null) {
            Log.println(Log.DEBUG,moduleName, "Creating new SearchResultsSingleton Instance");
            ourInstance = new SearchResultsSingleton();
        }
    }

    public static SearchResultsSingleton getInstance() {
        if (ourInstance == null)
        {
            Log.println(Log.ERROR,moduleName,"getInstance() => null !");

        }
        return ourInstance;
    }

    private SearchResultsSingleton() {
    }

    public List<Item> getSearchResults()
    {
        if (SearchResults == null) {
                SearchResults = new ArrayList<Item>();
        }

        return SearchResults;
    }

    public void setSearchResults(List<Item> newResults)
    {
        if (SearchResults == null) {

                SearchResults = newResults;

        }else
        {
            synchronized (SearchResults)
            {
                SearchResults = newResults;
            }
        }
    }

    public void cleanup()
    {
            SearchResults = null;
    }
}
