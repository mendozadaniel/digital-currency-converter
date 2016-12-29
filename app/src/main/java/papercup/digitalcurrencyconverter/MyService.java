package papercup.digitalcurrencyconverter;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


/**
 * Created by danielmendoza on 9/23/16.
 *
 * This service is trigger by the Alarm Manager and it starts an async task.
 */
public class MyService extends Service {
    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        update();
        return super.onStartCommand(intent, flags, startId);
    }

    private void update() {
        // Start Async task to grab data from OKCoin exchange
        MyAsyncTask asyncTask = new MyAsyncTask(getApplicationContext());
        asyncTask.execute();
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
