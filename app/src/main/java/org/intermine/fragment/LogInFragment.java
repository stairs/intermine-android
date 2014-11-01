package org.intermine.fragment;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.auth.api.GoogleAuthApiClientImpl;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.intermine.R;
import org.intermine.activity.MainActivity;
import org.intermine.net.request.auth.GoogleAuthenticationRequest;
import org.intermine.util.Strs;
import org.intermine.util.Views;

import java.io.IOException;
import java.net.Authenticator;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class LogInFragment extends BaseFragment {
    private String[] mAvailableAccounts;

    private ListView mAccountsList;
    private ProgressBar mProgressBar;
    private ArrayAdapter<String> mAccountsAdapter;

    private SharedPreferences pref;

    public LogInFragment() {
    }

    public static LogInFragment newInstance() {
        return new LogInFragment();
    }

    // --------------------------------------------------------------------------------------------
    // Inner classes
    // --------------------------------------------------------------------------------------------

    class GoogleAuthenticationListener implements RequestListener<String> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            setLoading(false);

            Throwable ex = spiceException.getCause();

            if (ex instanceof IOException) {
                Log.e("IOException", ex.getMessage());
            } else if (ex instanceof UserRecoverableAuthException) {
                UserRecoverableAuthException authEx = (UserRecoverableAuthException) ex;
                startActivityForResult(authEx.getIntent(), 1001);
                Log.e("AuthException", authEx.toString());
            } else if (ex instanceof GoogleAuthException) {
                Log.e("GoogleAuthException", ex.toString());
            }
        }

        @Override
        public void onRequestSuccess(String token) {
            setLoading(false);

            if (!Strs.isNullOrEmpty(token)) {
                SharedPreferences.Editor edit = pref.edit();

                edit.putString("Access Token", token);
                edit.commit();

                Log.i("Token", "Access Token retrieved:" + token);
                Toast.makeText(getActivity(), "Access Token is " + token, Toast.LENGTH_SHORT).show();
            }
        }
    }

    // --------------------------------------------------------------------------------------------
    // Fragment Lifecycle
    // --------------------------------------------------------------------------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAccountsList = (ListView) view.findViewById(R.id.accounts_list);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        mAvailableAccounts = getAccountNames();

        if (mAvailableAccounts.length != 0) {
            mAccountsAdapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1, mAvailableAccounts);

            pref = getActivity().getSharedPreferences("AppPref", Context.MODE_PRIVATE);
            mAccountsList.setAdapter(mAccountsAdapter);
            mAccountsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    SharedPreferences.Editor edit = pref.edit();

                    String email = mAvailableAccounts[position];
                    edit.putString("Email", email);
                    edit.commit();

                    setLoading(true);
                    executeRequest(new GoogleAuthenticationRequest(getActivity(), email),
                            new GoogleAuthenticationListener());
                }
            });
        } else {
            Toast.makeText(getActivity(), "No accounts found, Add an Account and Continue.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ((MainActivity) activity).onSectionAttached(getString(R.string.log_in));
    }

    // --------------------------------------------------------------------------------------------
    // Helper Methods
    // --------------------------------------------------------------------------------------------

    private String[] getAccountNames() {
        AccountManager accountManager = AccountManager.get(getActivity());
        Account[] accounts = accountManager.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);

        String[] names = new String[accounts.length];
        for (int i = 0; i < names.length; i++) {
            names[i] = accounts[i].name;
        }
        return names;
    }

    private void setLoading(boolean loading) {
        if (loading) {
            Views.setVisible(mProgressBar);
            Views.setInvisible(mAccountsList);
        } else {
            Views.setVisible(mAccountsList);
            Views.setInvisible(mProgressBar);
        }
    }
}
