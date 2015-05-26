package org.intermine.app.module;

/*
 * Copyright (C) 2015 InterMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

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
