package org.intermine.module;

import com.octo.android.robospice.SpiceManager;

import org.intermine.service.RoboSpiceService;

import dagger.Module;
import dagger.Provides;

@Module(
        library = true
)
public final class SpiceModule {
    @Provides
    SpiceManager provideSpiceManager() {
        return new SpiceManager(RoboSpiceService.class);
    }
}
