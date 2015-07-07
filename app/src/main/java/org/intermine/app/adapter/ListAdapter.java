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
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.intermine.app.R;
import org.intermine.app.core.ListItems;
import org.intermine.app.core.Tree;
import org.intermine.app.util.Collections;
import org.intermine.app.util.Strs;

import java.util.List;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class ListAdapter extends BaseAdapter {
    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private final int mAccentColor;

    private List<List<String>> mFilteredFeatures;
    private List<List<String>> mFeatures;
    private Tree<String> mFeaturesNames;
    private String mRootAttribute;

    private String mQuery;

    public ListAdapter(Context ctx) {
        mContext = ctx;
        mAccentColor = ctx.getResources().getColor(R.color.im_green);

        mLayoutInflater = LayoutInflater.from(ctx);
        mFilteredFeatures = Collections.newArrayList();
        mFeatures = Collections.newArrayList();
    }

    public void addListItems(ListItems listItems, String rootAttribute) {
        mRootAttribute = rootAttribute;

        if (!Collections.isNullOrEmpty(listItems.getFeatures())) {
            mFeatures.addAll(listItems.getFeatures());
            filter(mQuery);
        }

        if (!Collections.isNullOrEmpty(listItems.getFeaturesNames())) {
            mFeaturesNames = generateAttributesTree(listItems.getFeaturesNames());
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (null != mFilteredFeatures) {
            return mFilteredFeatures.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (null != mFilteredFeatures) {
            return mFilteredFeatures.get(position);
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

        List<String> features = (List<String>) getItem(position);
        listItem.setText(generateText(mFeaturesNames, features));
        return convertView;
    }

    public void filter(String query) {
        mQuery = query;
        mFilteredFeatures.clear();

        if (Strs.isNullOrEmpty(query)) {
            mFilteredFeatures.addAll(mFeatures);
        } else {
            query = query.toLowerCase();
            for (List<String> featureSet : mFeatures) {
                for (String feature : featureSet) {
                    if ((!Strs.isNullOrEmpty(feature) && feature.toLowerCase().contains(query))) {
                        mFilteredFeatures.add(featureSet);
                        break;
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    public boolean isFilteredResultsEmpty() {
        return Collections.isNullOrEmpty(mFilteredFeatures);
    }

    protected SpannableStringBuilder generateText(final Tree<String> attributes, final List<String> values) {
        if (!Collections.isNullOrEmpty(values)) {
            AttributeNodeVisitor visitor = new AttributeNodeVisitor(values);
            attributes.visitNodes(visitor);
            return visitor.getResult();
        }
        return null;
    }

    private Tree<String> generateAttributesTree(List<String> attributes) {
        Tree<String> tree = new Tree<>();

        for (String attribute : attributes) {
            String[] parts = attribute.split(" > ");

            Tree.Node node = tree.getRootElement();
            for (String part : parts) {
                if (!part.equals(mRootAttribute)) {
                    node = node.addChild(part);
                }
            }
        }
        return tree;
    }

    private class AttributeNodeVisitor implements Tree.NodeVisitor<String> {
        private List<java.lang.String> mValues;

        private SpannableStringBuilder mBuilder;
        private int count = 0;

        public AttributeNodeVisitor(List<String> values) {
            this.mValues = values;
            mBuilder = new SpannableStringBuilder();
        }

        @Override
        public boolean visit(Tree.Node<String> node) {
            if (!Strs.isNullOrEmpty(node.getValue())) {
                if (Collections.isNullOrEmpty(node.getChildren())) {
                    String featureName = node.getValue() + ": ";
                    Spannable featureNameSpannable = Strs.spanWithBoldAndColorFont(featureName
                            + mValues.get(count), 0, featureName.length(), mAccentColor);
                    mBuilder.append(featureNameSpannable);
                    count++;
                } else {
                    Spannable section = Strs.spanCenteredBoldAndColored(node.getValue().toString(),
                            0, node.getValue().toString().length(), mAccentColor);
                    mBuilder.append(section);
                }
                mBuilder.append('\n');
            }
            return true;
        }

        public SpannableStringBuilder getResult() {
            int length = mBuilder.length();

            if ('\n' == mBuilder.charAt(length - 1)) {
                mBuilder.delete(length - 1, length);
            }
            return mBuilder;
        }
    }
}