package papercup.digitalcurrencyconverter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.Calendar;

/**
 * Created by danielmendoza on 9/23/16.
 *
 * The MainActivity is used to set the Alarm for the widget. The widget is going to repeat the call
 * at an interval specified by the user during the Configuration.
 */
public class MainActivity extends AppWidgetProvider {

    private static PendingIntent service = null;

    public void onReceive(Context context, Intent intent) {

        super.onReceive(context, intent);
    }

    public void onUpdate(Context context, AppWidgetManager appWidgetManager,int[] appWidgetIds) {

        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.PREFERENCE_FILE_KEY), Context.MODE_PRIVATE);

        String interval = sharedPref.getString("interval", null);
        int minutes = 30;

        if (interval != null) minutes = Integer.parseInt(interval);

        final AlarmManager alarmManager =
                (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        final Calendar TIME = Calendar.getInstance();
        TIME.set(Calendar.MINUTE, 0);
        TIME.set(Calendar.SECOND, 0);
        TIME.set(Calendar.MILLISECOND, 0);

        final Intent myServiceIntent = new Intent(context, MyService.class);

        if (service == null)
            service = PendingIntent.getService(context, 0, myServiceIntent, PendingIntent.FLAG_CANCEL_CURRENT);


        alarmManager.setRepeating(AlarmManager.RTC, TIME.getTime().getTime(), minutes * 60 * 1000, service);
    }

    @Override
    public void onDisabled(Context context) {

        final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(service);
    }
}