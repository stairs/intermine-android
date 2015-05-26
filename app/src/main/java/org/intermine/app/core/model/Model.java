package org.intermine.app.core.model;

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

import java.util.Map;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class Model {
    @SerializedName("name")
    private String mName;

    @SerializedName("classes")
    private Map<String, ClassDescriptor> mClasses;

    private String mMineName;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Map<String, ClassDescriptor> getClasses() {
        return mClasses;
    }

    public void setClasses(Map<String, ClassDescriptor> classes) {
        mClasses = classes;
    }

    public String getMineName() {
        return mMineName;
    }

    public void setMineName(String mineName) {
        mMineName = mineName;
    }
}