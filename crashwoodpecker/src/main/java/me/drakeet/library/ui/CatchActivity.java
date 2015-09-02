/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 drakeet (drakeet.me@gmail.com)
 * http://drakeet.me
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.drakeet.library.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
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
        if (mPackageName != null) setTitle("Crashes in " + mPackageName);
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

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(R.string.by_drakeet)
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override public boolean onMenuItemClick(MenuItem item) {
                        Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse("http://drakeet.me"));
                        startActivity(it);
                        return true;
                    }
                });
        return true;
    }
}
