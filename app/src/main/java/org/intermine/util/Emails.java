package org.intermine.util;

import android.content.Intent;
import android.net.Uri;

import org.intermine.core.Gene;

import java.util.List;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class Emails {
    private static final String EMAIL_SUBJECT_TEMPLATE = "%s/%s, Gene Details";
    private static final String GENES_LIST_EMAIL_SUBJECT = "List of Genes from InterMine";

    public static Intent generateIntentToSendEmail(String subject, String body) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "", null));
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        return intent;
    }

    public static Intent generateIntentToSendEmails(List<Gene> genes) {
        StringBuilder emailContent = new StringBuilder();
        if (null != genes && !genes.isEmpty()) {
            for (Gene gene : genes) {
                emailContent.append(generateEmailContent(gene)).append("\n\n");
            }
        }
        return generateIntentToSendEmail(GENES_LIST_EMAIL_SUBJECT, emailContent.toString());
    }

    public static Intent generateIntentToSendEmail(Gene gene) {
        String subject = generateEmailSubject(gene);
        String body = generateEmailContent(gene);
        return generateIntentToSendEmail(subject, body);
    }

    protected static String generateEmailSubject(Gene gene) {
        return String.format(EMAIL_SUBJECT_TEMPLATE, gene.getSymbol(), gene.getPrimaryDBId());
    }

    protected static String generateEmailContent(Gene gene) {
        StringBuilder builder = new StringBuilder();
        //TODO: refactore
        addRowIfNotEmpty("Standard Name", gene.getSymbol(), builder);
        addRowIfNotEmpty("Systematic Name", gene.getPrimaryDBId(), builder);
        addRowIfNotEmpty("Secondary ID", gene.getSecondaryIdentifier(), builder);
        addRowIfNotEmpty("Organism Name", gene.getOrganismName(), builder);
        addRowIfNotEmpty("Organism Short Name", gene.getOrganismShortName(), builder);
        addRowIfNotEmpty("Name Description", gene.getName(), builder);
        addRowIfNotEmpty("Ontology Term", gene.getOntologyTerm(), builder);

        if (!Strs.isNullOrEmpty(gene.getLocationStart()) &&
                !Strs.isNullOrEmpty(gene.getLocationEnd())) {
            builder.append("Chromosomal Location: " + gene.getLocationStart()
                    + " to " + gene.getLocationEnd());
        }
        return builder.toString();
    }

    private static void addRowIfNotEmpty(String title, String value, StringBuilder builder) {
        if (!Strs.isNullOrEmpty(value)) {
            builder.append(String.format("%s: %s\n", title, value));
        }
    }
}
