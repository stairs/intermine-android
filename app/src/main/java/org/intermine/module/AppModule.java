package org.intermine.module;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.intermine.InterMineApplication;
import org.intermine.activity.GeneViewActivity;
import org.intermine.activity.GenesListActivity;
import org.intermine.activity.MainActivity;
import org.intermine.activity.SettingsActivity;
import org.intermine.activity.StartActivity;
import org.intermine.activity.TemplateActivity;
import org.intermine.activity.TemplateResultsActivity;
import org.intermine.fragment.GeneViewFragment;
import org.intermine.fragment.GenesListFragment;
import org.intermine.fragment.ListsFragment;
import org.intermine.fragment.LogInFragment;
import org.intermine.fragment.NavigationDrawerFragment;
import org.intermine.fragment.PreferencesFragment;
import org.intermine.fragment.SearchFragment;
import org.intermine.fragment.TemplatesFragment;
import org.intermine.net.request.PostAuthRequest;
import org.intermine.net.request.get.GeneSearchRequest;
import org.intermine.net.request.get.GeneSearchResultsCountRequest;
import org.intermine.net.request.get.GetListsRequest;
import org.intermine.net.request.get.GetModelRequest;
import org.intermine.net.request.get.GetTemplateResultsRequest;
import org.intermine.net.request.get.GetTemplatesRequest;
import org.intermine.net.request.post.AppendGenesToListRequest;
import org.intermine.net.request.post.CreateGenesList;
import org.intermine.net.request.post.FetchListResultsRequest;
import org.intermine.net.request.post.GetUserTokenRequest;
import org.intermine.storage.MemoryStorage;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        includes = {
                StorageModule.class,
                SpiceModule.class
        },
        injects = {
                MemoryStorage.class,
                InterMineApplication.class,
                StartActivity.class,
                GenesListActivity.class,
                GeneViewActivity.class,
                SettingsActivity.class,
                TemplateActivity.class,
                TemplateResultsActivity.class,
                MainActivity.class,
                GenesListActivity.class,
                ListsFragment.class,
                SearchFragment.class,
                GenesListFragment.class,
                TemplatesFragment.class,
                LogInFragment.class,
                GeneViewFragment.class,
                PreferencesFragment.class,
                GetModelRequest.class,
                GeneSearchRequest.class,
                GeneSearchResultsCountRequest.class,
                GetTemplateResultsRequest.class,
                GetTemplatesRequest.class,
                AppendGenesToListRequest.class,
                CreateGenesList.class,
                GetListsRequest.class,
                GetUserTokenRequest.class,
                PostAuthRequest.class,
                FetchListResultsRequest.class,
                NavigationDrawerFragment.class
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
