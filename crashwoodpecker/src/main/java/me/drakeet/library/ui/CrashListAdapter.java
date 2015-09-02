/*
 * Copyright (C) 2015 Drakeet <drakeet.me@gmail.com>
 *
 * This file is part of Meizhi
 *
 * Meizhi is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Meizhi is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Meizhi.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.drakeet.library.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import me.drakeet.library.R;

/**
 * Created by drakeet on 6/20/15.
 */
public class CrashListAdapter extends RecyclerView.Adapter<CrashListAdapter.ViewHolder> {

    public static final String TAG = "CrashListAdapter";

    private String[] mData;
    private Context mContext;

    public CrashListAdapter(Context context, @NonNull String[] strings) {
        mData = strings;
        mContext = context;
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_crash_cat, parent, false);
        return new ViewHolder(v);
    }

    @Override public void onBindViewHolder(final ViewHolder holder, final int position) {
        String crash = mData[position];
        if (crash != null) {
            holder.titleView.setText(crash);
        }
    }

    @Override public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override public int getItemCount() {
        return mData.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView titleView;

        public ViewHolder(View itemView) {
            super(itemView);
            titleView = (TextView) itemView.findViewById(R.id.tv_trace);
            itemView.setOnClickListener(this);
        }

        @Override public void onClick(View v) {
            // TODO: 9/2/15
        }
    }
}
