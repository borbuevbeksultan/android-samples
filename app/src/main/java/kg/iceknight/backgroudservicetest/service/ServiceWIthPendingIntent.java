package kg.iceknight.backgroudservicetest.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import kg.iceknight.backgroudservicetest.PendingActivity;

import static kg.iceknight.backgroudservicetest.PendingActivity.PARAM_RESULT;
import static kg.iceknight.backgroudservicetest.PendingActivity.STATUS_FINISH;



/*
    Обмен данными с сервисом происходит следующим образом:
    Сервис вызывается с Intent'ом который содержит в себе PendingIntent (отслеживаемый intent, который завязан к вызываеющему контексту)
    Конструктор PendingIntent принимает код запроса - указатель на этот PendingIntent
    При передаче в сервис через Intent сервис получает этот PendingIntent через вызов getParcelableIntent
     Обратная передача PendingIntent в вызывающий его контекст достигается вызовом его метода send()
     В параметрах код результата либо другой Intent с указанием текущего контекста и код запроса
     Вызывающий Intent отлавливает результат в метода onActivityResult(int requestCode, int resultCode, Intent data)
     где -
     requestCode, указатель на PendingIntent (он передается при создании PendingIntent методом  createPendingResult(TASK_CODE1, new Intent(), 0);)
     resultCode, передается в методе send()
     Intent - данные с резльтата

* */

public class ServiceWIthPendingIntent extends Service {

    final String LOG_TAG = "LOGS";
    ExecutorService executorService;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "Service onCreate");
        executorService = Executors.newFixedThreadPool(2);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "Service onDestroy");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "Service onStartCommand");

        int time = intent.getIntExtra(PendingActivity.PARAM_TIME, 1);

        PendingIntent pendingIntent = intent.getParcelableExtra(PendingActivity.PARAM_INTENT);

        MyRun myRun = new MyRun(time, startId, pendingIntent);
        executorService.execute(myRun);

        return super.onStartCommand(intent, flags, startId);
    }

    class MyRun implements Runnable {
        int time;
        int startId;
        PendingIntent pendingIntent;

        public MyRun(int time, int startId, PendingIntent pendingIntent) {
            this.time = time;
            this.startId = startId;
            this.pendingIntent = pendingIntent;
        }

        @Override
        public void run() {
            try {
                pendingIntent.send(PendingActivity.STATUS_START);
                TimeUnit.SECONDS.sleep(time);

                Intent intent = new Intent()
                        .putExtra(PARAM_RESULT, time * 100);

                pendingIntent.send(ServiceWIthPendingIntent.this, STATUS_FINISH, intent);
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            stop();
        }

        private void stop() {
            Log.d(LOG_TAG, "Runnable#" + startId + " end, stopSelfResult" + startId + stopSelfResult(startId));
        }
    }

}
