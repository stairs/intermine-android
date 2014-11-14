package org.intermine.module;

import android.content.Context;
import android.content.SharedPreferences;

import org.intermine.InterMineApplication;
import org.intermine.activity.BaseActivity;
import org.intermine.activity.MainActivity;
import org.intermine.activity.StartActivity;
import org.intermine.fragment.BaseFragment;
import org.intermine.net.request.BaseRequest;
import org.intermine.storage.MemoryStorage;
import org.intermine.storage.Storage;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        includes = {
                StorageModule.class
        },
        injects = {
                InterMineApplication.class,
                BaseActivity.class,
                StartActivity.class,
                MainActivity.class,
                BaseFragment.class,
                BaseRequest.class
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
        return context.getSharedPreferences("org.intermine", Context.MODE_PRIVATE);
    }
}
