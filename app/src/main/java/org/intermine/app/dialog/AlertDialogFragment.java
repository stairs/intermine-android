package org.intermine.app.dialog;

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
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import org.intermine.app.R;
import org.intermine.app.util.Strs;


public class AlertDialogFragment extends DialogFragment {
    protected static final String DIALOG_ID_KEY = "dialog_id";
    private static final String MESSAGE_KEY = "message";

    private int mDialogId;

    protected OnDialogDismissedListener listener;


    public static AlertDialogFragment newInstance(int id, String message) {
        AlertDialogFragment dialog = new AlertDialogFragment();
        Bundle args = new Bundle();
        args.putInt(DIALOG_ID_KEY, id);
        args.putString(MESSAGE_KEY, message);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (OnDialogDismissedListener) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        String message = null;

        if (null != bundle) {
            message = bundle.getString(MESSAGE_KEY);
            mDialogId = getArguments().getInt(DIALOG_ID_KEY);
        }

        if (Strs.isNullOrEmpty(message)) {
            message = getString(R.string.default_error_message);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message).setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onDialogDismissed(getDialogId());
                dialog.dismiss();
            }
        });
        return builder.create();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        listener.onDialogDismissed(getDialogId());
        super.onCancel(dialog);
    }

    public int getDialogId() {
        return mDialogId;
    }

    public interface OnDialogDismissedListener {
        void onDialogDismissed(int id);
    }
}
