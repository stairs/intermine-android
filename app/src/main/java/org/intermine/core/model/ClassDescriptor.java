package org.intermine.core.model;

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
