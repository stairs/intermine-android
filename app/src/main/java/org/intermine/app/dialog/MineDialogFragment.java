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
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.intermine.app.R;
import org.intermine.app.util.Strs;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class MineDialogFragment extends DialogFragment {
    public static final String MINE_DIALOG_TAG = "edit_mine_dialog_tag";

    public static final String MINE_NAME_KEY = "mine_name_key";
    public static final String MINE_URL_KEY = "mine_url_key";

    @InjectView(R.id.mine_name)
    EditText mMineName;

    @InjectView(R.id.mine_url)
    EditText mMineUrl;

    private String mOldMineName;
    private String mOldMineUrl;

    public interface MineDialogListener {
        void onMineAdded(String mineName, String mineUrl);

        void onMineEdited(String oldMineName, String mineName, String mineUrl);
    }

    protected MineDialogListener listener;

    public static MineDialogFragment newInstance() {
        MineDialogFragment dialog = new MineDialogFragment();
        return dialog;
    }

    public static MineDialogFragment newInstance(String oldMineName, String oldMineUrl) {
        MineDialogFragment dialog = new MineDialogFragment();
        Bundle args = new Bundle();
        args.putString(MINE_NAME_KEY, oldMineName);
        args.putString(MINE_URL_KEY, oldMineUrl);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (MineDialogListener) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.edit_mine_dialog_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);

        Bundle bundle = getArguments();

        if (null != bundle) {
            mOldMineName = bundle.getString(MINE_NAME_KEY);
            mMineName.setText(mOldMineName);

            mOldMineUrl = bundle.getString(MINE_URL_KEY);
            mMineUrl.setText(mOldMineUrl);

            getDialog().setTitle(getString(R.string.edit_mine_preference));
        } else {
            getDialog().setTitle(getString(R.string.add_mine_preference));
        }
    }

    @OnClick(R.id.cancel)
    public void onCancel() {
        dismiss();
    }

    @OnClick(R.id.save)
    public void onSaveMinePressed() {
        if (validateFields()) {
            if (Strs.isNullOrEmpty(mOldMineName)) {
                listener.onMineAdded(mMineName.getText().toString(), mMineUrl.getText().toString());
            } else {
                listener.onMineEdited(mOldMineName, mMineName.getText().toString(), mMineUrl.getText().toString());
            }
            dismiss();
        }
    }

    protected boolean validateFields() {
        boolean allValid = true;

        if (Strs.isNullOrEmpty(mMineName.getText().toString())) {
            mMineName.setError(getString(R.string.empty_value_em));
            allValid = false;
        }
        String mineUrl = mMineUrl.getText().toString();

        if (Strs.isNullOrEmpty(mineUrl) || !Patterns.WEB_URL.matcher(mineUrl).matches()) {
            mMineUrl.setError(getString(R.string.malformed_url_provided_em));
            allValid = false;
        }
        return allValid;
    }
}