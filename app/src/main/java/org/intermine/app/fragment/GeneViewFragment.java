package org.intermine.app.fragment;

/*
 * Copyright (C) 2015 InterMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.intermine.app.R;
import org.intermine.app.core.Gene;
import org.intermine.app.util.Strs;
import org.intermine.app.util.Views;

import butterknife.InjectView;

public class GeneViewFragment extends BaseFragment {
    public static final String GENE_EXTRA = "gene_extra";

    @InjectView(R.id.standard_name_value)
    TextView mStandardNameValue;

    @InjectView(R.id.mine)
    TextView mMine;

    @InjectView(R.id.systematic_name_title)
    TextView mSystematicNameTitle;

    @InjectView(R.id.systematic_name_value)
    TextView mSystematicNameValue;

    @InjectView(R.id.secondary_id_title)
    TextView mSecondaryIdTitle;

    @InjectView(R.id.secondary_id_value)
    TextView mSecondaryIdValue;

    @InjectView(R.id.organism_name_title)
    TextView mOrganismTitle;

    @InjectView(R.id.organism_name_value)
    TextView mOrganismValue;

    @InjectView(R.id.organism_short_title)
    TextView mOrganismShortTitle;

    @InjectView(R.id.organism_short_value)
    TextView mOrganismShortValue;

    @InjectView(R.id.name_description_title)
    TextView mNameDescriptionTitle;

    @InjectView(R.id.name_description_value)
    TextView mNameDescriptionValue;

    @InjectView(R.id.chromosomal_location_title)
    TextView mChromosomalLocationTitle;

    @InjectView(R.id.chromosomal_location_value)
    TextView mChromosomalLocationValue;

    @InjectView(R.id.ontology_term_title)
    TextView mOntologyTermTitle;

    @InjectView(R.id.ontology_term_value)
    TextView mOntologyTermValue;

    private GeneActionCallbacks mCallbacks;
    private Gene mGene;

    // --------------------------------------------------------------------------------------------
    // Static Methods
    // --------------------------------------------------------------------------------------------

    public static GeneViewFragment newInstance(Gene gene) {
        GeneViewFragment fragment = new GeneViewFragment();
        fragment.setGene(gene);
        return fragment;
    }

    // --------------------------------------------------------------------------------------------
    // Inner Classes
    // --------------------------------------------------------------------------------------------

    public interface GeneActionCallbacks {
        void onGeneAddedToFavorites(Gene gene);

        void onGeneSelectedToBeShared(Gene gene);

        void onShowGeneReport(Gene gene);
    }

    // --------------------------------------------------------------------------------------------
    // Lifecycle
    // --------------------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.gene_view_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (null != mGene) {
            if (Strs.isNullOrEmpty(mGene.getSymbol())) {
                Views.setGone(mStandardNameValue);
            } else {
                Views.setVisible(mStandardNameValue);
                mStandardNameValue.setText(Strs.capitalize(mGene.getSymbol()));
            }
            mMine.setText(mGene.getMine());

            showRowIfInfoAvailable(mGene.getPrimaryDBId(),
                    mSystematicNameTitle, mSystematicNameValue);
            showRowIfInfoAvailable(mGene.getSecondaryIdentifier(),
                    mSecondaryIdTitle, mSecondaryIdValue);
            showRowIfInfoAvailable(mGene.getOrganismName(), mOrganismTitle, mOrganismValue);
            showRowIfInfoAvailable(mGene.getOrganismShortName(),
                    mOrganismShortTitle, mOrganismShortValue);
            showRowIfInfoAvailable(mGene.getName(), mNameDescriptionTitle, mNameDescriptionValue);
            showRowIfInfoAvailable(mGene.getOntologyTerm(),
                    mOntologyTermTitle, mOntologyTermValue);

            if (Strs.isNullOrEmpty(mGene.getLocationStart())
                    || Strs.isNullOrEmpty(mGene.getLocationEnd())) {
                Views.setGone(mChromosomalLocationTitle, mChromosomalLocationValue);
            } else {
                Views.setVisible(mChromosomalLocationTitle, mChromosomalLocationValue);
                mChromosomalLocationValue.setText(mGene.getLocationStart() + " to "
                        + mGene.getLocationEnd());
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (GeneActionCallbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    // --------------------------------------------------------------------------------------------
    // Callbacks
    // --------------------------------------------------------------------------------------------

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.gene_view_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.favourites:
                if (null != mCallbacks) {
                    mCallbacks.onGeneAddedToFavorites(mGene);
                }
                return true;
            case R.id.share:
                if (null != mCallbacks) {
                    mCallbacks.onGeneSelectedToBeShared(mGene);
                }
                return true;
            case R.id.open_in_browser:
                if (null != mCallbacks) {
                    mCallbacks.onShowGeneReport(mGene);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // --------------------------------------------------------------------------------------------
    // Helper Methods
    // --------------------------------------------------------------------------------------------

    protected void showRowIfInfoAvailable(String value, TextView titleView, TextView valueView) {
        if (Strs.isNullOrEmpty(value)) {
            Views.setGone(titleView, valueView);
        } else {
            Views.setVisible(titleView, valueView);
            valueView.setText(value);
        }
    }

    public void setGene(Gene gene) {
        mGene = gene;
    }
}
