package org.intermine.app.module;

import com.octo.android.robospice.SpiceManager;

import org.intermine.app.service.RoboSpiceService;

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
