package plugins.wdx;


import plugins.TypedConstant;


public interface Flags {

    public static enum Subst implements TypedConstant, RV.GetSupportedFieldFlags {
        SIZE        (WDXPluginInterface.CONTFLAGS_SUBSTSIZE        ),
        DATETIME    (WDXPluginInterface.CONTFLAGS_SUBSTDATETIME    ),
        DATE        (WDXPluginInterface.CONTFLAGS_SUBSTDATE        ),
        TIME        (WDXPluginInterface.CONTFLAGS_SUBSTTIME        ),
        ATTRIBUTES  (WDXPluginInterface.CONTFLAGS_SUBSTATTRIBUTES  ),
        ATTRIBUTESTR(WDXPluginInterface.CONTFLAGS_SUBSTATTRIBUTESTR);

        private final int intValue;

        Subst(int intValue) {
            this.intValue = intValue;
        }

        public int intValue() {
            return intValue;
        }

        static Subst fromInt(int i) {
            if (i == SIZE        .intValue) return SIZE        ; else
            if (i == DATETIME    .intValue) return DATETIME    ; else
            if (i == DATE        .intValue) return DATE        ; else
            if (i == TIME        .intValue) return TIME        ; else
            if (i == ATTRIBUTES  .intValue) return ATTRIBUTES  ; else
            if (i == ATTRIBUTESTR.intValue) return ATTRIBUTESTR; else
            return null;
        }
    }

    public static enum ContentGetValue implements TypedConstant {

        DELAYIFSLOW(WDXPluginInterface.CONTENT_DELAYIFSLOW),
        PASSTHROUGH(2); // CONTENT_PASSTHROUGH is missing from WDXPluginInterface

        private final int intValue;

        ContentGetValue(int intValue) {
            this.intValue = intValue;
        }

        public int intValue() {
            return intValue;
        }

        static ContentGetValue fromInt(int i) {
            if (i == DELAYIFSLOW.intValue) return DELAYIFSLOW; else
            if (i == PASSTHROUGH.intValue) return PASSTHROUGH; else
            return null;
        }
    }

    public static enum SetValue implements TypedConstant {
        FIRST_ATTRIBUTE (WDXPluginInterface.SETFLAGS_FIRST_ATTRIBUTE),
        LAST_ATTRIBUTE  (WDXPluginInterface.SETFLAGS_LAST_ATTRIBUTE),
        ONLY_DATE       (WDXPluginInterface.SETFLAGS_ONLY_DATE);

        private final int intValue;

        SetValue(int intValue) {
            this.intValue = intValue;
        }

        public int intValue() {
            return intValue;
        }

        static SetValue fromInt(int i) {
            if (i == FIRST_ATTRIBUTE.intValue) return FIRST_ATTRIBUTE; else
            if (i == LAST_ATTRIBUTE .intValue) return LAST_ATTRIBUTE ; else
            if (i == ONLY_DATE      .intValue) return ONLY_DATE      ; else
            return null;
        }
    }

    public static enum EditValue implements TypedConstant {
        INITIALIZE(1); // EDITFLAGS_INITIALIZE is missing from WDXPluginInterface

        private final int intValue;

        EditValue(int intValue) {
            this.intValue = intValue;
        }

        public int intValue() {
            return intValue;
        }

        static EditValue fromInt(int i) {
            if (i == INITIALIZE.intValue) return INITIALIZE; else
            return null;
        }
    }

}
