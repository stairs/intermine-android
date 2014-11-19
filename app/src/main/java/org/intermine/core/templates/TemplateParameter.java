package org.intermine.core.templates;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * The TemplateParameter model is simple model representing template parameter. Template parameter
 * is predefined constraint where you can specify constraint operation and constrained
 * value. For example it can be constraint for gene length and you can specify if the gene length
 * should be less, equal or greater then your specified value.
 * <p/>
 * Parameters of some templates require extra value. See documentation of the template or InterMine
 * web page that generates template URL for user and displays it readily.
 *
 * @author Jakub Kulaviak
 */
public class TemplateParameter implements Parcelable {

    private String operation;

    private String value;

    private String extraValue;

    private String pathId;

    private List<String> values;

    private String code;

    /**
     * Create a new TemplateParameter.
     *
     * @param pathId     The path-string for this constraint.
     * @param operation  The operation you wish to use.
     * @param value      The value the constraint should be run against.
     * @param extraValue Any extra constraining value this operation might need.
     */
    public TemplateParameter(String pathId, String operation,
                             String value, String extraValue, String code) {
        super();
        this.pathId = pathId;
        this.operation = operation;
        this.value = value;
        this.values = null;
        this.extraValue = extraValue;
        this.code = code;
    }

    public TemplateParameter(String pathId, String operation, List<String> values, String code) {
        super();
        this.pathId = pathId;
        this.operation = operation;
        this.values = values;
        this.value = null;
        this.extraValue = null;
        this.code = code;
    }

    /**
     * Create a new TemplateParameter.
     *
     * @param pathId    The path-string for this constraint.
     * @param operation The operation you wish to use.
     * @param value     The value the constraint should be run against.
     */
    public TemplateParameter(String pathId, String operation, String value, String code) {
        super();
        this.operation = operation;
        this.value = value;
        this.pathId = pathId;
        this.extraValue = null;
        this.values = null;
        this.code = code;
    }

    protected TemplateParameter(Parcel in) {
        operation = in.readString();
        value = in.readString();
        pathId = in.readString();
        extraValue = in.readString();
        values = in.readArrayList(null);
        code = in.readString();
    }

    /**
     * @return the provided code *
     */
    public String getCode() {
        return code;
    }

    /**
     * @return the path-string associated with this constraint.
     */
    public String getPathId() {
        return pathId;
    }

    /**
     * @return extra value
     */
    public String getExtraValue() {
        return extraValue;
    }

    /**
     * @return The collection of multi-values
     */
    public Collection<String> getValues() {
        return values;
    }

    public boolean isMultiValue() {
        return values != null;
    }

    /**
     * @return operation
     */
    public String getOperation() {
        return operation;
    }

    /**
     * Returns value.
     *
     * @return value
     */
    public String getValue() {
        return value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(operation);
        dest.writeString(value);
        dest.writeString(pathId);
        dest.writeString(extraValue);
        dest.writeStringList(values);
        dest.writeString(code);
    }

    public static final Parcelable.Creator<TemplateParameter> CREATOR = new Parcelable.Creator<TemplateParameter>() {
        @Override
        public TemplateParameter createFromParcel(Parcel in) {
            return new TemplateParameter(in);
        }

        @Override
        public TemplateParameter[] newArray(int size) {
            return new TemplateParameter[size];
        }
    };
}
