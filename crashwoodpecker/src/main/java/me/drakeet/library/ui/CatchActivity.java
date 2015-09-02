package me.drakeet.library.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.google.gson.Gson;
import java.util.ArrayList;
import me.drakeet.library.R;
import me.drakeet.library.model.CrashCause;
import me.drakeet.library.model.CrashLogs;

/**
 * Created by drakeet(http://drakeet.me)
 * Date: 8/31/15 22:42
 */
public class CatchActivity extends AppCompatActivity {

    public final static String EXTRA_CRASH_LOGS = "extra_crash_logs";
    private ArrayList<CrashCause> mCrashList;
    private RecyclerView mRecyclerView;
    CrashListAdapter mCrashListAdapter;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catch);
        parseIntent();
        setUpRecyclerView();
    }

    private void parseIntent() {
        String json = getIntent().getStringExtra(EXTRA_CRASH_LOGS);
        CrashLogs crashLogs = new Gson().fromJson(json, CrashLogs.class);
        mCrashList = crashLogs.crashCauses;
    }

    private void setUpRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_crash);
        final LinearLayoutManager layoutManager =
                new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mCrashListAdapter = new CrashListAdapter(this, mCrashList);
        mRecyclerView.setAdapter(mCrashListAdapter);

        //mMeizhiListAdapter.setOnMeizhiTouchListener(getOnMeizhiTouchListener());
    }
}
