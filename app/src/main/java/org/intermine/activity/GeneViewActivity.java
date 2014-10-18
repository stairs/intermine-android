package org.intermine.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.widget.Toast;

import org.intermine.R;
import org.intermine.core.Gene;
import org.intermine.dao.GeneDAO;
import org.intermine.dao.SQLiteGeneDAO;
import org.intermine.fragment.GeneViewFragment;
import org.intermine.util.Emails;

import java.util.List;


public class GeneViewActivity extends MainActivity implements GeneViewFragment.GeneActionCallbacks {
    private GeneDAO mGeneDAO;
    private Gene mGene;

    // --------------------------------------------------------------------------------------------
    // Static Methods
    // --------------------------------------------------------------------------------------------

    /**
     * Conventional method for starting activity
     *
     * @param ctx  Context
     * @param gene Gene to show basic info about
     */
    public static void start(Context ctx, Gene gene) {
        Intent intent = new Intent(ctx, GeneViewActivity.class);
        intent.putExtra(GeneViewFragment.GENE_EXTRA, gene);
        ctx.startActivity(intent);
    }

    // --------------------------------------------------------------------------------------------
    // Activity Lifecycle
    // --------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        Bundle bundle = intent.getExtras();

        if (null != bundle) {
            mGene = bundle.getParcelable(GeneViewFragment.GENE_EXTRA);
        }

        populateContentFragment(GeneViewFragment.newInstance(mGene));

        getNavigationDrawer().setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout), false);
        mGeneDAO = new SQLiteGeneDAO(this);
    }

    // --------------------------------------------------------------------------------------------
    //  Event Listeners
    // --------------------------------------------------------------------------------------------

    @Override
    public void onGeneAddedToFavorites(Gene gene) {
        mGeneDAO.save(gene);
        Toast.makeText(this, R.string.gene_added_to_favorites, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGeneSelectedToBeEmailed(Gene gene) {
        sendEmail();
    }

    // --------------------------------------------------------------------------------------------
    // Helper Methods
    // --------------------------------------------------------------------------------------------

    protected void sendEmail() {
        Intent emailIntent = Emails.generateIntentToSendEmail(mGene);

        PackageManager manager = getPackageManager();
        List<ResolveInfo> infos = manager.queryIntentActivities(emailIntent, 0);

        if (infos.size() > 0) {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        } else {
            Toast.makeText(this, "Email client is not installed.", Toast.LENGTH_SHORT).show();
        }
    }
}
