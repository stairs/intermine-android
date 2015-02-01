package org.intermine.dialog;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.intermine.R;
import org.intermine.util.Strs;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class AddMineDialogFragment extends DialogFragment {
    public static final String ADD_MINE_DIALOG_TAG = "add_mine_dialog_tag";

    @InjectView(R.id.mine_name)
    EditText mMineName;

    @InjectView(R.id.mine_url)
    EditText mMineUrl;

    protected AddMineDialogListener listener;

    public static AddMineDialogFragment newInstance() {
        AddMineDialogFragment dialog = new AddMineDialogFragment();
        return dialog;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (AddMineDialogListener) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_mine_dialog_fragment, container, false);
        getDialog().setTitle(getString(R.string.add_mine_preference));
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);
    }

    @OnClick(R.id.cancel)
    public void onCancel() {
        dismiss();
    }

    @OnClick(R.id.add)
    public void onAddMinePressed() {
        if (validateFields()) {
            listener.onMineAdded(mMineName.getText().toString(), mMineUrl.getText().toString());
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

    public interface AddMineDialogListener {
        void onMineAdded(String mineName, String mineUrl);
    }
}
