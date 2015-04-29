package org.intermine.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;

import com.melnykov.fab.FloatingActionButton;

import org.intermine.app.R;
import org.intermine.app.adapter.MinesAdapter;
import org.intermine.app.core.Gene;
import org.intermine.app.dialog.MineDialogFragment;
import org.intermine.app.storage.Storage;
import org.intermine.app.util.Collections;
import org.intermine.app.util.Emails;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class MinesActivity extends BaseActivity implements MineDialogFragment.MineDialogListener {
    @Inject
    Storage mStorage;

    @InjectView(R.id.mines_list)
    ListView mMinesList;

    @InjectView(R.id.fab)
    FloatingActionButton mActionButton;

    private MinesAdapter mAdapter;

    private AbsListView.MultiChoiceModeListener mMultiListener = new AbsListView.MultiChoiceModeListener() {
        @Override
        public void onItemCheckedStateChanged(ActionMode mode, int pos, long id, boolean checked) {
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater menuInflater = mode.getMenuInflater();
            menuInflater.inflate(R.menu.mine_context, menu);

            getSupportActionBar().hide();
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.delete:
                    Set<String> selectedMineNames = mStorage.getSelectedMineNames();
                    Set<String> mineNames = mStorage.getMineNames();

                    SparseBooleanArray checkedItemIds = mMinesList.getCheckedItemPositions();
                    for (int i = 0; i < checkedItemIds.size(); i++) {
                        String mineName = (String) mAdapter.getItem(checkedItemIds.keyAt(i));

                        if (selectedMineNames.contains(mineName)) {
                            selectedMineNames.remove(mineName);
                        }

                        if (mineNames.contains(mineName)) {
                            mineNames.remove(mineName);
                        }
                        mStorage.setMineUrl(mineName, null);
                    }
                    mStorage.setMineNames(mineNames);
                    mStorage.setSelectedMineNames(selectedMineNames);
                    updateMines();
                    mode.finish();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            getSupportActionBar().show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mines_activity);
        ButterKnife.inject(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.default_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAdapter = new MinesAdapter(this);
        mMinesList.setAdapter(mAdapter);
        mMinesList.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        mMinesList.setMultiChoiceModeListener(mMultiListener);
        updateMines();
        mActionButton.attachToListView(mMinesList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();

        Map<String, Boolean> minesToState = mAdapter.getMinesToState();
        Set<String> selectedMineNames = new HashSet<>();
        for (String mineName : minesToState.keySet()) {
            if (minesToState.get(mineName)) {
                selectedMineNames.add(mineName);
            }
        }
        mStorage.setSelectedMineNames(selectedMineNames);
    }

    @OnItemClick(R.id.mines_list)
    public void showMineEditDialog(int position) {
        String mineName = (String) mAdapter.getItem(position);
        MineDialogFragment.newInstance(mineName, mStorage.getMineUrl(mineName)).show(getFragmentManager(),
                MineDialogFragment.MINE_DIALOG_TAG);
    }

    @OnClick(R.id.fab)
    public void showAddMineDialog() {
        MineDialogFragment.newInstance().show(getFragmentManager(),
                MineDialogFragment.MINE_DIALOG_TAG);
    }

    @Override
    public void onMineAdded(String mineName, String mineUrl) {
        Set<String> selectedMineNames = mStorage.getSelectedMineNames();
        Set<String> mineNames = mStorage.getMineNames();
        mineNames.add(mineName);
        selectedMineNames.add(mineName);
        mStorage.setMineNames(mineNames);
        mStorage.setSelectedMineNames(selectedMineNames);
        mStorage.setMineUrl(mineName, mineUrl);
        updateMines();
    }

    @Override
    public void onMineEdited(String oldMineName, String mineName, String mineUrl) {
        Set<String> selectedMineNames = mStorage.getSelectedMineNames();
        Set<String> mineNames = mStorage.getMineNames();

        if (selectedMineNames.contains(oldMineName)) {
            selectedMineNames.remove(oldMineName);
        }

        if (mineNames.contains(oldMineName)) {
            mineNames.remove(oldMineName);
        }

        mStorage.setMineUrl(oldMineName, null);
        mStorage.setMineUrl(mineName, mineUrl);
        mineNames.add(mineName);
        selectedMineNames.add(mineName);
        mStorage.setMineNames(mineNames);
        mStorage.setSelectedMineNames(selectedMineNames);
        updateMines();
    }

    private void updateMines() {
        Set<String> minesNames = mStorage.getMineNames();
        Set<String> selectedMinesNames = mStorage.getSelectedMineNames();
        Map<String, Boolean> minesToState = Collections.newHashMap();
        for (String mine : minesNames) {
            minesToState.put(mine, selectedMinesNames.contains(mine));
        }
        mAdapter.updateMines(minesToState);
    }
}
