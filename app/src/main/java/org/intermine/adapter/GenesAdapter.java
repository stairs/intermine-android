package org.intermine.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.intermine.R;
import org.intermine.core.Gene;
import org.intermine.util.Strs;
import org.intermine.util.Views;

import java.util.List;

/**
 * @author Darya Kamkova <Darya_Kamkova @ epam.com>
 */

public class GenesAdapter extends ArrayAdapter<Gene> {
    static class ViewHolder {
        TextView mPrimaryIdTitle;
        TextView mPrimaryIdValue;
        TextView mNameTitle;
        TextView mNameValue;
        TextView mOrganismNameTitle;
        TextView mOrganismNameValue;
        TextView mSymbolTitle;
        TextView mSymbolValue;
        TextView mMineName;
    }

    public GenesAdapter(Context context, List<Gene> objects) {
        super(context, 0, objects);
    }

    private final LayoutInflater mLayoutInflater = LayoutInflater
            .from(getContext());

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder holder;

        if (null == convertView) {
            v = mLayoutInflater.inflate(R.layout.gene_list_item, parent, false);

            holder = new ViewHolder();
            holder.mPrimaryIdTitle = (TextView) v.findViewById(R.id.primary_db_id_title);
            holder.mPrimaryIdValue = (TextView) v.findViewById(R.id.primary_db_id_value);
            holder.mNameTitle = (TextView) v.findViewById(R.id.name_title);
            holder.mNameValue = (TextView) v.findViewById(R.id.name_value);
            holder.mOrganismNameTitle = (TextView) v.findViewById(R.id.organism_name_title);
            holder.mOrganismNameValue = (TextView) v.findViewById(R.id.organism_name_value);
            holder.mSymbolTitle = (TextView) v.findViewById(R.id.symbol_title);
            holder.mSymbolValue = (TextView) v.findViewById(R.id.symbol_value);
            holder.mMineName = (TextView) v.findViewById(R.id.gene_mine);

            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        Gene gene = getItem(position);

        String primaryDbId = gene.getPrimaryDBId();

        if (Strs.isNullOrEmpty(primaryDbId)) {
            Views.setGone(holder.mPrimaryIdTitle, holder.mPrimaryIdValue);
        } else {
            Views.setVisible(holder.mPrimaryIdTitle, holder.mPrimaryIdValue);
            holder.mPrimaryIdValue.setText(primaryDbId);
        }

        String name = gene.getName();

        if (Strs.isNullOrEmpty(name)) {
            Views.setGone(holder.mNameTitle, holder.mNameValue);
        } else {
            Views.setVisible(holder.mNameTitle, holder.mNameValue);
            holder.mNameValue.setText(name);
        }

        String organismName = gene.getOrganismName();

        if (Strs.isNullOrEmpty(organismName)) {
            Views.setGone(holder.mOrganismNameTitle, holder.mOrganismNameValue);
        } else {
            Views.setVisible(holder.mOrganismNameTitle, holder.mOrganismNameValue);
            holder.mOrganismNameValue.setText(organismName);
        }

        String symbol = gene.getSymbol();

        if (Strs.isNullOrEmpty(symbol)) {
            Views.setGone(holder.mSymbolTitle, holder.mSymbolValue);
        } else {
            Views.setVisible(holder.mSymbolTitle, holder.mSymbolValue);
            holder.mSymbolValue.setText(symbol);
        }
        holder.mMineName.setText(gene.getMine());
        return v;
    }
}
