package org.intermine.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.intermine.R;
import org.intermine.activity.MainActivity;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class BrowseFragment extends Fragment implements View.OnClickListener {

    public BrowseFragment() {
    }

    public static BrowseFragment newInstance() {
        return new BrowseFragment();
    }

    // --------------------------------------------------------------------------------------------
    // Fragment Lifecycle
    // --------------------------------------------------------------------------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.browse_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.orf_verified_browse_category).setOnClickListener(this);
        view.findViewById(R.id.orf_uncharacterized_browse_category).setOnClickListener(this);
        view.findViewById(R.id.orf_dubious_browse_category).setOnClickListener(this);
        view.findViewById(R.id.ncrna_browse_category).setOnClickListener(this);
        view.findViewById(R.id.rrna_browse_category).setOnClickListener(this);
        view.findViewById(R.id.snrna_browse_category).setOnClickListener(this);
        view.findViewById(R.id.snorna_browse_category).setOnClickListener(this);
        view.findViewById(R.id.trna_browse_category).setOnClickListener(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ((MainActivity) activity).onSectionAttached(getString(R.string.browse));
    }

    // --------------------------------------------------------------------------------------------
    // Callbacks
    // --------------------------------------------------------------------------------------------

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.orf_verified_browse_category:
                break;
            case R.id.orf_uncharacterized_browse_category:
                break;
            case R.id.orf_dubious_browse_category:
                break;
            case R.id.ncrna_browse_category:
                break;
            case R.id.rrna_browse_category:
                break;
            case R.id.snrna_browse_category:
                break;
            case R.id.snorna_browse_category:
                break;
            case R.id.trna_browse_category:
                break;
        }
    }
}
