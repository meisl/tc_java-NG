package plugins.wdx;


import plugins.TypedConstant;

import static plugins.wdx.FieldValue.*;


public final class Constants {

    static final boolean isSet(TypedConstant c, int flags) {
        return (flags & c.intValue()) != 0;
    }

    static class Set<T extends TypedConstant> {
        static <C extends TypedConstant> Set<C> fromInt(int mask) {
            return new Set<C>(mask);
        }

        @SuppressWarnings("unchecked") // OK since we do not *write* to the varargs array
        static <C extends TypedConstant> Set<C> of(C... constants) {
            int mask = 0;
            for (C c: constants) {
                mask |= c.intValue();
            }
            return fromInt(mask);
        }

        final int mask;

        private Set(int mask) {
            this.mask = mask;
        }

        public boolean contains(T constant) {
            return isSet(constant, this.mask);
        }
    }

    /* ---- return codes ---------------------------------------------------- */


    public static final class NOMOREFIELDS implements RV.GetSupportedField {
        public static final NOMOREFIELDS instance = new NOMOREFIELDS();
        public int intValue() { return FieldValue.FT_NOMOREFIELDS; }
        private NOMOREFIELDS() {}
    }

    static final class NOSUCHFIELD implements RV.GetValueCanDelay, RV.GetValueImmediate, RV.SetValue, RV.EditValue {
        public static final NOSUCHFIELD instance = new NOSUCHFIELD();
        public int intValue() { return FieldValue.FT_NOSUCHFIELD; }
        private NOSUCHFIELD() {}
    }

    static final class FILEERROR implements RV.GetValueCanDelay, RV.GetValueImmediate, RV.SetValue {
        public static final FILEERROR instance = new FILEERROR();
        public int intValue() { return FieldValue.FT_FILEERROR; }
        private FILEERROR() {}
    }

    static final class FIELDEMPTY implements RV.GetValueCanDelay, RV.GetValueImmediate {
        public static final FIELDEMPTY instance = new FIELDEMPTY();
        public int intValue() { return FieldValue.FT_FIELDEMPTY; }
        private FIELDEMPTY() {}
    }

    static final class DELAYED implements RV.GetValueCanDelay {
        public static final DELAYED instance = new DELAYED();
        public int intValue() { return FieldValue.FT_DELAYED; }
        private DELAYED() {}
    }

    static final class ONDEMAND implements RV.GetValueCanDelay {
        public static final ONDEMAND instance = new ONDEMAND();
        public int intValue() { return FieldValue.FT_ONDEMAND; }
        private ONDEMAND() {}
    }

    static final class SETSUCCESS implements RV.SetValue, RV.EditValue {
        public static final SETSUCCESS instance = new SETSUCCESS();
        public int intValue() { return 0; } // FT_SETSUCCESS is missing from original tc-apis
        private SETSUCCESS() {}
    }

    static final class SETCANCEL implements RV.EditValue {
        public static final SETCANCEL instance = new SETCANCEL();
        public int intValue() { return FieldValue.FT_SETCANCEL; }
        private SETCANCEL() {}
    }


    /* ---- return codes for contentGetSupportedFieldFlags ------------------------ */


    static final class CONTFLAGS_EDIT implements RV.GetSupportedFieldFlags {
        public static final CONTFLAGS_EDIT instance = new CONTFLAGS_EDIT();
        public int intValue() { return WDXPluginInterface.CONTFLAGS_EDIT; }
        private CONTFLAGS_EDIT() {}
    }

    static final class CONTFLAGS_FIELDEDIT implements RV.GetSupportedFieldFlags {
        public static final CONTFLAGS_FIELDEDIT instance = new CONTFLAGS_FIELDEDIT();
        public int intValue() { return 16; } // CONTFLAGS_FIELDEDIT is missing from WDXPluginInterface
        private CONTFLAGS_FIELDEDIT() {}
    }

    static final class CONTFLAGS_PASSTHROUGH_SIZE_FLOAT implements RV.GetSupportedFieldFlags {
        public static final CONTFLAGS_PASSTHROUGH_SIZE_FLOAT instance = new CONTFLAGS_PASSTHROUGH_SIZE_FLOAT();
        public int intValue() { return WDXPluginInterface.CONTFLAGS_PASSTHROUGH_SIZE_FLOAT; }
        private CONTFLAGS_PASSTHROUGH_SIZE_FLOAT() {}
    }

    static final class CONTFLAGS_SUBSTMASK implements RV.GetSupportedFieldFlags {
        public static final CONTFLAGS_SUBSTMASK instance = new CONTFLAGS_SUBSTMASK();
        public int intValue() {
            return Constants.Set.of(Flags.Subst.values()).mask;
        }
        private CONTFLAGS_SUBSTMASK() {}
    }

    static final class CONTFLAGS_SUBSTMASK_EDIT implements RV.GetSupportedFieldFlags {
        public static final CONTFLAGS_SUBSTMASK_EDIT instance = new CONTFLAGS_SUBSTMASK_EDIT();
        public int intValue() {
            return CONTFLAGS_SUBSTMASK_EDIT.instance.intValue() | CONTFLAGS_EDIT.instance.intValue();
        }
        private CONTFLAGS_SUBSTMASK_EDIT() {}
    }



    /* ---- field types (also used as return codes for contentGetValue) ----------- */

    /*
    static final class BOOLEAN implements FieldType, RV.GetValueCanDelay, RV.GetValueImmediate {
        public static final BOOLEAN instance = new BOOLEAN();
        public int intValue() { return FieldValue.FT_BOOLEAN; }
        private BOOLEAN() {}
    }

    static final class STRING implements FieldType, RV.GetValueCanDelay, RV.GetValueImmediate {
        public static final STRING instance = new STRING();
        public int intValue() { return FieldValue.FT_STRING; }
        private STRING() {}
    }

    static final class NUMERIC_32 implements FieldType, RV.GetValueCanDelay, RV.GetValueImmediate {
        public static final NUMERIC_32 instance = new NUMERIC_32();
        public int intValue() { return FieldValue.FT_NUMERIC_32; }
        private NUMERIC_32() {}
    }

    static final class NUMERIC_64 implements FieldType, RV.GetValueCanDelay, RV.GetValueImmediate {
        public static final NUMERIC_64 instance = new NUMERIC_64();
        public int intValue() { return FieldValue.FT_NUMERIC_64; }
        private NUMERIC_64() {}
    }

    static final class NUMERIC_FLOATING implements FieldType, RV.GetValueCanDelay, RV.GetValueImmediate {
        public static final NUMERIC_FLOATING instance = new NUMERIC_FLOATING();
        public int intValue() { return FieldValue.FT_NUMERIC_FLOATING; }
        private NUMERIC_FLOATING() {}
    }

    static final class DATE implements FieldType, RV.GetValueCanDelay, RV.GetValueImmediate {
        public static final DATE instance = new DATE();
        public int intValue() { return FieldValue.FT_DATE; }
        private DATE() {}
    }

    static final class TIME implements FieldType, RV.GetValueCanDelay, RV.GetValueImmediate {
        public static final TIME instance = new TIME();
        public int intValue() { return FieldValue.FT_TIME; }
        private TIME() {}
    }

    static final class DATETIME implements FieldType, RV.GetValueCanDelay, RV.GetValueImmediate {
        public static final DATETIME instance = new DATETIME();
        public int intValue() { return FieldValue.FT_DATETIME; }
        private DATETIME() {}
    }

    static final class MULTIPLECHOICE implements FieldType, RV.GetValueCanDelay, RV.GetValueImmediate {
        public static final MULTIPLECHOICE instance = new MULTIPLECHOICE();
        public int intValue() { return FieldValue.FT_MULTIPLECHOICE; }
        private MULTIPLECHOICE() {}
    }

    static final class FULLTEXT implements FieldType, RV.GetValueCanDelay, RV.GetValueImmediate {
        public static final FULLTEXT instance = new FULLTEXT();
        public int intValue() { return FieldValue.FT_FULLTEXT; }
        private FULLTEXT() {}
    }
    */
}