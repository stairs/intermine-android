package org.intermine.app.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.intermine.app.core.Gene;
import org.intermine.app.core.GenesList;
import org.intermine.app.util.Strs;

import java.lang.reflect.Type;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class GeneSearchResultDeserializer implements JsonDeserializer<GenesList> {
    public static final String RELEVANCE_FIELD = "relevance";
    public static final String FIELDS_FIELD = "fields";
    public static final String PRIMARY_ID_FIELD = "primaryIdentifier";
    public static final String SECONDARY_ID_FIELD = "secondaryIdentifier";
    public static final String ORGANISM_NAME_FIELD = "organism.name";
    public static final String SYMBOL_FIELD = "symbol";
    public static final String NAME_FIELD = "name";
    public static final String LOCATION_START_FIELD = "chromosomeLocation.start";
    public static final String LOCATION_END_FIELD = "chromosomeLocation.end";
    public static final String ORGANISM_SHORT_NAME_FIELD = "organism.shortName";
    public static final String SEQUENCE_ONTOLOGY_TERM_FIELD = "sequenceOntologyTerm.name";

    @Override
    public GenesList deserialize(JsonElement json, Type typeOfT,
                                 JsonDeserializationContext context) throws JsonParseException {
        GenesList genesList = new GenesList();

        JsonObject jsonObject = (JsonObject) json;
        JsonObject facets = getJsonObject("facets", jsonObject);
        genesList.setResultsCount(getResultsCount(facets));

        if (null != facets) {
            JsonArray results = jsonObject.get("results").getAsJsonArray();

            if (0 < results.size()) {
                for (int i = 0; i < results.size(); i++) {
                    Gene gene = parseGeneObject(results.get(i));
                    genesList.add(gene);
                }
            }
        }
        return genesList;
    }

    protected Gene parseGeneObject(JsonElement json) {
        JsonObject jsonObject = (JsonObject) json;

        JsonObject fields = jsonObject.get(FIELDS_FIELD).getAsJsonObject();
        String name = getStringValue(NAME_FIELD, fields);
        String symbol = getStringValue(SYMBOL_FIELD, fields);
        String primaryId = getStringValue(PRIMARY_ID_FIELD, fields);
        String organismName = getStringValue(ORGANISM_NAME_FIELD, fields);
        String locationStart = getStringValue(LOCATION_START_FIELD, fields);
        String locationEnd = getStringValue(LOCATION_END_FIELD, fields);
        String organismShortName = getStringValue(ORGANISM_SHORT_NAME_FIELD, fields);
        String ontologyTerm = getStringValue(SEQUENCE_ONTOLOGY_TERM_FIELD, fields);
        String secondaryId = getStringValue(SECONDARY_ID_FIELD, fields);

        double relevance = jsonObject.get(RELEVANCE_FIELD).getAsDouble();

        Gene gene = new Gene();
        gene.setPrimaryDBId(primaryId);
        gene.setSymbol(symbol);
        gene.setName(name);
        gene.setRelevance(relevance);
        gene.setOrganismName(organismName);
        gene.setOrganismShortName(organismShortName);
        gene.setLocationStart(locationStart);
        gene.setLocationEnd(locationEnd);
        gene.setSecondaryIdentifier(secondaryId);
        gene.setOntologyTerm(ontologyTerm);
        return gene;
    }

    protected String getStringValue(String key, JsonObject jsonObj) {
        JsonElement item = jsonObj.get(key);

        if (null != item) {
            return item.getAsString();
        }
        return Strs.EMPTY_STRING;
    }

    protected JsonObject getJsonObject(String key, JsonObject jsonObj) {
        JsonElement item = jsonObj.get(key);

        if (null != item) {
            return item.getAsJsonObject();
        }
        return null;
    }

    private int getResultsCount(JsonObject obj) {
        JsonObject category = getJsonObject("Category", obj);

        if (null != category) {
            JsonElement resultsCount = category.get("Gene");

            if (null != resultsCount) {
                return resultsCount.getAsInt();
            }
        }
        return 0;
    }
}
