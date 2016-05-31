package dk.iha.itsmap.f16.grp25.lostandfound;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity {
    private SharedPreferences sp;
    private String Address;
    private EditText editServerAddress;
    private Button btnSave;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        editServerAddress = (EditText) findViewById(R.id.editServerAddress);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnBack = (Button) findViewById(R.id.btnBack);

        Address = "ServerAddress (Error 3, OnCreate unassigned)";


        SharedPreferences sp = getSharedPreferences(ProjectConstants.SharedPreferencesID, Context.MODE_PRIVATE);
        if (savedInstanceState != null)
        {
            if (savedInstanceState.containsKey(ProjectConstants.SharedPrefs_ServerAddress)) {
               Address = savedInstanceState.getString(ProjectConstants.SharedPrefs_ServerAddress);
            }
        }
        else
        {
            Address = sp.getString(ProjectConstants.SharedPrefs_ServerAddress, "ServerAddress (Error 2, sharedprefs)");
        }

        final SharedPreferences spE = sp;

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Address = editServerAddress.getText().toString();

                SharedPreferences.Editor editor = spE.edit();
                editor.putString(ProjectConstants.SharedPrefs_ServerAddress,Address);
                editor.commit();

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(ProjectConstants.SharedPrefs_ServerAddress,editServerAddress.getText().toString());

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (editServerAddress != null)
        {
            editServerAddress.setText(Address);
        }


    }
}
