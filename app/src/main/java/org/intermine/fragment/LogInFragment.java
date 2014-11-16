package org.intermine.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.intermine.InterMineApplication;
import org.intermine.R;
import org.intermine.activity.MainActivity;
import org.intermine.adapter.SimpleAdapter;
import org.intermine.net.request.post.GetUserTokenRequest;
import org.intermine.storage.Storage;
import org.intermine.util.Mines;
import org.intermine.util.Views;

import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnItemSelected;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class LogInFragment extends BaseFragment {
    private static final String TAG = LogInFragment.class.getSimpleName();

    @InjectView(R.id.mine_spinner)
    Spinner mMinesSpinner;

    @InjectView(R.id.login)
    TextView mLogin;

    @InjectView(R.id.password)
    TextView mPassword;

    @InjectView(R.id.login_button)
    Button mSubmit;

    @InjectView(R.id.progress_bar)
    ProgressBar mProgressBar;

    private String mMineName;

    public LogInFragment() {
    }

    public static LogInFragment newInstance() {
        return new LogInFragment();
    }

    // --------------------------------------------------------------------------------------------
    // Inner classes
    // --------------------------------------------------------------------------------------------

    class GetPermTokenRequestListener implements RequestListener<String> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            setLoading(false);
            Log.e(TAG, spiceException.toString());
        }

        @Override
        public void onRequestSuccess(String token) {
            setLoading(false);

            getStorage().setUserToken(mMineName, token);
        }
    }

    // --------------------------------------------------------------------------------------------
    // Fragment Lifecycle
    // --------------------------------------------------------------------------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SimpleAdapter adapter = new SimpleAdapter(getActivity());
        mMinesSpinner.setAdapter(adapter);

        Set<String> mineNames = getStorage().getMineNames();
        adapter.updateData(mineNames);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ((MainActivity) activity).onSectionAttached(getString(R.string.log_in));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    // --------------------------------------------------------------------------------------------
    // Helper Methods
    // --------------------------------------------------------------------------------------------

    @OnClick(R.id.login_button)
    protected void requestUserToken() {
        setLoading(true);

        String username = mLogin.getText().toString();
        String password = mPassword.getText().toString();

        GetUserTokenRequest tokenRequest = new GetUserTokenRequest(getActivity(),
                Mines.getMineBaseUrl(getActivity(), mMineName), username, password);
        executeRequest(tokenRequest, new GetPermTokenRequestListener());
    }

    @OnItemSelected(R.id.mine_spinner)
    protected void setMineBaseUrl() {
        mMineName = mMinesSpinner.getSelectedItem().toString();
    }

    private void setLoading(boolean loading) {
        if (loading) {
            Views.setVisible(mProgressBar);
            Views.setInvisible(mSubmit);
        } else {
            Views.setVisible(mSubmit);
            Views.setInvisible(mProgressBar);
        }
    }
}
