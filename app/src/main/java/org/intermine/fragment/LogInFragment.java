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
import org.intermine.net.request.post.GetUserTokenRequest;
import org.intermine.storage.Storage;
import org.intermine.util.Views;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class LogInFragment extends BaseFragment {
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
            Log.e("ddd", spiceException.toString());
        }

        @Override
        public void onRequestSuccess(String token) {
            setLoading(false);

            InterMineApplication app = (InterMineApplication) getActivity().getApplication();
            Storage storage = app.getStorage();
            storage.setUserToken("", token);
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

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLoading(true);
                requestUserToken();
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ((MainActivity) activity).onSectionAttached(getString(R.string.log_in));
    }

    // --------------------------------------------------------------------------------------------
    // Helper Methods
    // --------------------------------------------------------------------------------------------

    protected void requestUserToken() {
        String username = mLogin.getText().toString();
        String password = mPassword.getText().toString();

        GetUserTokenRequest tokenRequest = new GetUserTokenRequest(getActivity(), R.string.flymine_url,
                username, password);
        executeRequest(tokenRequest, new GetPermTokenRequestListener());
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
