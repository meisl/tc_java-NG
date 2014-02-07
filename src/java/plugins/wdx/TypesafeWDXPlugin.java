package plugins.wdx;


public interface TypesafeWDXPlugin {

    public RV.GetSupportedField getSupportedField(
        int fieldIndex,
        StringBuffer fieldName,
        StringBuffer units,
        int maxlen
    );
    
    public RV.GetSupportedFieldFlags getSupportedFieldFlags(int fieldIndex);

    public RV.GetValueCanDelay getValueCanDelay(
        String fileName,
        int fieldIndex,
        int unitIndex,
        FieldValue fieldValue,
        int maxlen,
        Double size
    );

    public RV.GetValueImmediate getValueImmediate(
        String fileName,
        int fieldIndex,
        int unitIndex,
        FieldValue fieldValue,
        int maxlen,
        Double size
    );

    public RV.SetValue setValue(
        String fileName,
        int fieldIndex,
        int unitIndex,
        FieldType fieldType,
        FieldValue fieldValue,
        Constants.Set<Flags.SetValue> flags
    );

    public RV.EditValue editValue(int parentWin,
        int fieldIndex,
        int unitIndex,
        FieldType fieldType,
        FieldValue fieldValue,
        int maxlen,
        Constants.Set<Flags.EditValue> flags,
        String langidentifier
    );

    public void sendStateInformation(State state, String path);

}