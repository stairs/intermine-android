package org.intermine.app.util;

/*
 * Copyright (C) 2015 InterMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import android.content.Intent;

import org.intermine.app.core.Gene;

import java.util.List;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class Sharing {
    private static final String EMAIL_SUBJECT_TEMPLATE = "%s/%s, Gene Details";
    private static final String GENES_LIST_EMAIL_SUBJECT = "List of Genes from InterMine";

    public static Intent generateIntentToSendText(String subject, String body) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        intent.setType("text/plain");
        return intent;
    }

    public static Intent generateIntentToSendText(List<Gene> genes) {
        StringBuilder emailContent = new StringBuilder();
        if (null != genes && !genes.isEmpty()) {
            for (Gene gene : genes) {
                emailContent.append(generateMessageContent(gene)).append("\n\n");
            }
        }
        return generateIntentToSendText(GENES_LIST_EMAIL_SUBJECT, emailContent.toString());
    }

    public static Intent generateIntentToSendText(Gene gene) {
        String subject = generateMessageSubject(gene);
        String body = generateMessageContent(gene);
        return generateIntentToSendText(subject, body);
    }

    protected static String generateMessageSubject(Gene gene) {
        return String.format(EMAIL_SUBJECT_TEMPLATE, gene.getSymbol(), gene.getPrimaryDBId());
    }

    protected static String generateMessageContent(Gene gene) {
        StringBuilder builder = new StringBuilder();
        addRowIfNotEmpty("Standard Name", gene.getSymbol(), builder);
        addRowIfNotEmpty("Systematic Name", gene.getPrimaryDBId(), builder);
        addRowIfNotEmpty("Secondary ID", gene.getSecondaryIdentifier(), builder);
        addRowIfNotEmpty("Organism Name", gene.getOrganismName(), builder);
        addRowIfNotEmpty("Organism Short Name", gene.getOrganismShortName(), builder);
        addRowIfNotEmpty("Name Description", gene.getName(), builder);
        addRowIfNotEmpty("Ontology Term", gene.getOntologyTerm(), builder);

        if (!Strs.isNullOrEmpty(gene.getLocationStart()) &&
                !Strs.isNullOrEmpty(gene.getLocationEnd())) {
            builder.append("Chromosomal Location: ").append(gene.getLocationStart()).
                    append(" to ").append(gene.getLocationEnd());
        }
        return builder.toString();
    }

    private static void addRowIfNotEmpty(String title, String value, StringBuilder builder) {
        if (!Strs.isNullOrEmpty(value)) {
            builder.append(String.format("%s: %s\n", title, value));
        }
    }
}
