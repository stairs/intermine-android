package org.intermine.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.intermine.R;
import org.intermine.util.Collections;
import org.intermine.util.Strs;

import java.util.List;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class ListAdapter extends BaseAdapter {
    private final Context mContext;
    private final LayoutInflater mLayoutInflater;

    private List<String> mFields;
    private List<List<String>> mListItems;

    private String mTemplate;

    public ListAdapter(Context ctx) {
        mContext = ctx;

        mLayoutInflater = LayoutInflater.from(ctx);
    }

    public void updateData(List<String> fields, List<List<String>> listItems) {
        mFields = fields;
        mListItems = listItems;

        mTemplate = generateTemplate(fields);

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (null != mListItems) {
            return mListItems.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (null != mListItems) {
            return mListItems.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView listItem;

        if (null == convertView) {
            convertView = mLayoutInflater.inflate(R.layout.list_item, parent, false);

            listItem = (TextView) convertView.findViewById(R.id.list_item);
            convertView.setTag(R.id.list_item, listItem);
        } else {
            listItem = (TextView) convertView.getTag(R.id.list_item);
        }

        List<String> listItemFeatures = (List<String>) getItem(position);
        String text = String.format(mTemplate, listItemFeatures.toArray());
        listItem.setText(text);

        return convertView;
    }

    protected String generateTemplate(List<String> fields) {
        if (!Collections.isNullOrEmpty(fields)) {
            StringBuilder stringBuilder = new StringBuilder();

            for (String field : fields) {
                int dotIndex = field.indexOf(">");

                if (dotIndex > -1) {
                    field = field.substring(dotIndex + 2);
                }
                stringBuilder.append(field).append(": %s\n");
            }
            return stringBuilder.toString();

        }
        return Strs.EMPTY_STRING;
    }
}
