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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.intermine.app.R;
import org.intermine.app.core.Gene;
import org.intermine.app.util.Strs;
import org.intermine.app.util.ThreadPreconditions;
import org.intermine.app.util.Views;

import java.util.Collections;
import java.util.List;

/**
 * @author Daria Komkova <Daria.Komkova @ hotmail.com>
 */

public class GenesAdapter extends BaseAdapter {
    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private List<Gene> mGenes = Collections.emptyList();

    public GenesAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public void updateGenes(List<Gene> genes) {
        mGenes = genes;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mGenes.size();
    }

    @Override
    public Object getItem(int position) {
        return mGenes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (null == convertView) {
            v = mLayoutInflater.inflate(R.layout.gene_list_item, parent, false);

            v.setTag(R.id.primary_db_id_title, v.findViewById(R.id.primary_db_id_title));
            v.setTag(R.id.primary_db_id_value, v.findViewById(R.id.primary_db_id_value));
            v.setTag(R.id.name_title, v.findViewById(R.id.name_title));
            v.setTag(R.id.name_value, v.findViewById(R.id.name_value));
            v.setTag(R.id.organism_name_title, v.findViewById(R.id.organism_name_title));
            v.setTag(R.id.organism_name_value, v.findViewById(R.id.organism_name_value));
            v.setTag(R.id.symbol_title, v.findViewById(R.id.symbol_title));
            v.setTag(R.id.symbol_value, v.findViewById(R.id.symbol_value));
            v.setTag(R.id.gene_mine, v.findViewById(R.id.gene_mine));
        }

        TextView primaryIdTitle = (TextView) v.getTag(R.id.primary_db_id_title);
        TextView primaryIdValue = (TextView) v.getTag(R.id.primary_db_id_value);
        TextView nameTitle = (TextView) v.getTag(R.id.name_title);
        TextView nameValue = (TextView) v.getTag(R.id.name_value);
        TextView organismNameTitle = (TextView) v.getTag(R.id.organism_name_title);
        TextView organismNameValue = (TextView) v.getTag(R.id.organism_name_value);
        TextView symbolTitle = (TextView) v.getTag(R.id.symbol_title);
        TextView symbolValue = (TextView) v.getTag(R.id.symbol_value);
        TextView mineName = (TextView) v.getTag(R.id.gene_mine);

        Gene gene = (Gene) getItem(position);

        String primaryDbId = gene.getPrimaryDBId();

        if (Strs.isNullOrEmpty(primaryDbId)) {
            Views.setGone(primaryIdTitle, primaryIdValue);
        } else {
            Views.setVisible(primaryIdTitle, primaryIdValue);
            primaryIdValue.setText(primaryDbId);
        }

        String name = gene.getName();

        if (Strs.isNullOrEmpty(name)) {
            Views.setGone(nameTitle, nameValue);
        } else {
            Views.setVisible(nameTitle, nameValue);
            nameValue.setText(name);
        }

        String organismName = gene.getOrganismName();

        if (Strs.isNullOrEmpty(organismName)) {
            Views.setGone(organismNameTitle, organismNameValue);
        } else {
            Views.setVisible(organismNameTitle, organismNameValue);
            organismNameValue.setText(organismName);
        }

        String symbol = gene.getSymbol();

        if (Strs.isNullOrEmpty(symbol)) {
            Views.setGone(symbolTitle, symbolValue);
        } else {
            Views.setVisible(symbolTitle, symbolValue);
            symbolValue.setText(symbol);
        }
        mineName.setText(gene.getMine());
        return v;
    }
}
