package org.intermine.storage;

import org.intermine.core.model.Model;

import java.util.Map;
import java.util.Set;

/**
 * @author Daria Komkova <Daria.Komkova @ hotmail.com>
 */
public interface Storage {
    public final static String USER_TOKEN_KEY = "user_token_key_";
    public static final String MINE_NAMES_KEY = "mine_names_key";
    public static final String ADD_MINE_KEY = "add_mine_key";
    public static final String MINE_URL_KEY = "mine_url_key";

    String getUserToken(String mineName);

    void setUserToken(String mineName, String token);

    Map<String, String> getMineToUserTokenMap();

    Set<String> getMineNames();

    void setMineNames(Set<String> mineNames);

    Model getMineModel(String mineName);

    Map<String, Model> getMineToModelMap();

    void addMineModel(String mineName, Model model);

    void setMineUrl(String mine, String url);

    String getMineUrl(String mine);

    Map<String, String> getMineNameToUrlMap();
}
