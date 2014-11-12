package org.intermine.core.model;

import com.google.gson.annotations.SerializedName;

import java.util.Map;
import java.util.Set;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class Model {
    @SerializedName("name")
    private String mName;

    @SerializedName("classes")
    private Map<String, ClassDescriptor> mClasses;

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
}