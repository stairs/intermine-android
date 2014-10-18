package org.intermine.storage;

/**
 * @author Daria Komkova <Daria.Komkova @ hotmail.com>
 */
public interface Storage {
    public final static String PROJECT_COEFFICIENT_KEY = "first_start_key";

    float getProjectCoefficient(String projectId);
    void setProjectCoefficient(String projectId, float coefficient);
}
