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
import org.intermine.app.activity.BaseActivity;
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

    @InjectView(R.id.login_container)
    ViewGroup mLoginContainer;

    @InjectView(R.id.login)
    TextView mLogin;

    @InjectView(R.id.password)
    TextView mPassword;

    @InjectView(R.id.login_button)
    Button mSubmit;

    @InjectView(R.id.progress_bar)
    ProgressBar mProgressBar;

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

            getStorage().setUserToken(getStorage().getWorkingMineName(), token);
            getActivity().finish();
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

    // --------------------------------------------------------------------------------------------
    // Helper Methods
    // --------------------------------------------------------------------------------------------

    @OnClick(R.id.login_button)
    protected void requestUserToken() {
        String username = mLogin.getText().toString();
        String password = mPassword.getText().toString();

        if (Strs.isNullOrEmpty(username) || Strs.isNullOrEmpty(password)) {
            Toast.makeText(getActivity(), R.string.empty_creds, Toast.LENGTH_LONG).show();
        } else {
            setLoading(true);

            GetUserTokenRequest tokenRequest = new GetUserTokenRequest(getActivity(),
                    getStorage().getWorkingMineName(), username, password);
            execute(tokenRequest, new GetPermTokenRequestListener());
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
