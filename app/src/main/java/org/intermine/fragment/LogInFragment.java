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
import android.widget.Toast;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.intermine.R;
import org.intermine.activity.MainActivity;
import org.intermine.adapter.SimpleAdapter;
import org.intermine.net.request.post.GetUserTokenRequest;
import org.intermine.util.Collections;
import org.intermine.util.Mines;
import org.intermine.util.Strs;
import org.intermine.util.Views;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.HashSet;
import java.util.List;
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

    @InjectView(R.id.creds_container)
    ViewGroup mCredsContainer;

    @InjectView(R.id.login)
    TextView mLogin;

    @InjectView(R.id.password)
    TextView mPassword;

    @InjectView(R.id.login_button)
    Button mSubmit;

    @InjectView(R.id.progress_bar)
    ProgressBar mProgressBar;

    @InjectView(R.id.authorized_for_mines_label)
    TextView mAuthorizedForMinesLabel;

    @InjectView(R.id.logged_in_for_all_mines_label)
    TextView mLoggedInForAllMines;

    private SimpleAdapter<String> mAdapter;

    private String mAuthorizedForMines;

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

            Throwable ex = spiceException.getCause();
            Log.e(TAG, spiceException.toString());

            mPassword.setText("");
            mLogin.requestFocus();

            if (ex instanceof HttpStatusCodeException) {
                HttpStatusCodeException httpException = (HttpStatusCodeException) ex;

                if (HttpStatus.UNAUTHORIZED.equals(httpException.getStatusCode())) {
                    Toast.makeText(getActivity(), R.string.incorrect_creds, Toast.LENGTH_LONG).show();
                    return;
                }
            }
            Toast.makeText(getActivity(), R.string.default_error_message, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onRequestSuccess(String token) {
            setLoading(false);

            getStorage().setUserToken(mMineName, token);

            cleanCredsFields();
            initLogInContainer(getStorage().getMineNames());
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

        mAdapter = new SimpleAdapter(getActivity());
        mMinesSpinner.setAdapter(mAdapter);

        mAuthorizedForMines = getString(R.string.authorized_for_mines_label);
    }

    @Override
    public void onStart() {
        super.onStart();

        Set<String> mineNames = getStorage().getMineNames();
        mAdapter.updateData(mineNames);
        initLogInContainer(mineNames);
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

    protected void initLogInContainer(Set<String> mineNames) {
        List<String> minesToLogin = Collections.newArrayList();
        for (String mine : mineNames) {
            String token = mStorage.getUserToken(mine);

            if (Strs.isNullOrEmpty(token)) {
                minesToLogin.add(mine);
            }
        }

        if (minesToLogin.isEmpty()) {
            Views.setGone(mMinesSpinner, mCredsContainer);
            Views.setVisible(mLoggedInForAllMines);
        } else {
            Views.setVisible(mMinesSpinner, mCredsContainer);
            Views.setGone(mLoggedInForAllMines);
            mAdapter.updateData(minesToLogin);
        }

        Set<String> loggedInMines = new HashSet<>(mineNames);
        loggedInMines.removeAll(minesToLogin);

        if (!loggedInMines.isEmpty()) {
            Views.setVisible(mAuthorizedForMinesLabel);
            String mines = Strs.join(loggedInMines, ", ");
            mAuthorizedForMinesLabel.setText(mAuthorizedForMines + " " + mines);
        } else {
            Views.setGone(mAuthorizedForMinesLabel);
        }
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

    protected void cleanCredsFields() {
        mLogin.setText("");
        mPassword.setText("");
        mLogin.requestFocus();
    }
}
