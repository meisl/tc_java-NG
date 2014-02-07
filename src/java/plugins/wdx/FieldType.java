package plugins.wdx;


import plugins.TypedConstant;
import static plugins.wdx.FieldValue.*;


/*
public interface FieldType extends RV.GetValue {

    public static final FieldType BOOLEAN          = Constants.BOOLEAN.instance;
    public static final FieldType STRING           = Constants.STRING.instance;
    public static final FieldType NUMERIC_32       = Constants.NUMERIC_32.instance;
    public static final FieldType NUMERIC_64       = Constants.NUMERIC_64.instance;
    public static final FieldType NUMERIC_FLOATING = Constants.NUMERIC_FLOATING.instance;
    public static final FieldType DATE             = Constants.DATE.instance;
    public static final FieldType TIME             = Constants.TIME.instance;
    public static final FieldType DATETIME         = Constants.DATETIME.instance;
    public static final FieldType MULTIPLECHOICE   = Constants.MULTIPLECHOICE.instance;
    public static final FieldType FULLTEXT         = Constants.FULLTEXT.instance;

}
*/

public enum FieldType implements RV.GetSupportedField, RV.GetValueCanDelay, RV.GetValueImmediate {

    BOOLEAN          (FT_BOOLEAN),
    STRING           (FT_STRING),
    NUMERIC_32       (FT_NUMERIC_32),
    NUMERIC_64       (FT_NUMERIC_64),
    NUMERIC_FLOATING (FT_NUMERIC_FLOATING),
    DATE             (FT_DATE),
    TIME             (FT_TIME),
    DATETIME         (FT_DATETIME),
    MULTIPLECHOICE   (FT_MULTIPLECHOICE),
    FULLTEXT         (FT_FULLTEXT);

    private final int intValue;

    FieldType(int intValue) {
        this.intValue = intValue;
    }

    public int intValue() {
        return intValue;
    }

    static FieldType fromInt(int i) {
        if (i == BOOLEAN         .intValue) return BOOLEAN;          else
        if (i == STRING          .intValue) return STRING;           else
        if (i == NUMERIC_32      .intValue) return NUMERIC_32;       else
        if (i == NUMERIC_64      .intValue) return NUMERIC_64;       else
        if (i == NUMERIC_FLOATING.intValue) return NUMERIC_FLOATING; else
        if (i == DATE            .intValue) return DATE;             else
        if (i == TIME            .intValue) return TIME;             else
        if (i == DATETIME        .intValue) return DATETIME;         else
        if (i == MULTIPLECHOICE  .intValue) return MULTIPLECHOICE;   else
        if (i == FULLTEXT        .intValue) return FULLTEXT;         else
        return null;
    }

}
