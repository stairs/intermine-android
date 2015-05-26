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
import java.util.Set;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class ClassDescriptor {
    @SerializedName("attributes")
    private Map<String, Attribute> mAttributes;

    @SerializedName("references")
    private Set<Object> mReferences;

    public Map<String, Attribute> getAttributes() {
        return mAttributes;
    }

    public void setAttributes(Map<String, Attribute> attributes) {
        mAttributes = attributes;
    }

    public Set<Object> getReferences() {
        return mReferences;
    }

    public void setReferences(Set<Object> references) {
        mReferences = references;
    }
}
