package org.intermine.app.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.intermine.app.R;
import org.intermine.app.activity.MainActivity;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class InfoFragment extends Fragment {

    public InfoFragment() {
    }

    public static InfoFragment newInstance() {
        return new InfoFragment();
    }

    // --------------------------------------------------------------------------------------------
    // Fragment Lifecycle
    // --------------------------------------------------------------------------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.info_fragment, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ((MainActivity) activity).onSectionAttached(getString(R.string.info));
    }
}
