package kg.iceknight.backgroudservicetest;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import kg.iceknight.backgroudservicetest.service.ServiceWIthPendingIntent;

public class PendingActivity extends AppCompatActivity {

    final String LOG_TAG = "LOG";

    final int TASK_CODE1 = 1;
    final int TASK_CODE2 = 2;
    final int TASK_CODE3 = 3;

    public final static int STATUS_START = 100;
    public final static int STATUS_FINISH = 200;

    public static final String PARAM_TIME = "time";
    public static final String PARAM_INTENT = "pendingIntent";
    public static final String PARAM_RESULT = "result";

    public TextView textView1;
    public TextView textView2;
    public TextView textView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending);
        textView1 = findViewById(R.id.tv1);
        textView2 = findViewById(R.id.tv2);
        textView3 = findViewById(R.id.tv3);
    }


    public void onClickStart(View view) {
        PendingIntent pendingIntent;

        Intent intent;

        pendingIntent = createPendingResult(TASK_CODE1, new Intent(), 0);
        intent = new Intent(this, ServiceWIthPendingIntent.class)
                .putExtra(PARAM_INTENT, pendingIntent)
                .putExtra(PARAM_TIME, 7);
        startService(intent);

        pendingIntent = createPendingResult(TASK_CODE2, new Intent(), 0);
        intent = new Intent(this, ServiceWIthPendingIntent.class)
                .putExtra(PARAM_INTENT, pendingIntent)
                .putExtra(PARAM_TIME, 3);
        startService(intent);

        pendingIntent = createPendingResult(TASK_CODE3, new Intent(), 0);
        intent = new Intent(this, ServiceWIthPendingIntent.class)
                .putExtra(PARAM_INTENT, pendingIntent)
                .putExtra(PARAM_TIME, 5);
        startService(intent);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(LOG_TAG, "request Code = " + requestCode + ", resultCode = " + resultCode);

        if (resultCode == STATUS_START) switch (requestCode) {
            case TASK_CODE1:
                textView1.setText("started");
                break;
            case TASK_CODE2:
                textView2.setText("started");
                break;
            case TASK_CODE3:
                textView3.setText("started");
                break;
        }

        if (resultCode == STATUS_FINISH) switch (requestCode) {
            case TASK_CODE1:
                textView1.setText("started " + "finished" + data.getIntExtra(PARAM_RESULT, 0));
                break;
            case TASK_CODE2:
                textView2.setText("started " + "finished" + data.getIntExtra(PARAM_RESULT, 0));
                break;
            case TASK_CODE3:
                textView3.setText("started " + "finished" + data.getIntExtra(PARAM_RESULT, 0));
                break;
        }
    }
}
