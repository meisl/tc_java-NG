package plugins.wdx;


public interface TypesafeWDXPlugin {

    public String getDetectString(int maxlen);

    /**
     * Called to enumerate all supported fields.
     * {@code fieldIndex} is increased by 1 starting from 0
     * until the plugin returns {@link RV.GetSupportedField#NOMOREFIELDS NOMOREFIELDS}.
     * 
     * @param fieldIndex
     *            The index of the field for which TC requests information.
     *            Starting with 0, the FieldIndex is increased until the plugin
     *            returns an error.
     * 
     * @param fieldName
     *            Here the plugin has to return the name of the field with index
     *            FieldIndex. The field may not contain the following chars: .
     *            (dot) | (vertical line) : (colon). You may return a maximum of
     *            maxlen characters, including the trailing 0.
     * 
     * @param units
     *            When a field supports several units like bytes, kbytes, Mbytes
     *            etc, they need to be specified here in the following form:
     *            bytes|kbytes|Mbytes . The separator is the vertical dash "|"
     *            (Alt+0124). As field names, unit names may not contain a
     *            vertical dash, a dot, or a colon. You may return a maximum of
     *            maxlen characters, including the trailing 0. If the field type
     *            is {@link FieldType#MULTIPLECHOICE}, the plugin needs to return all possible
     *            values here. Example: The field "File Type" of the built-in
     *            content plugin can have the values "File", "Folder" and
     *            "Reparse point". The available choices need to be returned in
     *            the following form: File|Folder|Reparse point . The same
     *            separator is used as for Units. You may return a maximum of
     *            maxlen characters, including the trailing 0. The field type
     *            FT_MULTIPLECHOICE does NOT support any units.
     * @param maxlen
     *            The maximum number of characters, including the trailing 0,
     *            which may be returned in each of the fields.
     * 
     * @return the {@link FieldType} of the specified field or 
     *         {@link RV.GetSupportedField#NOMOREFIELDS NOMOREFIELDS}
     *         to indicate that there are no more fields
     */
     public RV.GetSupportedField getSupportedField(
        int fieldIndex,
        StringBuffer fieldName,
        StringBuffer units,
        int maxlen
    );

    public SortOrder getDefaultSortOrder(int fieldIndex);

    public RV.GetSupportedFieldFlags getSupportedFieldFlags(
        int fieldIndex
    );

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

    public void stopGetValue(
        String fileName
    );

    public RV.SetValue setValue(
        String fileName,
        int fieldIndex,
        int unitIndex,
        FieldType fieldType,
        FieldValue fieldValue,
        Constants.Set<Flags.SetValue> flags
    );

    public RV.EditValue editValue(
        int parentWin,
        int fieldIndex,
        int unitIndex,
        FieldType fieldType,
        FieldValue fieldValue,
        int maxlen,
        Constants.Set<Flags.EditValue> flags,
        String langidentifier
    );

    public void sendStateInformation(
        State state,
        String path
    );

}