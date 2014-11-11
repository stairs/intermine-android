package org.intermine.storage;

/**
 * @author Daria Komkova <Daria.Komkova @ hotmail.com>
 */
public interface Storage {
    public final static String USER_TOKEN_KEY = "user_token_key_";

    String getUserToken(String mineName);

    void setUserToken(String mineName, String token);
}
