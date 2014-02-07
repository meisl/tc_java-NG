package plugins.wdx;


public abstract class AbstractContentPlugin extends WDXPluginAdapter implements TypesafeWDXPlugin {


    /**
     * {@inheritDoc}
     */
    @Override
    public String contentGetDetectString(int maxlen) {
        return getDetectString(maxlen);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int contentGetSupportedField(int fieldIndex,
                                              StringBuffer fieldName,
                                              StringBuffer units,
                                              int maxlen
    ) {
        return getSupportedField(fieldIndex, fieldName, units, maxlen).intValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int contentGetDefaultSortOrder(int fieldIndex) {
        return getDefaultSortOrder(fieldIndex).intValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int contentGetSupportedFieldFlags(int fieldIndex) {
        return getSupportedFieldFlags(fieldIndex).intValue();
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void contentStopGetValue(
        String fileName
    ) {
        stopGetValue(fileName);
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void contentSendStateInformation(
        int state,
        String path
    ) {
        sendStateInformation(State.fromInt(state), path);
    }

}