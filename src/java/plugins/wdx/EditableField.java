package plugins.wdx;


import java.io.IOException;

import static plugins.wdx.FieldValue.*;


public abstract class EditableField<T> extends Field<T> {

    public static abstract class STRING extends EditableField<String> {
        protected STRING(String name) {
            super(name, FT_STRING, String.class);
        }
        final String _getValue(String fileName) throws IOException {
            return getValue(fileName);
        }
        final void _setValue(String fileName, FieldValue fieldValue) throws IOException {
            setValue(fileName, fieldValue.getStr());
        }
        public abstract String getValue(String fileName) throws IOException;
        public abstract void setValue(String fileName, String value) throws IOException;
    }

    public static abstract class INT extends EditableField<Integer> {
        protected INT(String name) {
            super(name, FT_NUMERIC_32, Integer.class);
        }
        final Integer _getValue(String fileName) throws IOException {
            return getValue(fileName);
        }
        final void _setValue(String fileName, FieldValue fieldValue) throws IOException {
            setValue(fileName, fieldValue.getIntValue());
        }
        public abstract int getValue(String fileName) throws IOException;
        public final void setValue(String fileName, Integer value) throws IOException {
            setValue(fileName, (int)value);
        }
        public abstract void setValue(String fileName, int value) throws IOException;
    }

    protected EditableField(String name, int type, Class<T> javaType) {
        super(name, type, javaType);
    }

    abstract void _setValue(String fileName, FieldValue value) throws IOException;

    public abstract void setValue(String fileName, T value) throws IOException;

}
