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

import org.intermine.app.core.model.Model;
import org.intermine.app.core.templates.TemplateParameter;
import org.intermine.app.core.templates.constraint.Constraint;
import org.intermine.app.core.templates.constraint.ConstraintOperation;
import org.intermine.app.core.templates.constraint.PathConstraint;
import org.intermine.app.core.templates.constraint.PathConstraintAttribute;
import org.intermine.app.core.templates.constraint.PathConstraintLookup;
import org.intermine.app.core.templates.constraint.PathConstraintSimpleMultiValue;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.intermine.app.core.templates.constraint.SwitchOffAbility.OFF;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class Templates {
    private static final String CONSTRAINT_PARAM = "constraint";
    private static final String OP_PARAM = "op";
    private static final String VALUE_PARAM = "value";
    private static final String EXTRA_PARAM = "extra";
    private static final String CODE_PARAM = "code";
    private static final String VALUES_PARAM = "values";

    private Templates() {
    }

    public static List<PathConstraint> convertToPathConstraints(
            Collection<Constraint> constraints, Model model) {
        List<PathConstraint> typedConstraints = Collections.newArrayList();

        for (Constraint constraint : constraints) {
            if (OFF != constraint.getSwitched() && constraint.isEditable()) {
                typedConstraints.add(convertToPathConstraint(constraint, model));
            }
        }
        return typedConstraints;
    }

    public static PathConstraint convertToPathConstraint(Constraint constraint, Model model) {

        ConstraintOperation operation = ConstraintOperation.valueByName(constraint.getOperation());
        List<String> values = constraint.getValues();
        String path = constraint.getPath();

        if (PathConstraintSimpleMultiValue.VALID_OPERATIONS.contains(operation)
                && !values.isEmpty()) {
            return new PathConstraintSimpleMultiValue(path, operation, values, constraint.getCode());
        } else if (ConstraintOperation.LOOKUP.equals(operation)) {
            return new PathConstraintLookup(path, constraint.getValue(),
                    constraint.getExtraValue(), constraint.getCode());
        } else if (PathConstraintAttribute.VALID_OPERATIONS.contains(operation)) {
            return new PathConstraintAttribute(path, operation,
                    constraint.getValue(), constraint.getCode());
        }
        return null;
    }

    public static Map<String, String> generateUrlParams(List<TemplateParameter> parameters) {
        Map<String, String> params = Collections.newHashMap();

        for (int i = 0; i < parameters.size(); i++) {
            int index = i + 1;
            TemplateParameter param = parameters.get(i);

            params.put(CONSTRAINT_PARAM + index, param.getPathId());
            params.put(OP_PARAM + index, param.getOperation());

            String valueIndex = VALUE_PARAM + index;
            if (param.isMultiValue()) {
                for (String value : param.getValues()) {
                    params.put(valueIndex, value);
                }
            } else {
                params.put(valueIndex, param.getValue());
            }
            if (param.getExtraValue() != null) {
                params.put(EXTRA_PARAM + index, param.getExtraValue());
            }
            if (param.getCode() != null) {
                params.put(CODE_PARAM + index, param.getCode());
            }
        }
        return params;
    }
}
