package org.intermine.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.intermine.R;
import org.intermine.core.Gene;
import org.intermine.util.Strs;
import org.intermine.util.Views;

public class GeneViewFragment extends Fragment {
    public static final String GENE_EXTRA = "gene_extra";

    private TextView mStandardNameTitle;
    private TextView mStandardNameValue;
    private TextView mSystematicNameTitle;
    private TextView mSystematicNameValue;
    private TextView mSecondaryIdTitle;
    private TextView mSecondaryIdValue;
    private TextView mOrganismTitle;
    private TextView mOrganismValue;
    private TextView mOrganismShortTitle;
    private TextView mOrganismShortValue;
    private TextView mNameDescriptionTitle;
    private TextView mNameDescriptionValue;
    private TextView mChromosomalLocationTitle;
    private TextView mChromosomalLocationValue;
    private TextView mOntologyTermTitle;
    private TextView mOntologyTermValue;

    private GeneActionCallbacks mCallbacks;
    private Gene mGene;

    public static interface GeneActionCallbacks {
        void onGeneAddedToFavorites(Gene gene);

        void onGeneSelectedToBeEmailed(Gene gene);

    }

    public static GeneViewFragment newInstance(Gene gene) {
        GeneViewFragment fragment = new GeneViewFragment();
        fragment.setGene(gene);
        return fragment;
    }

    public GeneViewFragment() {
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
            mStandardNameTitle = (TextView) view.findViewById(R.id.standard_name_title);
            mStandardNameValue = (TextView) view.findViewById(R.id.standard_name_value);
            mSystematicNameTitle = (TextView) view.findViewById(R.id.systematic_name_title);
            mSystematicNameValue = (TextView) view.findViewById(R.id.systematic_name_value);
            mSecondaryIdTitle = (TextView) view.findViewById(R.id.secondary_id_title);
            mSecondaryIdValue = (TextView) view.findViewById(R.id.secondary_id_value);
            mOrganismTitle = (TextView) view.findViewById(R.id.organism_name_title);
            mOrganismValue = (TextView) view.findViewById(R.id.organism_name_value);
            mOrganismShortTitle = (TextView) view.findViewById(R.id.organism_short_title);
            mOrganismShortValue = (TextView) view.findViewById(R.id.organism_short_value);
            mNameDescriptionTitle = (TextView) view.findViewById(R.id.name_description_title);
            mNameDescriptionValue = (TextView) view.findViewById(R.id.name_description_value);
            mChromosomalLocationTitle = (TextView) view.findViewById(R.id.chromosomal_location_title);
            mChromosomalLocationValue = (TextView) view.findViewById(R.id.chromosomal_location_value);
            mOntologyTermTitle = (TextView) view.findViewById(R.id.ontology_term_title);
            mOntologyTermValue = (TextView) view.findViewById(R.id.ontology_term_value);

            showRowIfInfoAvailable(mGene.getSymbol(), mStandardNameTitle, mStandardNameValue);
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
                //TODO: refactore
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
        inflater.inflate(R.menu.context, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.favourites:
                if (null != mCallbacks) {
                    mCallbacks.onGeneAddedToFavorites(mGene);
                }
                return true;
            case R.id.email:
                if (null != mCallbacks) {
                    mCallbacks.onGeneSelectedToBeEmailed(mGene);
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

    public Gene getGene() {
        return mGene;
    }

    public void setGene(Gene gene) {
        mGene = gene;
    }
}
