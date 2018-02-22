package kg.iceknight.backgroudservicetest.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import kg.iceknight.backgroudservicetest.ActivityWithBroadcastReceiver;

/*
    Класс BroadcastReceiver является слушателем
     Он регистрируется через вызов метода registerReceiver(BroadcastReceiver, IntentFilter)
     Intent FilterFilter задает action на который реагирует (после регистрации) BroadcastReceiver
     BroadcastReceiver должен реализовать метод onReceive(context, intent), в котором обрабатывается сообщение
     Intent с action создается через конструктор
     А последующий вызов sendBroadcast(intent) посылает сигнал в систему
     и все зарегистрированные слушатели с данным action'ом примут данный intent
* */

public class ServiceWithBroadcastReceiver extends Service {

    ExecutorService es;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        es = Executors.newFixedThreadPool(2);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int time = intent.getIntExtra(ActivityWithBroadcastReceiver.PARAM_TIME, 1);
        int task = intent.getIntExtra(ActivityWithBroadcastReceiver.PARAM_TASK, 0);

        MyRun myRun = new MyRun(time, task);
        es.execute(myRun);

        return super.onStartCommand(intent, flags, startId);
    }

    class MyRun implements Runnable {
        int time;
        int task;

        MyRun(int time, int task) {
            this.time = time;
            this.task = task;
        }

        @Override
        public void run() {
            try {
                Intent resultIntent = new Intent(ActivityWithBroadcastReceiver.ACTION);

                resultIntent.putExtra(ActivityWithBroadcastReceiver.PARAM_TASK, task);
                resultIntent.putExtra(ActivityWithBroadcastReceiver.PARAM_STATUS, ActivityWithBroadcastReceiver.STATUS_START);
                sendBroadcast(resultIntent);
                TimeUnit.SECONDS.sleep(time);
                resultIntent.putExtra(ActivityWithBroadcastReceiver.PARAM_STATUS, ActivityWithBroadcastReceiver.STATUS_FINISH);
                resultIntent.putExtra(ActivityWithBroadcastReceiver.PARAM_RESULT, time * 100);
                sendBroadcast(resultIntent);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



}
