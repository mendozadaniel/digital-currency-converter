package papercup.digitalcurrencyconverter;

import android.nfc.NfcAdapter;
import android.os.AsyncTask;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.widget.TextViewCompat;
import android.util.Log;
import android.util.TypedValue;
import android.widget.RemoteViews;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by danielmendoza on 9/27/16.
 *
 * This async task has two purposes:
 * 1. Retrieve and process Bitcoin information from the user specified exchange
 * 2. Retrieve and convert the Bitcoin information to selected currency.
 *
 */
public class MyAsyncTask extends AsyncTask <Void, Void, HashMap<String, String>> {

    Context context;

    public MyAsyncTask(Context context) {
        this.context = context;
    }

    protected void onPreExecute() { }

    private JSONObject extractJSONObjectFromURL(String webAddress) {

        JSONObject jsonObj = null;

        try {

            URL url = new URL(webAddress);

            HttpsURLConnection https_connection = (HttpsURLConnection) url.openConnection();

            try {

                InputStream in = new BufferedInputStream(https_connection.getInputStream());

                try {

                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "iso-8859-1"), 8);
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    in.close();

                    String jsonStr = sb.toString();

                    // try parse the string to a JSON object
                    try {

                        jsonObj = new JSONObject(jsonStr);
                    } catch (JSONException e) {
                        Log.e("ERROR", "Error parsing data into JSON object: " + e.toString());
                    }

                } catch (Exception e) {
                    Log.e("ERROR", "Error retrieving data from URL: " + e.toString());
                }

            } finally {
                https_connection.disconnect();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e("ERROR", "MalformedURLException: " + e.toString());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("ERROR", "IOException: " + e.toString());
        }

        return jsonObj;
    }

    protected HashMap<String, String> doInBackground(Void... urls) {

        /* Retrieve ticker information from exchange. */
        JSONObject jsonURLObj =
                extractJSONObjectFromURL("https://www.okcoin.cn/api/v1/ticker.do?symbol=btc_cny");

        /* Retrieve currency information from European Central Bank. */
        JSONObject jsonConversionRateObj =
                extractJSONObjectFromURL("https://api.fixer.io/latest?base=USD");

        if ( jsonURLObj == null || jsonConversionRateObj == null ) return null;

        /* Create a map to store the current data. */
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("date", "Mon 12:00 AM");
        hashMap.put("last", "$0000.00");
        hashMap.put("volume", "0.0");

        double currentConversionRate = 0;

        try {
            hashMap.put("date", jsonURLObj.get("date").toString());
            hashMap.put("last", jsonURLObj.getJSONObject("ticker").get("last").toString());
            hashMap.put("volume", jsonURLObj.getJSONObject("ticker").get("vol").toString());

            currentConversionRate =
                    Double.parseDouble(jsonConversionRateObj.getJSONObject("rates").get("CNY").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /* Format Date */
        String formattedDate = new SimpleDateFormat("E hh:mm a")
                .format(new java.util.Date (Long.parseLong(hashMap.get("date"))*1000));
        hashMap.put("date", formattedDate);

        /* Format Volume */
        DecimalFormat dfVol = new DecimalFormat("###,###,###,##0");

        String volume = dfVol.format(Double.parseDouble(hashMap.get("volume")));
        hashMap.put("volume", volume);

        /* Format Decimal for last, high, and low price. */
        DecimalFormat dfDigits = new DecimalFormat();
        dfDigits.setMaximumFractionDigits(2);

        double last_usd = Double.parseDouble(hashMap.get("last")) / currentConversionRate;

        hashMap.put("last", dfDigits.format(last_usd));

        return hashMap;
    }

    @Override
    protected void onPostExecute(HashMap<String, String> result) {

        if ( result != null ) {

            AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
            // Get widget ids for widgets created
            ComponentName widget = new ComponentName(context, MainActivity.class);
            int[] widgetIds = widgetManager.getAppWidgetIds(widget);

            // Update Text
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                                                        R.layout.activity_main);
            if ( result.containsKey("date") ) {
                remoteViews.setTextViewText(R.id.lastUpdatedTextView,
                                            result.get("date"));
            }
            if ( result.containsKey("last") ) {
                remoteViews.setTextViewText(R.id.lastPriceTextView,
                                            "$" + result.get("last"));
            }
            if ( result.containsKey("volume") ) {
                /* char symbol = '\u0243'; */
                remoteViews.setTextViewText(R.id.volumeTextView,
                                            result.get("volume"));
            }
            // Refresh widget to show text
            widgetManager.updateAppWidget(widgetIds, remoteViews);
        }
    }
}
