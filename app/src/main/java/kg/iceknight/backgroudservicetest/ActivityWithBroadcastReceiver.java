package kg.iceknight.backgroudservicetest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import kg.iceknight.backgroudservicetest.service.ServiceWithBroadcastReceiver;

public class ActivityWithBroadcastReceiver extends AppCompatActivity {

    public static final String ACTION = "kg.iceknight.broadcastAction1";

    private BroadcastReceiver broadcastReceiver;

    public static final int STATUS_START = 100;
    public static final int STATUS_FINISH = 200;

    final int TASK1_CODE = 1;
    final int TASK2_CODE = 2;
    final int TASK3_CODE = 3;

    private TextView textView1;
    private TextView textView2;
    private TextView textView3;


    public static final String PARAM_TIME = "time";
    public static final String PARAM_TASK = "task";
    public static final String PARAM_RESULT = "result";
    public static final String PARAM_STATUS = "status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_broadcast_receiver);

        textView1 = findViewById(R.id.tv1);
        textView2 = findViewById(R.id.tv2);
        textView3 = findViewById(R.id.tv3);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int task = intent.getIntExtra(PARAM_TASK, 0);
                int status = intent.getIntExtra(PARAM_STATUS, 0);

                if (status == STATUS_START) switch (task) {
                    case TASK1_CODE:
                        textView1.setText("started");
                        break;
                    case TASK2_CODE:
                        textView2.setText("started");
                        break;
                    case TASK3_CODE:
                        textView3.setText("started");
                        break;
                }

                if (status == STATUS_FINISH) switch (task) {
                    case TASK1_CODE:
                        textView1.setText("started finished " + intent.getIntExtra(PARAM_RESULT, 0));
                        break;
                    case TASK2_CODE:
                        textView2.setText("started finished " + intent.getIntExtra(PARAM_RESULT, 0));
                        break;
                    case TASK3_CODE:
                        textView3.setText("started finished " + intent.getIntExtra(PARAM_RESULT, 0));
                        break;
                }

            }
        };

        IntentFilter intentFilter = new IntentFilter(ACTION);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    public void onCLickStart(View view) {
        Intent intent = new Intent(this, ServiceWithBroadcastReceiver.class)
                .putExtra(PARAM_TASK, TASK1_CODE);
        startService(intent);

        intent = new Intent(this, ServiceWithBroadcastReceiver.class)
                .putExtra(PARAM_TASK, TASK2_CODE);
        startService(intent);

        intent = new Intent(this, ServiceWithBroadcastReceiver.class)
                .putExtra(PARAM_TASK, TASK3_CODE);
        startService(intent);

    }
}
