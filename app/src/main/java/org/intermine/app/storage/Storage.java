package org.intermine.app.storage;

/*
 * Copyright (C) 2015 InterMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import org.intermine.app.core.model.Model;
import org.intermine.app.net.request.get.GetTypeFieldsRequest;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Daria Komkova <Daria.Komkova @ hotmail.com>
 */
public interface Storage {
    String USER_TOKEN_KEY = "user_token_key_";
    String SELECTED_MINE_NAMES_KEY = "selected_mine_names_key";
    String MINE_NAMES_KEY = "mine_names_key";
    String MINE_URL_KEY = "mine_url_key_";
    String MINE_URL_WEB_APP_KEY = "mine_url_web_app_key_";
    String USER_LEARNED_DRAWER = "navigation_drawer_learned";
    String TYPE_FIELDS_KEY = "type_fields_key_";

    String getUserToken(String mineName);

    void setUserToken(String mineName, String token);

    Map<String, String> getMineToUserTokenMap();


    Model getMineModel(String mineName);

    Map<String, Model> getMineToModelMap();

    void addMineModel(String mineName, Model model);


    void setMineUrl(String mine, String url);

    String getMineUrl(String mine);

    void setMineWebAppUrl(String mine, String url);

    String getMineWebAppUrl(String mine);

    Set<String> getSelectedMineNames();

    void setSelectedMineNames(Set<String> selectedMineNames);

    Set<String> getMineNames();

    void setMineNames(Set<String> mineNames);

    Map<String, String> getMineNameToUrlMap();

    boolean hasUserLearnedDrawer();

    void setUserLearnedDrawer(boolean userLearnedDrawer);

    void setTypeFields(String mineName, Map<String, List<String>> typeFields);

    Map<String, List<String>> getTypeFields(String mineName);
}
