public class AlternateDataStream {

    public static final int CP_HASH        = 0x23;  // "#".codePointAt(0);
    public static final int CP_ASTERISK    = 0x2A;  // "*".codePointAt(0);
    public static final int CP_QUESTION    = 0x3F;  // "?".codePointAt(0);
    public static final int CP_BAR         = 0x7C;  // "|".codePointAt(0);
    public static final int CP_GT          = 0x3E;  // ">".codePointAt(0);
    public static final int CP_LT          = 0x3C;  // "<".codePointAt(0);
    public static final int CP_SLASH       = 0x2F;  // "/".codePointAt(0);
    public static final int CP_BACKSLASH   = 0x5C;  // "\\".codePointAt(0);

    private static StringBuilder appendEscaped(StringBuilder sb, int codePoint) {
        String hs = Integer.toHexString(codePoint);
        sb.append('#');
        for (int i = hs.length(); i < 4; i++) {
            sb.append('0');
        }
        sb.append(hs);
        return sb;
    }
    
    /* From <a href="http://stackoverflow.com/questions/6198986/how-can-i-replace-non-printable-unicode-characters-in-java">http://stackoverflow.com/questions/6198986/how-can-i-replace-non-printable-unicode-characters-in-java</a>
     */
    private static String fix(String s) {
        StringBuilder sb = new StringBuilder(s.length());
        for (int offset = 0; offset < s.length();) {
            int codePoint = s.codePointAt(offset);
            offset += Character.charCount(codePoint);
            
            switch (codePoint) {
                case CP_HASH:
                    sb.append("##");
                    break;
                case CP_QUESTION:
                case CP_ASTERISK:
                case CP_BAR:
                case CP_GT:
                case CP_LT:
                case CP_SLASH:
                case CP_BACKSLASH:
                    appendEscaped(sb, codePoint);
                    break;
                default:
                    // Replace invisible control characters and unused code points
                    switch (Character.getType(codePoint)) {
                        case Character.CONTROL:     // \p{Cc}
                        case Character.FORMAT:      // \p{Cf}
                        case Character.PRIVATE_USE: // \p{Co}
                        case Character.SURROGATE:   // \p{Cs}
                        case Character.UNASSIGNED:  // \p{Cn}
                            appendEscaped(sb, codePoint);
                            break;
                        default:
                            sb.append(Character.toChars(codePoint));
                            break;
                    }
            }
        }
        return sb.toString();
    }
    private final String fileName;
    private final String rawName;
    private final int length;
    
    private String name;
    
    public AlternateDataStream(String fileName, String rawName, int length) {
        this.fileName = fileName;
        this.rawName = rawName;
        this.length = length;
    }
    
    public int length() {
        return this.length;
    }
    
    public String getRawName() {
        return this.rawName;
    }
    
    public String getName() {
        if (this.name == null) {
            this.name = fix(this.rawName);
        }
        return this.name;
    }

}

