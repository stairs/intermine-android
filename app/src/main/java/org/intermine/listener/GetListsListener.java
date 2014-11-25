package org.intermine.listener;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.SpiceRequest;
import com.octo.android.robospice.request.listener.RequestListener;

import org.intermine.R;
import org.intermine.activity.BaseActivity;
import org.intermine.core.Gene;
import org.intermine.net.HttpNetworkException;
import org.intermine.net.ResponseHelper;
import org.intermine.net.request.get.GetListsRequest;
import org.intermine.net.request.post.AppendGenesToListRequest;
import org.intermine.net.request.post.CreateGenesList;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.intermine.net.request.get.GetListsRequest.Lists;

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