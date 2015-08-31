package me.drakeet.library.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import me.drakeet.library.CrashLogs
import me.drakeet.library.R

/**
 * Created by drakeet on 8/31/15.
 */
public class CatchActivity: AppCompatActivity() {

    val EXTRA_CRASH_LOGS = "EXTRA_CRASH_LOGS"
    var mTextView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catch)
        val crashLogs: CrashLogs = getIntent().getSerializableExtra(EXTRA_CRASH_LOGS) as CrashLogs
        mTextView = findViewById(R.id.text) as TextView
        mTextView?.setText("" + crashLogs.crashCauses?.count())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        getMenuInflater().inflate(R.menu.menu_catch, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.getItemId()

        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}
