package org.intermine.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;
import org.intermine.R;
import org.intermine.core.ListItems;
import org.intermine.util.Collections;
import org.intermine.util.Strs;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class ListAdapter extends BaseAdapter {
    private final Context mContext;
    private final LayoutInflater mLayoutInflater;

    private List<String> mFeaturesName;
    private List<List<String>> mFeatures;

    private String mTemplate;

    public ListAdapter(Context ctx) {
        mContext = ctx;

        mLayoutInflater = LayoutInflater.from(ctx);
        mFeatures = new ArrayList<List<String>>();
    }

    public void updateData(ListItems listItems) {
        mFeaturesName = listItems.getFeaturesNames();

        if (!Collections.isNullOrEmpty(listItems.getFeatures())) {
            mFeatures.addAll(listItems.getFeatures());
        }

        mTemplate = generateTemplate(listItems.getFeaturesNames());

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (null != mFeatures) {
            return mFeatures.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (null != mFeatures) {
            return mFeatures.get(position);
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

    protected String generateTemplate(List<String> featuresNames) {
        if (!Collections.isNullOrEmpty(featuresNames)) {
            List<String> templateParts = Collections.newArrayList();

            for (String featureName : featuresNames) {
                int dotIndex = featureName.indexOf(">");

                if (dotIndex > -1) {
                    featureName = featureName.substring(dotIndex + 2);
                }
                templateParts.add(featureName + ": %s");
            }
            return StringUtils.join(templateParts, ", \n");
        }
        return Strs.EMPTY_STRING;
    }
}
