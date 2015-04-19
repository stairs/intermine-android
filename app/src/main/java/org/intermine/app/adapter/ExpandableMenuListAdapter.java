package org.intermine.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import org.intermine.app.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class ExpandableMenuListAdapter extends BaseExpandableListAdapter {
    private final Context mContext;
    private final LayoutInflater mInflater;

    private Map<String, List<String>> mGroups;
    private List<String> mMenus;

    public ExpandableMenuListAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public void updateMenuList(Map<String, List<String>> groups) {
        mGroups = groups;
        mMenus = new ArrayList<>(groups.keySet());
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return mGroups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mGroups.get(mMenus.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mGroups.get(mMenus.get(groupPosition));
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mGroups.get(mMenus.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        TextView menuItem;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.menu_list_item, parent, false);
            menuItem = ButterKnife.findById(convertView, R.id.menu_item);
            convertView.setTag(R.id.menu_item, menuItem);
        } else {
            menuItem = (TextView) convertView.getTag(R.id.menu_item);
        }

        final int childrenCount = getChildrenCount(groupPosition);

        if (0 == childrenCount) {
            menuItem.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        } else {
            if (isExpanded) {
                menuItem.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_expand_less_grey, 0);
            } else {
                menuItem.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_expand_more_grey, 0);
            }
        }

        String menuTitle = mMenus.get(groupPosition);
        menuItem.setText(menuTitle);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        TextView submenuItem;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.submenu_list_item, parent, false);
            submenuItem = ButterKnife.findById(convertView, R.id.submenu_item);
            convertView.setTag(R.id.submenu_item, submenuItem);
        } else {
            submenuItem = (TextView) convertView.getTag(R.id.submenu_item);
        }

        String submenuTitle = (String) getChild(groupPosition, childPosition);
        submenuItem.setText(submenuTitle);
        return convertView;
    }
}