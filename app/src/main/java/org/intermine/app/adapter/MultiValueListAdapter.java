package org.intermine.app.adapter;

/*
 * Copyright (C) 2015 InterMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.common.collect.Sets;

import org.intermine.app.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import butterknife.ButterKnife;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class MultiValueListAdapter extends RecyclerView.Adapter<MultiValueListAdapter.ViewHolder> {
    private List<String> mValues;
    private Set<String> mSelected;

    public MultiValueListAdapter(Collection<String> values) {
        mValues = new ArrayList<>(values);
        mSelected = Sets.newHashSet();
    }

    @Override
    public MultiValueListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.checkbox_list_item, parent, false);
        return ViewHolder.newInstance(view);
    }

    @Override
    public void onBindViewHolder(MultiValueListAdapter.ViewHolder holder, final int position) {
        holder.mValue.setText(mValues.get(position));
        holder.mCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String value = mValues.get(position);

                if (isChecked) {
                    mSelected.add(value);
                } else {
                    mSelected.remove(value);
                }
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public Set<String> getSelected() {
        return mSelected;
    }

    public static final class ViewHolder extends RecyclerView.ViewHolder {
        private final View mParent;
        private final TextView mValue;
        private final CheckBox mCheckbox;

        public static ViewHolder newInstance(View parent) {
            TextView value = (TextView) parent.findViewById(R.id.value);
            CheckBox checkBox = ButterKnife.findById(parent, R.id.checkbox);
            return new ViewHolder(parent, value, checkBox);
        }

        private ViewHolder(View parent, TextView value, CheckBox checkBox) {
            super(parent);
            this.mParent = parent;
            this.mValue = value;
            this.mCheckbox = checkBox;
        }

        public void setOnClickListener(View.OnClickListener listener) {
            mParent.setOnClickListener(listener);
        }
    }
}