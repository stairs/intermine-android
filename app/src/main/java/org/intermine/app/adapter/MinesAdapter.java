package org.intermine.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import org.intermine.app.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class MinesAdapter extends BaseAdapter {
    private final Context mContext;
    private final LayoutInflater mLayoutInflater;

    private List<String> mMines = Collections.emptyList();
    private Map<String, Boolean> mMinesToState = Collections.emptyMap();

    public MinesAdapter(Context ctx) {
        mContext = ctx;
        mLayoutInflater = LayoutInflater.from(ctx);
    }

    public void updateMines(Map<String, Boolean> minesToState) {
        if (null != minesToState) {
            this.mMinesToState = minesToState;
            this.mMines = new ArrayList<>(minesToState.keySet());
        } else {
            this.mMinesToState = Collections.emptyMap();
            this.mMines = Collections.emptyList();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mMinesToState.size();
    }

    @Override
    public Object getItem(int position) {
        return mMines.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final TextView title;
        CheckBox checkBox;

        if (null == convertView) {
            convertView = mLayoutInflater.inflate(R.layout.mines_list_item, parent, false);

            title = (TextView) convertView.findViewById(R.id.mine_name);
            checkBox = (CheckBox) convertView.findViewById(R.id.mine_checkbox);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mMinesToState.put(title.getText().toString(), isChecked);
                }
            });

            convertView.setTag(R.id.mine_name, title);
            convertView.setTag(R.id.mine_checkbox, checkBox);
        } else {
            title = (TextView) convertView.getTag(R.id.mine_name);
            checkBox = (CheckBox) convertView.getTag(R.id.mine_checkbox);
        }

        String mine = (String) getItem(position);
        title.setText(mine);
        checkBox.setChecked(mMinesToState.get(mine));
        return convertView;
    }

    public Map<String, Boolean> getMinesToState() {
        return mMinesToState;
    }
}