package org.intermine.storage;

import java.util.Set;

/**
 * @author Daria Komkova <Daria.Komkova @ hotmail.com>
 */
public interface Storage {
    public final static String USER_TOKEN_KEY = "user_token_key_";
    public static final String MINE_NAMES_KEY = "mine_names_key";

    String getUserToken(String mineName);

    void setUserToken(String mineName, String token);

    Set<String> getMineNames();

    void setMineNames(Set<String> mineNames);
}
