package papercup.digitalcurrencyconverter;


import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by danielmendoza on 9/23/16.
 *
 * Configuration Activity that allows the user to modify the following settings for their widget;
 *   - Refresh Interval Time (minutes)
 */
public class Configure extends Activity {

    private Configure context;
    private int widgetID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // Don't create the widget if the user escapes
        setResult(RESULT_CANCELED);
        setContentView(R.layout.activity_configure);
        context = this;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            widgetID = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        Button button = (Button) findViewById(R.id.configureButton);
        button.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {

                final EditText et = (EditText) findViewById(R.id.refreshInterval);

                SharedPreferences sharedPref =
                        getSharedPreferences(getString(R.string.PREFERENCE_FILE_KEY),
                                Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("interval", et.getText().toString());
                editor.apply();

                Toast.makeText(Configure.this, "Saved Configuration", Toast.LENGTH_LONG).show();

                startService(new Intent(getBaseContext(), MyService.class));

                Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);
                setResult(RESULT_OK, resultValue);
                finish();
            }
        });
    }
}
