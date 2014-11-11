package org.intermine.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.intermine.R;
import org.intermine.util.Collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.ButterKnife;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class MinesAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> mMineNames;

    public MinesAdapter(Context context) {
        mContext = context;
    }

    public void updateMines(Collection<String> mineNames) {
        mMineNames = new ArrayList<>(mineNames);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (Collections.isNullOrEmpty(mMineNames)) {
            return 0;
        }
        return mMineNames.size();
    }

    @Override
    public Object getItem(int position) {
        if (Collections.isNullOrEmpty(mMineNames)) {
            return null;
        }
        return mMineNames.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView;

        if (null == convertView) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.mine_spinner_item, parent, false);

            textView = ButterKnife.findById(convertView, R.id.mine_name);
            convertView.setTag(R.id.mine_name, textView);
        } else {
            textView = (TextView) convertView.getTag(R.id.mine_name);
        }

        String mineName = mMineNames.get(position);
        textView.setText(mineName);
        return convertView;
    }
}
