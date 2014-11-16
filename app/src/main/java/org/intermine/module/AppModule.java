package org.intermine.module;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.intermine.InterMineApplication;
import org.intermine.activity.BaseActivity;
import org.intermine.activity.GeneViewActivity;
import org.intermine.activity.GenesListActivity;
import org.intermine.activity.MainActivity;
import org.intermine.activity.SettingsActivity;
import org.intermine.activity.StartActivity;
import org.intermine.activity.TemplateActivity;
import org.intermine.activity.TemplateResultsActivity;
import org.intermine.fragment.BaseFragment;
import org.intermine.fragment.GenesListFragment;
import org.intermine.fragment.LogInFragment;
import org.intermine.fragment.PreferencesFragment;
import org.intermine.fragment.SearchFragment;
import org.intermine.net.request.BaseRequest;
import org.intermine.net.request.PostAuthRequest;
import org.intermine.net.request.get.GeneSearchRequest;
import org.intermine.net.request.get.GeneSearchResultsCountRequest;
import org.intermine.net.request.get.GetListsRequest;
import org.intermine.net.request.get.GetModelRequest;
import org.intermine.net.request.get.GetQueryResultsRequest;
import org.intermine.net.request.get.GetTemplateResultsRequest;
import org.intermine.net.request.get.GetTemplatesRequest;
import org.intermine.net.request.post.AppendGenesToListRequest;
import org.intermine.net.request.post.FetchListResultsRequest;
import org.intermine.net.request.post.GetUserTokenRequest;
import org.intermine.net.request.post.PostBaseAuthorizationRequest;
import org.intermine.storage.MemoryStorage;

import java.util.prefs.Preferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        includes = {
                StorageModule.class
        },
        injects = {
                MemoryStorage.class,
                InterMineApplication.class,
                BaseActivity.class,
                StartActivity.class,
                GenesListActivity.class,
                GeneViewActivity.class,
                SettingsActivity.class,
                TemplateActivity.class,
                TemplateResultsActivity.class,
                MainActivity.class,
                GenesListActivity.class,
                BaseFragment.class,
                SearchFragment.class,
                GenesListFragment.class,
                LogInFragment.class,
                PreferencesFragment.class,
                BaseRequest.class,
                GetModelRequest.class,
                GetListsRequest.class,
                GeneSearchRequest.class,
                GeneSearchResultsCountRequest.class,
                GetQueryResultsRequest.class,
                GetTemplateResultsRequest.class,
                GetTemplatesRequest.class,
                AppendGenesToListRequest.class,
                GetUserTokenRequest.class,
                PostBaseAuthorizationRequest.class,
                PostAuthRequest.class,
                FetchListResultsRequest.class
        },
        library = true
)
public final class AppModule {

    /**
     * Application context.
     */
    private final Context context;

    public AppModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return context;
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
