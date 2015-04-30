package org.intermine.app.fragment;

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

import org.intermine.app.R;
import org.intermine.app.activity.MainActivity;
import org.intermine.app.adapter.SimpleAdapter;
import org.intermine.app.net.request.post.GetUserTokenRequest;
import org.intermine.app.util.Strs;
import org.intermine.app.util.Views;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.Collection;
import java.util.HashSet;
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

    @InjectView(R.id.login_container)
    ViewGroup mLoginContainer;

    @InjectView(R.id.logout_container)
    ViewGroup mLogOutContainer;

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

            cleanLoginFields();
            Views.setGone(mLoginContainer);
            Views.setVisible(mLogOutContainer);
            updateBottomLine();
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

        Set<String> mineNames = new HashSet<>(getStorage().getSelectedMineNames());
        mAdapter.updateData(mineNames);
        Object selected = mMinesSpinner.getSelectedItem();

        if (null != selected) {
            showAuthContainer(selected.toString());
        }

        updateBottomLine();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ((MainActivity) activity).onSectionAttached(getString(R.string.log_in));
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
                mMineName, username, password);
        execute(tokenRequest, new GetPermTokenRequestListener());
    }

    @OnClick(R.id.logout_button)
    protected void logOut() {
        getStorage().setUserToken(mMineName, null);
        Views.setVisible(mLoginContainer);
        Views.setGone(mLogOutContainer);
        updateBottomLine();
    }

    @OnItemSelected(R.id.mine_spinner)
    protected void onMineSelected() {
        mMineName = mMinesSpinner.getSelectedItem().toString();
        showAuthContainer(mMineName);
    }

    protected void showAuthContainer(String mineName) {
        String token = getStorage().getUserToken(mineName);

        if (Strs.isNullOrEmpty(token)) {
            Views.setVisible(mLoginContainer);
            Views.setGone(mLogOutContainer);
        } else {
            Views.setVisible(mLogOutContainer);
            Views.setGone(mLoginContainer);
        }
    }

    protected void updateBottomLine() {
        if (getStorage().getMineToUserTokenMap().isEmpty()) {
            Views.setGone(mAuthorizedForMinesLabel);
        } else {
            Views.setVisible(mAuthorizedForMinesLabel);

            Collection<String> mines = getStorage().getMineToUserTokenMap().keySet();
            String text = Strs.join(mines, ", ");
            mAuthorizedForMinesLabel.setText(mAuthorizedForMines.concat(" ").concat(text));
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

    protected void cleanLoginFields() {
        mLogin.setText("");
        mPassword.setText("");
        mLogin.requestFocus();
    }
}
