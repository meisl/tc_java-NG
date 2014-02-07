package plugins.wdx;


import plugins.TypedConstant;


public interface RV {

    public static interface GetSupportedField extends TypedConstant {
        public static final GetSupportedField NOMOREFIELDS = Constants.NOMOREFIELDS.instance;
        // or one of the FieldType enum constants
    }

    public static interface GetSupportedFieldFlags extends TypedConstant {
        public static final GetSupportedFieldFlags EDIT           = Constants.CONTFLAGS_EDIT.instance;
        public static final GetSupportedFieldFlags SUBSTMASK      = Constants.CONTFLAGS_SUBSTMASK.instance;
        public static final GetSupportedFieldFlags SUBSTMASK_EDIT = Constants.CONTFLAGS_SUBSTMASK_EDIT.instance;
        // or one of the Flags.Subst enum constants
    }

    public static interface GetValueImmediate extends TypedConstant {
        public static final GetValueImmediate NOSUCHFIELD      = Constants.NOSUCHFIELD.instance;
        public static final GetValueImmediate FILEERROR        = Constants.FILEERROR.instance;
        public static final GetValueImmediate FIELDEMPTY       = Constants.FIELDEMPTY.instance;
        // or one of the FieldType enum constants
    }

    public static interface GetValueCanDelay extends TypedConstant {
        public static final GetValueCanDelay NOSUCHFIELD      = Constants.NOSUCHFIELD.instance;
        public static final GetValueCanDelay FILEERROR        = Constants.FILEERROR.instance;
        public static final GetValueCanDelay FIELDEMPTY       = Constants.FIELDEMPTY.instance;
        public static final GetValueCanDelay DELAYED          = Constants.DELAYED.instance;
        public static final GetValueCanDelay ONDEMAND         = Constants.ONDEMAND.instance;
        // or one of the FieldType enum constants
    }

    public static interface SetValue extends TypedConstant {
        public static final SetValue NOSUCHFIELD = Constants.NOSUCHFIELD.instance;
        public static final SetValue FILEERROR   = Constants.FILEERROR.instance;
        public static final SetValue SETSUCCESS  = Constants.SETSUCCESS.instance;
    }

    public static interface EditValue extends TypedConstant {
        public static final EditValue NOSUCHFIELD = Constants.NOSUCHFIELD.instance;
        public static final EditValue SETSUCCESS  = Constants.SETSUCCESS.instance;
        public static final EditValue SETCANCEL   = Constants.SETCANCEL.instance;
    }

}
