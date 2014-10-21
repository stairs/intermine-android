package org.intermine.net.request.auth;

import android.content.Context;

import com.google.android.gms.auth.GoogleAuthUtil;

import org.intermine.net.request.BaseRequest;

public class GoogleAuthenticationRequest extends BaseRequest<String> {
    private static final String SCOPE = "oauth2:server:client_id:677421287445-f8csi4h7t81it7memjb5i1cgjfa3nvqc.apps.googleusercontent.com";
    private String mEmail;

    public GoogleAuthenticationRequest(Context ctx, String email) {
        super(String.class, ctx, null, null);

        mEmail = email;
    }

    @Override
    public String loadDataFromNetwork() throws Exception {
        return GoogleAuthUtil.getToken(getContext(), mEmail, "oauth2:https://www.googleapis.com/auth/tasks");
    }
}
