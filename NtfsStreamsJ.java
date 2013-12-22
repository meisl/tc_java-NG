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

    public static final String streams_exe = "c:\\Programme\\totalcmd\\plugins\\wdx\\NtfsStreamsJ\\streams.exe";
    public static final String lads_exe = "c:\\Programme\\totalcmd\\plugins\\wdx\\NtfsStreamsJ\\lads.exe";

    private Log log = LogFactory.getLog(NtfsStreamsJ.class);

    private final Matcher streamsOutMatcher = Pattern.compile("^\\s+:([^:]*):\\$DATA\\t(\\d+)$").matcher("");
    private final Matcher ladsOutMatcher = Pattern.compile("^\\s*(\\d+)\\s+(.+):([^:]*)$").matcher("");

    public NtfsStreamsJ() {
        log.debug(NtfsStreamsJ.class.getName());
    }

    private LineNumberReader getRawHelperOutput(String helperExeName, String fileName) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(helperExeName, fileName);
        pb.redirectErrorStream(true);

        Process p = pb.start();
        LineNumberReader stdoutReader = new LineNumberReader(new InputStreamReader(p.getInputStream()));
        return stdoutReader;
    }
    
    private Iterable<String> matchingLines(final Matcher m, final LineNumberReader r) {
        return new Iterable<String>() {
            private boolean canGetIterator = true;
            private String nextLine = null;
            public Iterator<String> iterator() {
                if (canGetIterator) {
                    return new Iterator<String>() {
                        public boolean hasNext() {
                            try {
                                if (nextLine == null) {
                                    while ((nextLine = r.readLine()) != null) {
                                        m.reset(nextLine);
                                        if (m.matches()) {
                                            return true;
                                        }
                                    }
                                    nextLine = null;
                                }
                                return nextLine != null;
                            } catch (IOException e) {
                                log.error(e);
                                throw new RuntimeException(e);
                            }
                        }
                        public String next() {
                            if (hasNext()) {
                                String result = nextLine;
                                nextLine = null;
                                return result;
                            }
                            throw new NoSuchElementException();
                        }
                        public void remove() {
                            throw new UnsupportedOperationException();
                        }
                    };
                } else {
                    throw new IllegalStateException("can iterate only once");
                }
            }
        };
    }
    
    public List<AlternateDataStream> getStreamsWithHelper(final String helperExeName, String fileName) throws IOException, InterruptedException {
        File file = new File(fileName).getCanonicalFile();
        fileName = file.getPath();
        
        Matcher m;
        int streamNameIdx, streamLengthIdx, fileNameIdx;
        LineNumberReader stdoutReader;
        if (helperExeName.equals(streams_exe)) {
                m = this.streamsOutMatcher;
                streamNameIdx = 1;
                streamLengthIdx = 2;
                fileNameIdx = -1;
                stdoutReader = getRawHelperOutput(helperExeName, file.getPath());
        } else if (helperExeName.equals(lads_exe)) {
                m = this.ladsOutMatcher;
                streamNameIdx = 3;
                streamLengthIdx = 1;
                fileNameIdx = 2;
                stdoutReader = getRawHelperOutput(helperExeName, file.getParent());
        } else {
            throw new RuntimeException("NYI: " + helperExeName);
        }
        List<AlternateDataStream> result = new ArrayList<AlternateDataStream>();
        try {
            String streamName;
            int streamLength;
            for (String line: matchingLines(m, stdoutReader)) {
                System.out.println(">>" + line);
                if ((fileNameIdx < 0) || fileName.equals(m.group(fileNameIdx))) {
                    streamName = m.group(streamNameIdx);
                    streamLength = Integer.parseInt(m.group(streamLengthIdx), 10);
                    AlternateDataStream s = new AlternateDataStream(fileName, streamName, streamLength);
                    result.add(s);
                    log.debug(s.getName() + ":" + s.length());
                }
            }
            stdoutReader.close();
        } catch (Exception e) {
            log.error(e);
            throw e;
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