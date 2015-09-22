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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
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

    private List<List<String>> mFilteredFeatures;
    private List<List<String>> mFeatures;
    private Tree mFeaturesNames;

    private String mQuery;

    public ListAdapter(Context ctx) {
        mContext = ctx;

        mLayoutInflater = LayoutInflater.from(ctx);
        mFilteredFeatures = Collections.newArrayList();
        mFeatures = Collections.newArrayList();
    }

    public void addListItems(ListItems listItems) {
        if (!Collections.isNullOrEmpty(listItems.getFeatures())) {
            mFeatures.addAll(listItems.getFeatures());
            filter(mQuery);
        }

        if (!Collections.isNullOrEmpty(listItems.getFeaturesNames())) {
            mFeaturesNames = generateAttributesTree(listItems.getFeaturesNames());
            mFeaturesNames.compact(mFeaturesNames.getRootElement());
            mFeaturesNames.computeDepthOfEachNode(mFeaturesNames.getRootElement());
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
        if (null == convertView) {
            convertView = mLayoutInflater.inflate(R.layout.list_item, parent, false);

            ViewCreatorVisitor visitor = new ViewCreatorVisitor((ViewGroup) convertView);
            mFeaturesNames.visitNodes(visitor);
        }

        List<String> features = (List<String>) getItem(position);
        AttributeNodeVisitor visitor = new AttributeNodeVisitor(features, (ViewGroup) convertView);
        mFeaturesNames.visitNodes(visitor);
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

    private Tree generateAttributesTree(List<String> attributes) {
        Tree tree = new Tree();
        int number = 0;

        for (String attribute : attributes) {
            String[] parts = attribute.split(" > ");

            Tree.Node node = tree.getRootElement();
            for (String part : parts) {
                node = node.addChild(part);
            }
            node.setNumber(number++);
        }
        return tree;
    }

    private class AttributeNodeVisitor implements Tree.NodeVisitor {
        private ViewGroup mContainer;
        private List<java.lang.String> mValues;

        public AttributeNodeVisitor(List<String> values, ViewGroup container) {
            this.mValues = values;
            mContainer = container;
        }

        @Override
        public boolean visit(Tree.Node node) {
            if (null != node.getValue()) {
                if (node.getChildren().isEmpty()) {
                    TextView attributeValue = (TextView) mContainer.findViewWithTag(node.getNumber());
                    attributeValue.setText(mValues.get(node.getNumber()));
                }
            }
            return true;
        }
    }

    private class ViewCreatorVisitor implements Tree.NodeVisitor {
        private ViewGroup mContainer;

        public ViewCreatorVisitor(ViewGroup container) {
            mContainer = container;
        }

        @Override
        public boolean visit(Tree.Node node) {
            if (!Strs.isNullOrEmpty(node.getValue())) {
                LayoutInflater inflater = LayoutInflater.from(mContext);

                if (!Collections.isNullOrEmpty(node.getChildren())) {
                    TextView rowTitle = (TextView) inflater.inflate(R.layout.attribute_class_title, null);
                    rowTitle.setText(node.getValue());
                    rowTitle.setGravity(Gravity.CENTER);
                    mContainer.addView(rowTitle);
                } else {
                    LinearLayout row = (LinearLayout) inflater.inflate(R.layout.attribute_row, null);
                    TextView attributeTitle = (TextView) row.findViewById(R.id.attribute_title);
                    attributeTitle.setText(node.getValue());

                    TextView attributeValue = (TextView) row.findViewById(R.id.attribute_value);
                    attributeValue.setTag(node.getNumber());
                    mContainer.addView(row);
                }
            }
            return true;
        }
    }
}