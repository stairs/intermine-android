package org.intermine.app.view;

/*
 * Copyright (C) 2015 InterMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.intermine.app.R;

public class ProgressView extends RelativeLayout {
    private ImageView mProgress;
    private Animation mAnimation;

    public ProgressView(Context context) {
        super(context, null);
    }

    public ProgressView(Context context, AttributeSet attrs, boolean a) {
        super(context, attrs);

        String service = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(service);

        inflater.inflate(R.layout.progress_view, this, true);

        if (!isInEditMode()) {
            mAnimation = AnimationUtils.loadAnimation(context, R.anim.rotate_infinite);

            mProgress = (ImageView) findViewById(R.id.progress_view_progress);
            mProgress.startAnimation(mAnimation);
        }
    }

    public ProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);

        String service = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(service);

        inflater.inflate(R.layout.progress_view, this, true);

        if (!isInEditMode()) {
            mAnimation = AnimationUtils.loadAnimation(context, R.anim.rotate_infinite);

            mProgress = (ImageView) findViewById(R.id.progress_view_progress);
            mProgress.startAnimation(mAnimation);
        }
    }
}
