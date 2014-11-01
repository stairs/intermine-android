package org.intermine.core;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class ListItems extends ArrayList<List<String>> {
    private List<String> mFields;

    public List<String> getFields() {
        return mFields;
    }

    public void setFields(List<String> fields) {
        mFields = fields;
    }
}
