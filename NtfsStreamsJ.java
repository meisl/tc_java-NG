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

    public static enum Helper {
        STREAMS(
            "c:\\Programme\\totalcmd\\plugins\\wdx\\NtfsStreamsJ\\streams.exe",
            //"^\\s+:([^:]*):\\$DATA\\t(\\d+)$"
            "^\\s+:([^:]*):\\$DATA\\t(\\d+)|(([a-zA-z]:\\\\)?([^:?*|<>/]+\\\\)*([^:?*|<>/\\\\]+)):$"
        ),
        LADS(
            "c:\\Programme\\totalcmd\\plugins\\wdx\\NtfsStreamsJ\\lads.exe",
            "^\\s*(\\d+)\\s+(.+?)\\\\?:([^:]*)$"
        );
        
        public final String exeName;
        public final Matcher outputLineMatcher;
        Helper(String exeName, String pattern) {
            this.exeName = exeName;
            this.outputLineMatcher = Pattern.compile(pattern).matcher("");
        }
    }

    private Log log = LogFactory.getLog(NtfsStreamsJ.class);
    private final Helper helper;
    
    public NtfsStreamsJ() {
        this(Helper.STREAMS);
    }
    
    public NtfsStreamsJ(Helper helper) {
        log.debug(NtfsStreamsJ.class.getName() + "(" + helper + ")");
        this.helper = helper;
    }

    private LineNumberReader getRawHelperOutput(String fileName) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(this.helper.exeName, fileName);
        pb.redirectErrorStream(true);
        Process p = pb.start();
        // ATTENTION: don't do p.waitFor() - it'd block forever if helper produces more output than the output stream's buffer can hold at once...
        LineNumberReader stdoutReader = new LineNumberReader(new InputStreamReader(p.getInputStream()));
        return stdoutReader;
    }
    
    private Iterable<MatchResult> matchingLines(final LineNumberReader r, final Matcher m) {
        return new Iterable<MatchResult>() {
            private boolean canGetIterator = true;
            public Iterator<MatchResult> iterator() {
                if (canGetIterator) {
                    canGetIterator = false;
                    final SeqIterator<String> rawLines = new SeqIteratorAdapter<String>() {
                        protected String seekNext() {
                            try {
                                return r.readLine();
                            } catch (IOException e) {
                                log.error(e);
                                throw new RuntimeException(e);
                            }
                        }
                    };
                    final SeqIterator<MatchResult> matches = rawLines.map(new Func1<String, MatchResult>() {
                        public MatchResult apply(String s) {
                            if (m.reset(s).matches()) {
                                return m.toMatchResult();
                            } else {
                                return null;
                            }
                        }
                    }).filter(new Func1<MatchResult, Boolean>() {
                        public Boolean apply(MatchResult match) {
                            return match != null;
                        }
                    });
                    return matches;
                } else {
                    throw new IllegalStateException("can iterate only once");
                }
            }
        };
    }
    
    private static abstract class StreamListDesc implements Iterator<MatchResult> {
        public final String fileName;
        public final File file;
        
        private MatchResult nextOut;
        private boolean nextValid;
        
        public StreamListDesc(String fileName, MatchResult firstResult, boolean firstValid) {
            this.fileName = fileName;
            this.file = new File(fileName);
            this.nextOut = firstResult;
            this.nextValid = firstValid;
        }
        
        protected abstract MatchResult seekNext();

        public boolean hasNext() {
            if (!nextValid) {
                nextValid = true;
                nextOut = seekNext();
            }
            return nextOut != null;
        }
        
        public MatchResult next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            nextValid = false;
            return nextOut;
        }
        
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public <TKey> Iterator<StreamListDesc> groupBy(final Iterator<MatchResult> rawMatchesIt, final Func2<MatchResult, TKey, TKey> keyOf) {
        return new Iterator<StreamListDesc>() {
            private StreamListDesc nextOut = null;
            private TKey lastKey = null;
            private MatchResult pendingMatch = null;
            public boolean hasNext() {
                if (nextOut != null) {
                    return true;
                }
                MatchResult match = pendingMatch;
                if (match == null) {
                    if (rawMatchesIt.hasNext()) {
                        match = rawMatchesIt.next();
                    }
                } else {
                    pendingMatch = null;
                }
                while (match != null) {
                    TKey key = keyOf.apply(match, lastKey);
                    if (key.equals(lastKey)) {
                        // TODO append match to last StreamListDesc
                    } else {
                        lastKey = key;
                        nextOut = new StreamListDesc((String)key, match, true) {
                            protected MatchResult seekNext() {
                                if (!rawMatchesIt.hasNext()) { 
                                    return null;
                                }
                                MatchResult match = rawMatchesIt.next();
                                TKey key = keyOf.apply(match, (TKey)this.fileName);
                                if (((String)key).equals(this.fileName)) {
                                    return match;
                                }
                                pendingMatch = match;
                                return null;
                            }
                        };
                        return true;
                    }
                    if (rawMatchesIt.hasNext()) {
                        match = rawMatchesIt.next();
                    }
                }
                nextOut = null;
                return false;
            }
            public StreamListDesc next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                StreamListDesc out = nextOut;
                nextOut = null;
                return out;
            }
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
    
    public List<AlternateDataStream> getStreams(String fileName) throws IOException, InterruptedException {
        File file = new File(fileName).getCanonicalFile();
        fileName = file.getPath();
        
        final int streamNameIdx, streamLengthIdx, fileNameIdx;
        LineNumberReader stdoutReader;
        Iterator<StreamListDesc> lists;
        switch (this.helper) {
            case STREAMS:
                streamNameIdx = 1;
                streamLengthIdx = 2;
                fileNameIdx = 3;
                stdoutReader = getRawHelperOutput(file.getPath());
                
                lists = groupBy(
                    matchingLines(stdoutReader, this.helper.outputLineMatcher).iterator(), 
                    new Func2<MatchResult, String, String>() { public String apply(MatchResult m, String lastKey) {
                        String fileName = m.group(fileNameIdx);
                        return fileName != null ? fileName : lastKey;
                    } }
                );
                while (lists.hasNext()) {
                    StreamListDesc list = lists.next();
                    //if (list.file.equals(file)) {
                        System.out.println(list.fileName);
                        while (list.hasNext()) {
                            System.out.println("   " + list.next().group(0));
                        }
                    //}
                }
                return null;
                
                //break;
            case LADS:
                streamNameIdx = 3;
                streamLengthIdx = 1;
                fileNameIdx = 2;
                stdoutReader = getRawHelperOutput(file.getParent());
                
                lists = groupBy(
                    matchingLines(stdoutReader, this.helper.outputLineMatcher).iterator(), 
                    new Func2<MatchResult, String, String>() { public String apply(MatchResult m, String lastKey) { return m.group(fileNameIdx); } }
                );
                while (lists.hasNext()) {
                    StreamListDesc list = lists.next();
                    //if (list.file.equals(file)) {
                        System.out.println(list.fileName);
                        while (list.hasNext()) {
                            System.out.println("   " + list.next().group(0));
                        }
                    //}
                }
                return null;
                //break;
            default:
                throw new RuntimeException("NYI: " + this.helper);
        }
        /*
        List<AlternateDataStream> result = new ArrayList<AlternateDataStream>();
        try {
            String streamName;
            int streamLength;
            Iterator<MatchResult> it = matchingLines(stdoutReader, this.helper.outputLineMatcher).iterator();
            while (it.hasNext()) {
                MatchResult match = it.next();
                    streamName = match.group(streamNameIdx);
                    System.out.println(match.group(fileNameIdx));
                    if (match.group(fileNameIdx) == null) {
                        streamLength = Integer.parseInt(match.group(streamLengthIdx), 10);
                        AlternateDataStream s = new AlternateDataStream(fileName, streamName, streamLength);
                        result.add(s);
                        log.debug(s);
                    }
            }
            stdoutReader.close();
        } catch (Exception e) {
            log.error(e);
            throw e;
        }
        return result;
        */
    }

    public int contentGetSupportedField(int fieldIndex,
                                         StringBuffer fieldName,
                                         StringBuffer units,
                                         int maxlen)
        {
        switch (fieldIndex) {
            case 0:
                fieldName.append("count");
                return FT_NUMERIC_32;
            case 1:
                fieldName.append("summary");
                return FT_STRING;
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
            List<AlternateDataStream> streams = getStreams(fileName);
            switch (fieldIndex) {
                case 0:
                    fieldValue.setValue(FT_NUMERIC_32, streams.size());
                    log.debug("count=" + streams.size());
                    return FT_NUMERIC_32;
                case 1:
                    int n = streams.size();
                    if (n == 0) {
                        fieldValue.setValue(FT_STRING, "");
                    } else {
                        StringBuilder result = new StringBuilder(n + " ADSs:");
                        for (AlternateDataStream ads: streams) {
                            result.append(System.lineSeparator()).append(ads);
                        }
                        result.append(System.lineSeparator());
                        fieldValue.setValue(FT_STRING, result.toString());
                }
                return FT_STRING;
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