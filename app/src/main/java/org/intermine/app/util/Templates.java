package org.intermine.app.util;

import org.intermine.app.core.model.Model;
import org.intermine.app.core.templates.constraint.Constraint;
import org.intermine.app.core.templates.constraint.ConstraintOperation;
import org.intermine.app.core.templates.constraint.PathConstraint;
import org.intermine.app.core.templates.constraint.PathConstraintAttribute;
import org.intermine.app.core.templates.constraint.PathConstraintLookup;
import org.intermine.app.core.templates.constraint.PathConstraintSimpleMultiValue;
import org.intermine.app.core.templates.constraint.SwitchOffAbility;

import java.util.Collection;
import java.util.List;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class Templates {
    private Templates() {

    }

    public static List<PathConstraint> convertToPathConstraints(
            Collection<Constraint> constraints, Model model) {
        List<PathConstraint> typedConstraints = Collections.newArrayList();

        for (Constraint constraint : constraints) {
            if (!SwitchOffAbility.OFF.toString().equals(constraint.getSwitched())) {
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
}
