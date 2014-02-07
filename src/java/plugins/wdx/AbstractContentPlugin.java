package plugins.wdx;


public abstract class AbstractContentPlugin extends WDXPluginAdapter implements TypesafeWDXPlugin {

    @Override
    public final int contentGetSupportedField(int fieldIndex,
                                              StringBuffer fieldName,
                                              StringBuffer units,
                                              int maxlen
    ) {
        return getSupportedField(fieldIndex, fieldName, units, maxlen).intValue();
    }

    @Override
    public final int contentGetValue(String fileName,
                                        int fieldIndex,
                                        int unitIndex,
                                        FieldValue fieldValue,
                                        int maxlen,
                                        int flags
    ) {
        Double size = Constants.isSet(Flags.ContentGetValue.PASSTHROUGH, flags)
            ? fieldValue.getDoubleValue()
            : null
        ;
        return Constants.isSet(Flags.ContentGetValue.DELAYIFSLOW, flags)
            ? getValueCanDelay (fileName, fieldIndex, unitIndex, fieldValue, maxlen, size).intValue()
            : getValueImmediate(fileName, fieldIndex, unitIndex, fieldValue, maxlen, size).intValue()
        ;
    }

    @Override
    public final int contentSetValue(
        String fileName,
        int fieldIndex,
        int unitIndex,
        int fieldType,
        FieldValue fieldValue,
        int flags
    ) {
        Constants.Set<Flags.SetValue> typedFlags = Constants.Set.fromInt(flags);
        return setValue(fileName, fieldIndex, unitIndex, FieldType.fromInt(fieldType), fieldValue, typedFlags).intValue();
    }

    @Override
    public int contentEditValue(
        int parentWin,
        int fieldIndex,
        int unitIndex,
        int fieldType,
        FieldValue fieldValue,
        int maxlen,
        int flags,
        String langidentifier
    ) {
        Constants.Set<Flags.EditValue> typedFlags = Constants.Set.fromInt(flags);
        return editValue(parentWin, fieldIndex, unitIndex, FieldType.fromInt(fieldType), fieldValue, maxlen, typedFlags, langidentifier).intValue();
    }

    @Override
    public void contentSendStateInformation(int state, String path) {
        sendStateInformation(State.fromInt(state), path);
    }
}