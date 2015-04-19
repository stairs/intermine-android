package org.intermine.app.module;

import android.content.Context;
import android.content.SharedPreferences;

import org.intermine.app.storage.MemoryStorage;
import org.intermine.app.storage.Storage;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        complete = false,
        library = true
)
public final class StorageModule {
    @Provides
    @Singleton
    Storage provideStorage(Context context, SharedPreferences sharedPreferences) {
        return new MemoryStorage(context);
    }
}
