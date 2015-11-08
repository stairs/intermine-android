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
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.intermine.app.R;
import org.intermine.app.core.List;
import org.intermine.app.core.templates.Template;
import org.intermine.app.util.Strs;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class TemplatesAdapter extends RecyclerView.Adapter<TemplatesAdapter.ViewHolder> {
    private ArrayList<Template> mFilteredTemplates;
    private ArrayList<Template> mTemplates;
    private OnItemClickListener mListener;

    public TemplatesAdapter(OnItemClickListener mListener) {
        this.mListener = mListener;
    }

    public void updateData(Collection<Template> templates) {
        mTemplates = new ArrayList<>(templates);
        mFilteredTemplates = new ArrayList<>(templates);
        notifyDataSetChanged();
    }

    @Override
    public TemplatesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.templates_item, parent, false);
        return ViewHolder.newInstance(view);
    }

    @Override
    public void onBindViewHolder(TemplatesAdapter.ViewHolder holder, int position) {
        final Template template = getTemplate(position);
        holder.mTitle.setText(template.getTitle());
        Spanned descriptionText = Html.fromHtml(Strs.nullToEmpty(template.getDescription()));
        holder.mDescription.setText(descriptionText);
        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(template);
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public Template getTemplate(int position) {
        if (position < 0 || position >= mFilteredTemplates.size()) {
            throw new IllegalArgumentException();
        }
        return mFilteredTemplates.get(position);
    }

    @Override
    public int getItemCount() {
        if (null != mFilteredTemplates) {
            return mFilteredTemplates.size();
        }
        return 0;
    }

    public void filter(String query) {
        query = query.toLowerCase();

        mFilteredTemplates.clear();
        if (Strs.isNullOrEmpty(query)) {
            mFilteredTemplates.addAll(mTemplates);
        } else {
            for (Template template : mTemplates) {
                String title = template.getTitle();
                String description = template.getDescription();

                if ((!Strs.isNullOrEmpty(title) && title.toLowerCase().contains(query))
                        || (!Strs.isNullOrEmpty(description) && description.toLowerCase().contains(query))) {
                    mFilteredTemplates.add(template);
                }
            }
        }
        notifyDataSetChanged();
    }

    public ArrayList<Template> getFilteredTemplates() {
        return mFilteredTemplates;
    }

    public static interface OnItemClickListener {
        void onItemClick(Template template);
    }

    public static final class ViewHolder extends RecyclerView.ViewHolder {
        private final View mParent;
        private final TextView mTitle;
        private final TextView mDescription;

        public static ViewHolder newInstance(View parent) {
            TextView title = (TextView) parent.findViewById(R.id.template_title);
            TextView description = (TextView) parent.findViewById(R.id.template_description);
            return new ViewHolder(parent, title, description);
        }

        private ViewHolder(View parent, TextView title, TextView description) {
            super(parent);
            this.mParent = parent;
            this.mTitle = title;
            this.mDescription = description;
        }

        public void setTitle(CharSequence text) {
            mTitle.setText(text);
        }

        public void setDescription(CharSequence text) {
            mDescription.setText(text);
        }

        public void setOnClickListener(View.OnClickListener listener) {
            mParent.setOnClickListener(listener);
        }
    }
}
