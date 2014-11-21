package org.intermine.util;

import android.app.FragmentManager;
import android.view.View;
import android.view.animation.AlphaAnimation;

import org.intermine.dialog.AlertDialogFragment;

public class Views {
    private static final String DEFAULT_DIALOG_TAG = "dialog_tag";

    public static void setVisible(View... views) {
        for (View view : views) {
            view.setVisibility(View.VISIBLE);
        }
    }

    public static void setInvisible(View... views) {
        for (View view : views) {
            view.setVisibility(View.INVISIBLE);
        }
    }

    public static void setGone(View... views) {
        for (View view : views) {
            view.setVisibility(View.GONE);
        }
    }

    public static void setEnabled(View... views) {
        for (View view : views) {
            view.setEnabled(true);
        }
    }

    public static void setDisabled(View... views) {
        for (View view : views) {
            view.setEnabled(false);
        }
    }

    public static void setClickable(boolean clickable, View... views) {
        for (View view : views) {
            view.setClickable(clickable);
        }
    }

    public static void setAlpha(float alpha, View... views) {
        for (View view : views) {
            AlphaAnimation anim = new AlphaAnimation(alpha, alpha);
            anim.setDuration(0);
            anim.setFillAfter(true);

            view.startAnimation(anim);
        }
    }

    public static void showDialogFragment(FragmentManager fm, AlertDialogFragment dialog) {
        showDialogFragmentWithTag(fm, dialog, DEFAULT_DIALOG_TAG);
    }

    public static void showDialogFragmentWithTag(FragmentManager fm, AlertDialogFragment dialog, String tag) {
        dismissDialogFragmentWithTag(fm, tag);
        dialog.show(fm, tag);
    }

    public static void dismissDialogFragmentWithTag(FragmentManager fm, String tag) {
        AlertDialogFragment existingDialog = (AlertDialogFragment) fm.findFragmentByTag(tag);
        if (existingDialog != null) {
            existingDialog.dismiss();
        }
    }

    public static void dismissDialog(FragmentManager fm) {
        dismissDialogFragmentWithTag(fm, DEFAULT_DIALOG_TAG);
    }
}
