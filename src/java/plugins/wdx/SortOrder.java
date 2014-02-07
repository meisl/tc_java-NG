package plugins.wdx;


import plugins.TypedConstant;


public enum SortOrder implements TypedConstant {
    ASCENDING (+1),
    DESCENDING(-1);

    private final int intValue;

    SortOrder(int intValue) {
        this.intValue = intValue;
    }

    public int intValue() {
        return intValue;
    }

    static SortOrder fromInt(int i) {
        if (i == ASCENDING  .intValue) return ASCENDING;  else
        if (i == DESCENDING .intValue) return DESCENDING; else
        return null;
    }
}