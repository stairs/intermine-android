package org.intermine.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.intermine.R;
import org.intermine.core.List;
import org.intermine.util.Strs;

import java.util.ArrayList;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class ListsAdapter extends BaseAdapter {
    private final Context mContext;
    private final LayoutInflater mLayoutInflater;

    private ArrayList<List> mLists;

    public ListsAdapter(Context ctx) {
        mContext = ctx;
        mLayoutInflater = LayoutInflater.from(ctx);
    }

    public void setLists(ArrayList<List> lists) {
        mLists = lists;
    }

    @Override
    public int getCount() {
        if (null != mLists) {
            return mLists.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (null != mLists) {
            return mLists.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView title, description, size;

        if (null == convertView) {
            convertView = mLayoutInflater.inflate(R.layout.lists_item, parent, false);

            title = (TextView) convertView.findViewById(R.id.list_title);
            description = (TextView) convertView.findViewById(R.id.list_description);
            size = (TextView) convertView.findViewById(R.id.list_size);

            convertView.setTag(R.id.list_title, title);
            convertView.setTag(R.id.list_description, description);
            convertView.setTag(R.id.list_size, size);
        } else {
            title = (TextView) convertView.getTag(R.id.list_title);
            description = (TextView) convertView.getTag(R.id.list_description);
            size = (TextView) convertView.getTag(R.id.list_size);
        }

        List list = (List) getItem(position);

        title.setText(list.getTitle());
        description.setText(Html.fromHtml(Strs.stripFromBr(list.getDescription())));
        size.setText(Integer.toString(list.getSize()));
        return convertView;
    }
}
