package org.intermine.app.core.templates.constraint;

/*
 * Copyright (C) 2015 InterMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import com.google.gson.annotations.SerializedName;

public enum SwitchOffAbility {
    @SerializedName("ON")
    ON,
    @SerializedName("OFF")
    OFF,
    @SerializedName("LOCKED")
    LOCKED
}
