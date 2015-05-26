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
