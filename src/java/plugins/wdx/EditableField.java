package plugins.wdx;


import java.io.IOException;

import plugins.*;
import static plugins.wdx.FieldValue.*;


/**
  * @author Matthias Kling (meisl)
  */
public abstract class EditableField<T> extends Field<T> {

    public static abstract class STRING extends EditableField<String> {
        protected STRING(String name) {
            super(name, FT_STRING, String.class);
        }
        final void _setValue(String fileName, FieldValue fieldValue) throws IOException {
            setValue(fileName, fieldValue.getStr());
        }
    }

    public static abstract class INT extends EditableField<Integer> {
        protected INT(String name) {
            super(name, FT_NUMERIC_32, Integer.class);
        }
        final void _setValue(String fileName, FieldValue fieldValue) throws IOException {
            setValue(fileName, fieldValue.getIntValue());
        }
        }


    /* ----- EditableField: non-static members ----------------------------- */


    protected EditableField(String name, int type, Class<T> javaType) {
        super(name, type, javaType);
    }

    abstract void _setValue(String fileName, FieldValue value) throws IOException;
    
    public abstract void setValue(String fileName, T value) throws IOException;

}
