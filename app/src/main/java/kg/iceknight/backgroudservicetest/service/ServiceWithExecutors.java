package kg.iceknight.backgroudservicetest.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ServiceWithExecutors extends Service {

    final String LOG_TAG = "ServiceWithExecutors";
    ExecutorService executorService;
    Object someResult;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "ServiceWithExecutors onCreate");
        executorService = Executors.newFixedThreadPool(3);
        someResult = new Object();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "ServiceWithExecutors onDestroy");
        someResult = null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "ServiceWithExecutors onStartCommand");
        int time = intent.getIntExtra("time", 1);
        MyRun myRun = new MyRun(time, startId);

        executorService.execute(myRun);
        return super.onStartCommand(intent, flags, startId);
    }

    class MyRun implements Runnable {
        int time;
        int startId;

        public MyRun(int time, int startId) {
            this.time = time;
            this.startId = startId;
            Log.d(LOG_TAG, "MyRun#" + startId + " create");
        }

        @Override
        public void run() {
            Log.d(LOG_TAG, "MyRun#" + startId + " start, time = " + time);
            try {
                TimeUnit.SECONDS.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                Log.d(LOG_TAG, "MyRun#" + startId + " error, null pointer");
            }
            stop();
        }

        void stop() {
            Log.d(LOG_TAG, "MyRun# " + startId + " end, stopSelf " + startId);
            stopSelf();
        }
    }

    /*
    Сервис вызывается командой startService()
    Сервис останавливается командой stopSelf(), ее вызывает сам сервис внутри себя
    Каждый раз когда уже рабочий сервис вызывается, ей передается число-счетчик startId
     И остановить тот вызов который пришел, можно командой stopSelf(startId)
     Сервис уничтожается когда самый последний из запросов вызвал stopSelf(startId), но тем не менее
     есть обрабатываемые запросы в пуле потоков.
     Сервис работает в том же потоке, что и UI
     Надо создавать новые потоки
     Можно использовать пул потоков, для ограниченного кол-ва обработок
    * */

}
