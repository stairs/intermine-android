package org.intermine.app.listener;

/*
 * Copyright (C) 2015 InterMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.SpiceRequest;
import com.octo.android.robospice.request.listener.RequestListener;

import org.intermine.app.R;
import org.intermine.app.activity.BaseActivity;
import org.intermine.app.core.Gene;
import org.intermine.app.net.HttpNetworkException;
import org.intermine.app.net.ResponseHelper;
import org.intermine.app.net.request.get.GetListsRequest;
import org.intermine.app.net.request.post.AppendGenesToListRequest;
import org.intermine.app.net.request.post.CreateGenesList;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.intermine.app.net.request.get.GetListsRequest.Lists;

public class GetListsListener implements RequestListener<Lists> {
    private BaseActivity mBaseActivity;
    private String mMine;
    private List<Gene> mGenes;

    public GetListsListener(BaseActivity baseActivity, String mine, List<Gene> genes) {
        mBaseActivity = baseActivity;
        mMine = mine;
        mGenes = genes;
    }

    @Override
    public void onRequestFailure(SpiceException spiceException) {
        Throwable ex = spiceException.getCause();

        if (ex instanceof HttpNetworkException) {
            HttpNetworkException httpStatusCodeException = (HttpNetworkException) ex;

            if (HttpStatus.NOT_FOUND.equals(httpStatusCodeException.getStatusCode())) {
                CreateGenesList request = new CreateGenesList(mBaseActivity, mMine,
                        mBaseActivity.getString(R.string.gene_favorites_list_name), mGenes);
                mBaseActivity.execute(request, new RequestListener<Void>() {
                    @Override
                    public void onRequestFailure(SpiceException ex) {
                        ResponseHelper.handleSpiceException(ex, mBaseActivity, mMine);
                    }

                    @Override
                    public void onRequestSuccess(Void aVoid) {

                    }
                });
            }
        }
    }

    @Override
    public void onRequestSuccess(GetListsRequest.Lists result) {
        SpiceRequest req = new AppendGenesToListRequest(mBaseActivity, mMine,
                mBaseActivity.getString(R.string.gene_favorites_list_name), mGenes);
        mBaseActivity.execute(req, new RequestListener<Void>() {
            @Override
            public void onRequestFailure(SpiceException ex) {
                ResponseHelper.handleSpiceException(ex, mBaseActivity, mMine);
            }

            @Override
            public void onRequestSuccess(Void aVoid) {

            }
        });
    }
}