package plugins.wdx;


import plugins.TypedConstant;


public enum State implements TypedConstant {

    READNEWDIR      (WDXPluginInterface.CONTST_READNEWDIR),
    REFRESHPRESSED  (WDXPluginInterface.CONTST_REFRESHPRESSED),
    SHOWHINT        (WDXPluginInterface.CONTST_SHOWHINT);

    private final int intValue;

    State(int intValue) {
        this.intValue = intValue;
    }

    public int intValue() {
        return intValue;
    }

    static State fromInt(int i) {
        if (i == READNEWDIR    .intValue) return READNEWDIR;     else
        if (i == REFRESHPRESSED.intValue) return REFRESHPRESSED; else
        if (i == SHOWHINT      .intValue) return SHOWHINT;       else
        return null;
    }

}
