package me.drakeet.library.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import me.drakeet.library.R;

/**
 * Created by drakeet(http://drakeet.me)
 * Date: 8/31/15 22:42
 */
public class CatchActivity extends AppCompatActivity {

    public final static String EXTRA_CRASH_LOGS = "extra_crash_logs";
    public final static String EXTRA_PACKAGE = "extra_package";
    private RecyclerView mRecyclerView;
    CrashListAdapter mCrashListAdapter;
    private String[] mCrashArray;
    private String mPackageName;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catch);
        parseIntent();
        setTitle("Crash in " + mPackageName);
        setUpRecyclerView();
    }

    private void parseIntent() {
        mCrashArray = getIntent().getStringArrayExtra(EXTRA_CRASH_LOGS);
        mPackageName = getIntent().getStringExtra(EXTRA_PACKAGE);
    }

    private void setUpRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_crash);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mCrashListAdapter = new CrashListAdapter(mCrashArray, mPackageName);
        mRecyclerView.setAdapter(mCrashListAdapter);
    }
}
