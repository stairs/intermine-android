package org.intermine.app.core.templates.constraint;

public enum ConstraintOperation {
    EQUALS("="), EXACT_MATCH("=="), NOT_EQUALS("!="), STRICT_NOT_EQUALS("!=="),
    LESS_THAN("<"), LESS_THAN_EQUALS("<="), GREATER_THAN(">"), GREATER_THAN_EQUALS(">="),
    MATCHES("LIKE"), DOES_NOT_MATCH("NOT LIKE"), IS_NULL("IS NULL"), IS_EMPTY("IS NULL"),
    IS_NOT_NULL("IS NOT NULL"), IS_NOT_EMPTY("IS NULL"), CONTAINS("CONTAINS"),
    DOES_NOT_CONTAIN("DOES NOT CONTAIN"), IN("IN"), NOT_IN("NOT IN"), EXISTS("CONTAINS"),
    DOES_NOT_EXIST("DOES NOT CONTAIN"), AND("AND"), OR("OR"), NAND("NAND"), NOR("NOR"),
    LOOKUP("LOOKUP"), OVERLAPS("OVERLAPS"), DOES_NOT_OVERLAP("DOES NOT OVERLAP"), ONE_OF("ONE OF"),
    NONE_OF("NONE OF"), WITHIN("WITHIN"), OUTSIDE("OUTSIDE"), ISA("ISA"), ISNT("ISNT"), HAS("HAS"),
    DOES_NOT_HAVE("DOES NOT HAVE");

    private final String mName;

    private ConstraintOperation(String name) {
        this.mName = name;
    }

    public static ConstraintOperation valueByName(String name) {
        for (ConstraintOperation op : values()) {
            if (op.getName().equals(name)) {
                return op;
            }
        }
        return null;
    }

    /**
     * Get the negated op
     *
     * @return the negated op
     */
    public ConstraintOperation negate() {
        if (this == EQUALS) {
            return NOT_EQUALS;
        } else if (this == EXACT_MATCH) {
            return STRICT_NOT_EQUALS;
        } else if (this == NOT_EQUALS) {
            return EQUALS;
        } else if (this == STRICT_NOT_EQUALS) {
            return EXACT_MATCH;
        } else if (this == LESS_THAN) {
            return GREATER_THAN_EQUALS;
        } else if (this == GREATER_THAN_EQUALS) {
            return LESS_THAN;
        } else if (this == GREATER_THAN) {
            return LESS_THAN_EQUALS;
        } else if (this == LESS_THAN_EQUALS) {
            return GREATER_THAN;
        } else if (this == MATCHES) {
            return DOES_NOT_MATCH;
        } else if (this == DOES_NOT_MATCH) {
            return MATCHES;
        } else if (this == IS_NULL) {
            return IS_NOT_NULL;
        } else if (this == IS_NOT_NULL) {
            return IS_NULL;
        } else if (this == CONTAINS) {
            return DOES_NOT_CONTAIN;
        } else if (this == DOES_NOT_CONTAIN) {
            return CONTAINS;
        } else if (this == IN) {
            return NOT_IN;
        } else if (this == NOT_IN) {
            return IN;
        } else if (this == AND) {
            return NAND;
        } else if (this == NAND) {
            return AND;
        } else if (this == OR) {
            return NOR;
        } else if (this == NOR) {
            return OR;
        } else if (this == ONE_OF) {
            return NONE_OF;
        } else if (this == NONE_OF) {
            return ONE_OF;
        } else if (this == WITHIN) {
            return OUTSIDE;
        } else if (this == OUTSIDE) {
            return WITHIN;
        } else if (this == ISA) {
            return ISNT;
        } else if (this == ISNT) {
            return ISA;
        }
        throw new IllegalArgumentException("Unknown operation!");
    }

    private String getName() {
        return mName;
    }

    @Override
    public String toString() {
        return mName;
    }
}
