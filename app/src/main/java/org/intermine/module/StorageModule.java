package org.intermine.module;

import android.content.Context;
import android.content.SharedPreferences;

import org.intermine.storage.MemoryStorage;
import org.intermine.storage.Storage;

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
