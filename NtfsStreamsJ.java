import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.*;

import java.util.*;
import java.util.regex.*;

import plugins.wdx.WDXPluginAdapter;
import plugins.wdx.FieldValue;
import static plugins.wdx.FieldValue.*;

/* Mark Russinovich's <a href="http://technet.microsoft.com/de-de/sysinternals/bb897440">streams.exe</a> v1.56
 * <p>
 * Frank Heyne's <a href="http://www.heysoft.de/en/software/lads.php?lang=EN">LADS.exe</a> v4.10
 * <p>
 * since Windows Vista: dir /r 
 */
public class NtfsStreamsJ extends WDXPluginAdapter {

    private Log log = LogFactory.getLog(NtfsStreamsJ.class);

    private static class AlternateDataStream {
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

    public static final int CP_HASH        = 0x23;  // "#".codePointAt(0);
    public static final int CP_ASTERISK    = 0x2A;  // "*".codePointAt(0);
    public static final int CP_QUESTION    = 0x3F;  // "?".codePointAt(0);
    public static final int CP_BAR         = 0x7C;  // "|".codePointAt(0);
    public static final int CP_GT          = 0x3E;  // ">".codePointAt(0);
    public static final int CP_LT          = 0x3C;  // "<".codePointAt(0);
    public static final int CP_SLASH       = 0x2F;  // "/".codePointAt(0);
    public static final int CP_BACKSLASH   = 0x5C;  // "\\".codePointAt(0);


    private String streams_exe = "c:\\Programme\\totalcmd\\plugins\\wdx\\NtfsStreamsJ\\streams.exe";
    private String lads_exe = "c:\\Programme\\totalcmd\\plugins\\NtfsStreamsJ\\lads.exe";

    private final Matcher streamsOutMatcher = Pattern.compile("^\\s+:([^:]*):\\$DATA\\t(\\d+)$").matcher("");
    private final Matcher ladsOutMatcher = Pattern.compile("^\\s(\\d+)\\s+(.+):([^:]*)$").matcher("");

    public NtfsStreamsJ() {
        log.debug(NtfsStreamsJ.class.getName());
    }

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
    
    private LineNumberReader getRawHelperOutput(String helperExeName, String fileName) throws IOException, InterruptedException {
        Process p = Runtime.getRuntime().exec(new String[]{helperExeName, fileName});
        LineNumberReader stdoutReader = new LineNumberReader(new InputStreamReader(p.getInputStream()));
        p.waitFor();
        return stdoutReader;
    }
    
    public List<AlternateDataStream> getStreamsWithHelper(final String helperExeName, final String fileName) throws IOException, InterruptedException {
        List<AlternateDataStream> result = new ArrayList<AlternateDataStream>();
        if (helperExeName.equals(streams_exe)) {
            try {
                LineNumberReader stdoutReader = getRawHelperOutput(helperExeName, fileName);
                String line;
                Matcher m = this.streamsOutMatcher;
                while ((line = stdoutReader.readLine()) != null) {
                    m.reset(line);
                    if (m.matches()) {
                        AlternateDataStream s = new AlternateDataStream(fileName, m.group(1), Integer.parseInt(m.group(2), 10));
                        result.add(s);
                        log.debug(s.getName() + ":" + s.length());
                    }
                }
                stdoutReader.close();
            } catch (Exception e) {
                log.error(e);
                throw e;
            }
        } else {
            throw new RuntimeException("NYI: " + helperExeName);
        }
        return result;
    }

    public int contentGetSupportedField(int fieldIndex,
                                         StringBuffer fieldName,
                                         StringBuffer units,
                                         int maxlen)
        {
        switch (fieldIndex) {
            case 0:
                fieldName.append("streamCount");
                return FT_NUMERIC_32;
        }
        return FT_NOMOREFIELDS;
    }

    public int contentGetValue(String fileName,
                                int fieldIndex,
                                int unitIndex,
                                FieldValue fieldValue,
                                int maxlen,
                                int flags)
        {
        log.debug("contentGetValue('" + fileName + "', " + fieldIndex + ",...)");
        try {
            List<AlternateDataStream> streams = getStreamsWithHelper(streams_exe, fileName);
            switch (fieldIndex) {
                case 0:
                    fieldValue.setValue(FT_NUMERIC_32, streams.size());
                    return FT_NUMERIC_32;
            }
        } catch (IOException e) {
            log.error(e);
            return FT_FILEERROR;
        } catch (InterruptedException e) {
            log.error(e);
            return FT_FILEERROR;
        }
        return FT_NOSUCHFIELD;
    }

}