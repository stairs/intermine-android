package org.intermine.app.listener;

/*
 * Copyright (C) 2015 InterMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import org.intermine.app.core.Gene;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public interface OnGeneSelectedListener {
    void onGeneSelected(Gene gene);
}