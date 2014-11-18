package org.intermine.util;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

public class Views {
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
}
