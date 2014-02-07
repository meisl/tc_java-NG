package plugins.wdx;


import plugins.TypedConstant;
import plugins.AbstractTypedConstant;

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


    public static final class NOMOREFIELDS extends AbstractTypedConstant implements RV.GetSupportedField {
        public static final NOMOREFIELDS instance = new NOMOREFIELDS();
        private NOMOREFIELDS() { super(FieldValue.FT_NOMOREFIELDS); }
    }

    public static final class NOSUCHFIELD extends AbstractTypedConstant implements RV.GetValueCanDelay, RV.GetValueImmediate, RV.SetValue, RV.EditValue {
        public static final NOSUCHFIELD instance = new NOSUCHFIELD();
        private NOSUCHFIELD() { super(FieldValue.FT_NOSUCHFIELD); }
    }

    public static final class FILEERROR extends AbstractTypedConstant implements RV.GetValueCanDelay, RV.GetValueImmediate, RV.SetValue {
        public static final FILEERROR instance = new FILEERROR();
        private FILEERROR() { super(FieldValue.FT_FILEERROR); }
    }

    public static final class FIELDEMPTY extends AbstractTypedConstant implements RV.GetValueCanDelay, RV.GetValueImmediate {
        public static final FIELDEMPTY instance = new FIELDEMPTY();
        private FIELDEMPTY() { super(FieldValue.FT_FIELDEMPTY); }
    }

    public static final class DELAYED extends AbstractTypedConstant implements RV.GetValueCanDelay {
        public static final DELAYED instance = new DELAYED();
        private DELAYED() { super(FieldValue.FT_DELAYED); }
    }

    public static final class ONDEMAND extends AbstractTypedConstant implements RV.GetValueCanDelay {
        public static final ONDEMAND instance = new ONDEMAND();
        private ONDEMAND() { super(FieldValue.FT_ONDEMAND); }
    }

    public static final class SETSUCCESS extends AbstractTypedConstant implements RV.SetValue, RV.EditValue {
        public static final SETSUCCESS instance = new SETSUCCESS();
        private SETSUCCESS() { super(0); } // FT_SETSUCCESS is missing from original tc-apis
    }

    public static final class SETCANCEL extends AbstractTypedConstant implements RV.EditValue {
        public static final SETCANCEL instance = new SETCANCEL();
        private SETCANCEL() { super(FieldValue.FT_SETCANCEL); }
    }


    /* ---- return codes for contentGetSupportedFieldFlags ------------------------ */


    public static final class CONTFLAGS_EDIT extends AbstractTypedConstant implements RV.GetSupportedFieldFlags {
        public static final CONTFLAGS_EDIT instance = new CONTFLAGS_EDIT();
        private CONTFLAGS_EDIT() { super(WDXPluginInterface.CONTFLAGS_EDIT); }
    }

    public static final class CONTFLAGS_FIELDEDIT extends AbstractTypedConstant implements RV.GetSupportedFieldFlags {
        public static final CONTFLAGS_FIELDEDIT instance = new CONTFLAGS_FIELDEDIT();
        private CONTFLAGS_FIELDEDIT() { super(16); } // CONTFLAGS_FIELDEDIT is missing from WDXPluginInterface
    }

    public static final class CONTFLAGS_PASSTHROUGH_SIZE_FLOAT extends AbstractTypedConstant implements RV.GetSupportedFieldFlags {
        public static final CONTFLAGS_PASSTHROUGH_SIZE_FLOAT instance = new CONTFLAGS_PASSTHROUGH_SIZE_FLOAT();
        private CONTFLAGS_PASSTHROUGH_SIZE_FLOAT() { super(WDXPluginInterface.CONTFLAGS_PASSTHROUGH_SIZE_FLOAT); }
    }

    public static final class CONTFLAGS_SUBSTMASK extends AbstractTypedConstant implements RV.GetSupportedFieldFlags {
        public static final CONTFLAGS_SUBSTMASK instance = new CONTFLAGS_SUBSTMASK();
        private CONTFLAGS_SUBSTMASK() { super(Constants.Set.of(Flags.Subst.values()).mask); }
    }

    public static final class CONTFLAGS_SUBSTMASK_EDIT extends AbstractTypedConstant implements RV.GetSupportedFieldFlags {
        public static final CONTFLAGS_SUBSTMASK_EDIT instance = new CONTFLAGS_SUBSTMASK_EDIT();
        private CONTFLAGS_SUBSTMASK_EDIT() { super(CONTFLAGS_SUBSTMASK_EDIT.instance.intValue() | CONTFLAGS_EDIT.instance.intValue()); }
    }



    /* ---- field types (also used as return codes for contentGetValue) ----------- */

    /*
    public static final class BOOLEAN extends AbstractTypedConstant implements FieldType, RV.GetValueCanDelay, RV.GetValueImmediate {
        public static final BOOLEAN instance = new BOOLEAN();
        public int intValue() { return FieldValue.FT_BOOLEAN; }
        private BOOLEAN() {}
    }

    public static final class STRING extends AbstractTypedConstant implements FieldType, RV.GetValueCanDelay, RV.GetValueImmediate {
        public static final STRING instance = new STRING();
        public int intValue() { return FieldValue.FT_STRING; }
        private STRING() {}
    }

    public static final class NUMERIC_32 extends AbstractTypedConstant implements FieldType, RV.GetValueCanDelay, RV.GetValueImmediate {
        public static final NUMERIC_32 instance = new NUMERIC_32();
        public int intValue() { return FieldValue.FT_NUMERIC_32; }
        private NUMERIC_32() {}
    }

    public static final class NUMERIC_64 extends AbstractTypedConstant implements FieldType, RV.GetValueCanDelay, RV.GetValueImmediate {
        public static final NUMERIC_64 instance = new NUMERIC_64();
        public int intValue() { return FieldValue.FT_NUMERIC_64; }
        private NUMERIC_64() {}
    }

    public static final class NUMERIC_FLOATING extends AbstractTypedConstant implements FieldType, RV.GetValueCanDelay, RV.GetValueImmediate {
        public static final NUMERIC_FLOATING instance = new NUMERIC_FLOATING();
        public int intValue() { return FieldValue.FT_NUMERIC_FLOATING; }
        private NUMERIC_FLOATING() {}
    }

    public static final class DATE extends AbstractTypedConstant implements FieldType, RV.GetValueCanDelay, RV.GetValueImmediate {
        public static final DATE instance = new DATE();
        public int intValue() { return FieldValue.FT_DATE; }
        private DATE() {}
    }

    public static final class TIME extends AbstractTypedConstant implements FieldType, RV.GetValueCanDelay, RV.GetValueImmediate {
        public static final TIME instance = new TIME();
        public int intValue() { return FieldValue.FT_TIME; }
        private TIME() {}
    }

    public static final class DATETIME extends AbstractTypedConstant implements FieldType, RV.GetValueCanDelay, RV.GetValueImmediate {
        public static final DATETIME instance = new DATETIME();
        public int intValue() { return FieldValue.FT_DATETIME; }
        private DATETIME() {}
    }

    public static final class MULTIPLECHOICE extends AbstractTypedConstant implements FieldType, RV.GetValueCanDelay, RV.GetValueImmediate {
        public static final MULTIPLECHOICE instance = new MULTIPLECHOICE();
        public int intValue() { return FieldValue.FT_MULTIPLECHOICE; }
        private MULTIPLECHOICE() {}
    }

    public static final class FULLTEXT extends AbstractTypedConstant implements FieldType, RV.GetValueCanDelay, RV.GetValueImmediate {
        public static final FULLTEXT instance = new FULLTEXT();
        public int intValue() { return FieldValue.FT_FULLTEXT; }
        private FULLTEXT() {}
    }
    */
}