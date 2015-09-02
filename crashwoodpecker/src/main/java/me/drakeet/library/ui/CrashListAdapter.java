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

import android.graphics.Typeface;
import android.support.v4.widget.Space;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import me.drakeet.library.R;
import me.drakeet.library.StringStyleUtils;

/**
 * Created by drakeet on 6/20/15.
 */
public class CrashListAdapter extends RecyclerView.Adapter<CrashListAdapter.ViewHolder> {

    public static final String TAG = "CrashListAdapter";

    private String[] mData;
    private String mPackageName;

    public CrashListAdapter(String[] strings, String pack) {
        mData = strings;
        mPackageName = pack;
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_crash_cat, parent, false);
        return new ViewHolder(v);
    }

    @Override public void onBindViewHolder(final ViewHolder holder, final int position) {
        String crash = mData[position];
        holder.log = crash;
        if (crash != null) {
            setSpaceState(holder, /*show = */ crash.startsWith("at "));

            if (crash.startsWith("Caused by")) {
                holder.title.setTypeface(null, Typeface.BOLD);
                holder.title.setTextColor(0xdeffffff);
            }
            else {
                holder.title.setTypeface(null, Typeface.NORMAL);
                holder.title.setTextColor(0xffef4545);
            }

            if (crash.contains(mPackageName)) {
                int indexOfC = crash.indexOf("(");
                String atPackage = crash.substring(0, indexOfC);
                SpannableStringBuilder builder = new SpannableStringBuilder(atPackage).append(
                        StringStyleUtils.format(holder.title.getContext(),
                                crash.substring(indexOfC), R.style.LineTextAppearance));
                CharSequence title = builder.subSequence(0, builder.length());
                holder.title.setText(title);
            }
            else {
                holder.title.setText(crash);
            }
        }
    }

    @Override public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override public int getItemCount() {
        return mData == null ? 0 : mData.length;
    }

    public void setSpaceState(ViewHolder holder, boolean show) {
        if (!show) {
            holder.space.setVisibility(View.GONE);
        }
        else {
            holder.space.setVisibility(View.VISIBLE);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        Space space;
        String log;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tv_trace);
            space = (Space) itemView.findViewById(R.id.space);
            itemView.setOnClickListener(this);
        }

        @Override public void onClick(View v) {
            if (log.endsWith("more")) {
                Toast.makeText(v.getContext(), "It is not supported temporarily.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
