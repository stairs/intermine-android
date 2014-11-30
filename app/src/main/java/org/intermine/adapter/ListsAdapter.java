package org.intermine.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.intermine.R;
import org.intermine.core.List;
import org.intermine.util.Strs;

import java.util.ArrayList;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class ListsAdapter extends RecyclerView.Adapter<ListsAdapter.ViewHolder> {
    private ArrayList<List> mLists;
    private OnItemClickListener mListener;

    public ListsAdapter(ArrayList<List> lists, OnItemClickListener listener) {
        mLists = lists;
        mListener = listener;
    }

    @Override
    public ListsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        View parent = LayoutInflater.from(context).inflate(R.layout.lists_item, viewGroup, false);
        return ViewHolder.newInstance(parent);
    }

    @Override
    public void onBindViewHolder(ListsAdapter.ViewHolder viewHolder, int position) {
        final List list = mLists.get(position);
        viewHolder.setTitle(list.getTitle());
        Spanned description = Html.fromHtml(Strs.stripFromBr(list.getDescription()));
        viewHolder.setDescription(description);
        viewHolder.setSize(Integer.toString(list.getSize()));
        viewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(list);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (null != mLists) {
            return mLists.size();
        }
        return 0;
    }

    public static interface OnItemClickListener {
        void onItemClick(List list);
    }

    public static final class ViewHolder extends RecyclerView.ViewHolder {
        private final View parent;
        private final TextView mTitle;
        private final TextView mDescription;
        private final TextView mSize;

        public static ViewHolder newInstance(View parent) {
            TextView title = (TextView) parent.findViewById(R.id.list_title);
            TextView description = (TextView) parent.findViewById(R.id.list_description);
            TextView size = (TextView) parent.findViewById(R.id.list_size);
            return new ViewHolder(parent, title, description, size);
        }

        private ViewHolder(View parent, TextView title, TextView description, TextView size) {
            super(parent);
            this.parent = parent;
            this.mTitle = title;
            this.mDescription = description;
            this.mSize = size;
        }

        public void setTitle(CharSequence text) {
            mTitle.setText(text);
        }

        public void setDescription(CharSequence text) {
            mDescription.setText(text);
        }

        public void setSize(CharSequence text) {
            mSize.setText(text);
        }

        public void setOnClickListener(View.OnClickListener listener) {
            parent.setOnClickListener(listener);
        }
    }
}