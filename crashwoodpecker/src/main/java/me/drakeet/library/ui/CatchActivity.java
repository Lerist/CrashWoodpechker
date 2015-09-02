package me.drakeet.library.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import com.google.gson.Gson;
import me.drakeet.library.CrashLogs;
import me.drakeet.library.R;

/**
 * Created by drakeet(http://drakeet.me)
 * Date: 8/31/15 22:42
 */
public class CatchActivity extends AppCompatActivity {

    public final static String EXTRA_CRASH_LOGS = "extra_crash_logs";
    TextView mTextView;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catch);
        String json = getIntent().getStringExtra(EXTRA_CRASH_LOGS);
        CrashLogs crashLogs = new Gson().fromJson(json, CrashLogs.class);
        mTextView = (TextView) findViewById(R.id.tv_text);
        if (crashLogs != null)
            mTextView.setText("count:" + crashLogs.crashCauses.size());
    }
}
