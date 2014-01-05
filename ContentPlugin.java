import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;

import java.util.*;

import plugins.wdx.WDXPluginAdapter;
import plugins.wdx.FieldValue;
import static plugins.wdx.FieldValue.*;

public abstract class ContentPlugin extends WDXPluginAdapter {

    static abstract class Field<T> {

        static abstract class STRING extends Field<String> {
            protected STRING(String name) {
                super(name, FT_STRING, String.class);
            }
            final String _getValue(String fileName) throws IOException, InterruptedException {
                return getValue(fileName);
            }
            public abstract String getValue(String fileName) throws IOException, InterruptedException;
        }

        static abstract class INT extends Field<Integer> {
            protected INT(String name) {
                super(name, FT_NUMERIC_32, Integer.class);
            }
            final Integer _getValue(String fileName) throws IOException, InterruptedException {
                return getValue(fileName);
            }
            public abstract int getValue(String fileName) throws IOException, InterruptedException;
        }

        public final String name;
        public final int type;
        public final Class<T> javaType;

        private Field(String name, int type, Class<T> javaType) {
            // TODO: check validity of field name
            this.name = name;
            this.type = type;
            this.javaType = javaType;
        }

        abstract T _getValue(String fileName) throws IOException, InterruptedException;

        public String toString() {
            return javaType.getName().replace("java.lang.", "") + " " + name;
        }
    }

    private final Log myLog;
    protected final Log log;

    protected ContentPlugin() {
        this.myLog = LogFactory.getLog(ContentPlugin.class);
        this.log = LogFactory.getLog(this.getClass());
        initFields();
    }
    
    protected abstract void initFields();

    private Map<String, Field<?>> namesToFields = new HashMap<>();
    private List<Field<?>> fields = new ArrayList<>();

    protected final <T, F extends Field<T>> void define(F field) {
        if (namesToFields.containsKey(field.name)) {
            throw new RuntimeException("duplicate field name " + field.name);
        }
        namesToFields.put(field.name, field);
        fields.add(field);
    }
    
    public Iterable<Field<?>> fields() {
        return this.fields;
    }

    public void listFields() {
        listFields(System.out);
    }

    public void listFields(PrintStream out) {
        int n = 0;
        for (Field<?> f: fields()) {
            out.println(n++ + "\t" + f);
        }
    }

    public final int contentGetSupportedField(int fieldIndex,
                                                StringBuffer fieldName,
                                                StringBuffer units,
                                                int maxlen)
        {
        myLog.debug("contentGetSupportedField(" + fieldIndex + ",...," + maxlen + ")");
        if (fieldIndex >= fields.size()) {
            return FT_NOMOREFIELDS;
        }
        Field<?> field = fields.get(fieldIndex);
        fieldName.append(field.name);
        return field.type;
    }

    public final int contentGetValue(String fileName,
                                int fieldIndex,
                                int unitIndex,
                                FieldValue fieldValue,
                                int maxlen,
                                int flags)
        {
        myLog.debug("contentGetValue('" + fileName + "', " + fieldIndex + ",...," + maxlen + ", " + flags + ")");
        if (fieldIndex >= fields.size()) {
            return FT_NOSUCHFIELD;
        }
        Field<?> field = fields.get(fieldIndex);
        try {
            Object value = field._getValue(fileName);
            log.debug(field.name + "=" + value);
            fieldValue.setValue(field.type, value);
            return field.type;
        } catch (IOException e) {
            log.error(e);
            return FT_FILEERROR;
        } catch (InterruptedException e) {
            log.error(e);
            return FT_FILEERROR;
        }
    }

}