package org.intermine.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.intermine.app.R;
import org.intermine.app.util.Collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.ButterKnife;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class SimpleAdapter<T> extends BaseAdapter {
    private Context mContext;
    private List<T> mTitles;

    public SimpleAdapter(Context context) {
        mContext = context;
    }

    public void updateData(Collection<T> data) {
        mTitles = new ArrayList<>(data);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (Collections.isNullOrEmpty(mTitles)) {
            return 0;
        }
        return mTitles.size();
    }

    @Override
    public Object getItem(int position) {
        if (Collections.isNullOrEmpty(mTitles)) {
            return null;
        }
        return mTitles.get(position);
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
            convertView = inflater.inflate(R.layout.simple_spinner_item, parent, false);

            textView = ButterKnife.findById(convertView, R.id.title);
            convertView.setTag(R.id.title, textView);
        } else {
            textView = (TextView) convertView.getTag(R.id.title);
        }

        T title = mTitles.get(position);
        textView.setText(title.toString());
        return convertView;
    }
}
