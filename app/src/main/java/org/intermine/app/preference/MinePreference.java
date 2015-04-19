package org.intermine.app.preference;

import android.content.Context;
import android.preference.MultiSelectListPreference;
import android.util.AttributeSet;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class MinePreference extends MultiSelectListPreference {

    public MinePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MinePreference(Context context) {
        super(context);
    }
}
