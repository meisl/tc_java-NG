package plugins.wdx;


import java.io.IOException;

import static plugins.wdx.FieldValue.*;

/**
  * @author Matthias Kling (meisl)
  */
public abstract class Field<T> {

    /** Found that (my) TotalCommander on (my) WinXP always passes 259 as
      * the max name length including the trailing 0 to
      * contentGetSupportedField(..) so this is the bound to be checked in
      * runTests().
      * <p>
      * Note that another check against the actual value passed to
      * contentGetSupportedField is performed on invocation of that method.
      */
    public static final int MAX_NAME_LENGTH = 258;

    public static abstract class STRING extends Field<String> {
        protected STRING(String name) {
            super(name, FT_STRING, String.class);
        }
    }

    public static abstract class INTEGER extends Field<Integer> {
        protected INTEGER(String name) {
            super(name, FT_NUMERIC_32, Integer.class);
        }
    }


    /* ----- Field: non-static members ------------------------------------- */


    public final String name;
    public final int type;
    public final Class<T> javaType;

    protected Field(String name, int type, Class<T> javaType) {
        this.name = name;
        this.type = type;
        this.javaType = javaType;
        assertValidName();
    }

    public abstract T getValue(String fileName) throws IOException;

    public final boolean isEditable() {
        return this instanceof EditableField;
    }
    
    public boolean isDelayInOrder(String fileName) throws IOException {
        return false;
    }

    public String getJavaTypeName() {
        return javaType.getName().replace("java.lang.", "");
    }

    public String toString() {
        return getJavaTypeName() + " " + name;
    }
    
    final void assertValidNameLength() {
        assertValidNameLength(MAX_NAME_LENGTH);
    }

    final void assertValidNameLength(int maxlen) {
        if (this.name.length() > maxlen)
            throw new IllegalArgumentException("field name too long (" + name.length() + " > " + maxlen + "): \"" + name + "\"");
    }
    
    final void assertValidName() {
        if (name == null) {
            throw new IllegalArgumentException("field name must not be null!");
        }
        assertValidNameLength();
        if (name.contains(".") || name.contains("|") || name.contains(":")) {
            throw new IllegalArgumentException("field name must not contain '.', '|' or ':': \"" + name + "\"");
        }
    }
}
