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
            "^\\s+:([^:]*):\\$DATA\\t(\\d+)|(([a-zA-z]:\\\\)?([^:?*|<>/]+\\\\)*([^:?*|<>/\\\\]+)):$",
            3, 1, 2
        ),
        LADS(
            "c:\\Programme\\totalcmd\\plugins\\wdx\\NtfsStreamsJ\\lads.exe",
            "^\\s*(\\d+)\\s+(.+?)\\\\?:([^:]*)$",
            2, 3, 1
        );
        
        public final String exeName;
        public final Pattern outputLinePattern;
        public final Fn1<Tuple3<String,String,String>, Tuple3<String,String,String>> fn_permute;

        
        Helper(String exeName, String pattern, int fileNameIdx, int streamNameIdx, int streamSizeIdx) {
            this.exeName = exeName;
            this.outputLinePattern = Pattern.compile(pattern);
            fn_permute = Tuple3.fn_permute(fileNameIdx - 1, streamNameIdx - 1, streamSizeIdx - 1); // -1 because group(0) is discarded
        }

        public SeqIterator<String> rawOutput(String fileName) throws IOException {
            ProcessBuilder pb = new ProcessBuilder(this.exeName, fileName);
            pb.redirectErrorStream(true);
            Process p = pb.start();
            // ATTENTION: don't do p.waitFor() - it'd block forever if helper produces more output than the output stream's buffer can hold at once...
            final LineNumberReader r = new LineNumberReader(new InputStreamReader(p.getInputStream()));
            return new SeqIteratorAdapter<String>("linesFrom(" + pb.command() + ")") {
                protected String seekNext() {
                    try {
                        String line = r.readLine();
                        if (line != null) {
                            return line;
                        }
                        r.close();
                        return endOfSeq();
                    } catch (IOException e) {
                        //log.error(e);     // TODO: log(IOException) in rawOutput iterator
                        throw new RuntimeException(e);
                    }
                }
            };
        }

        public SeqIterator<Tuple3<String, String, String>> matchingLines(String fileName) throws IOException {
            return rawOutput(fileName)
                .map(Func.regexMatch(this.outputLinePattern))
                .filter(Predicate.notNull())
                .map(Func.toTuple3())
                .map(this.fn_permute)
            ;
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

    public <TKey> SeqIterator<SectionIterator<TKey, Tuple3<String,String,String>>> groupBy(
        final Iterator<Tuple3<String,String,String>> rawMatchesIt, 
        final Fn2<Tuple3<String,String,String>, TKey, TKey> keyOf
    ) {
        return new SeqIteratorAdapter<SectionIterator<TKey, Tuple3<String,String,String>>>() {
            private TKey lastKey = null;
            private SectionIterator<TKey, Tuple3<String,String,String>> lastOut = null;
            private Tuple3<String,String,String> pendingMatch = null;

            public SectionIterator<TKey, Tuple3<String,String,String>> seekNext() {
                System.out.println(">>>seekNext");
                Tuple3<String,String,String> match = pendingMatch;
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
                        previous.append(match);
                    } else {
                        lastKey = key;
                        return new SectionIteratorAdapter<TKey, Tuple3<String,String,String>>(key, match, (match.item1 != null)) {
                            protected Tuple3<String,String,String> seekNext(TKey currentKey) {
                                if (!rawMatchesIt.hasNext()) {
                                    return endOfSeq();
                                }
                                Tuple3<String,String,String> match = rawMatchesIt.next();
                                TKey key = keyOf.apply(match, currentKey);
                                if (key.equals(currentKey)) {
                                    return match;
                                }
                                pendingMatch = match;
                                return endOfSeq();
                            }
                        };
                    }
                    match = rawMatchesIt.hasNext() ? rawMatchesIt.next() : null;
                }
                return endOfSeq();
            }
        };
    }
    
    public List<AlternateDataStream> getStreams(String fileName) throws IOException, InterruptedException {
        File file = new File(fileName).getCanonicalFile();
        fileName = file.getPath();
        
        final int streamNameIdx, streamLengthIdx, fileNameIdx;
        final Iterator<Tuple3<String, String, String>> matchingLines;
        Iterable<SectionIterator<String, Tuple3<String,String,String>>> lists;
        switch (this.helper) {
            case STREAMS:
                matchingLines = this.helper.matchingLines(file.getPath());
                break;
            case LADS:
                matchingLines = this.helper.matchingLines(file.getParent());
                break;
            default:
                throw new RuntimeException("NYI: " + this.helper);
        }
        System.out.println(matchingLines);
        /*
        Func2<MatchResult, String, String> groupingFn = new Func2<MatchResult, String, String>() { public String apply(MatchResult m, String lastKey) {
            String fileName = m.group(fileNameIdx);
            return fileName != null ? fileName : lastKey;
        } };
        */
        Tuple2<String, String> t = Tuple.create("a", "b");
        Tuple1<String> t1 = t;
        Fn1<Tuple2<String, ?>, String> proj0 = Tuple2.fn_project0();
        Fn1<Tuple2<?, String>, String> proj1 = Tuple2.fn_project1();
        System.out.println(proj0.apply(t));

        Fn2<Tuple3<String, String, String>, String, String> groupingFn = new Fn2<Tuple3<String, String, String>, String, String>() {
            public String apply(Tuple3<String, String, String> t, String lastKey) {
                System.out.println(">>> " + t + ", " + lastKey);
                String fileName = t.item0;  //(fileNameIdx == 3) ? t.group(fileNameIdx);
                return fileName != null ? fileName : lastKey;
            }
        };
        
        lists = groupBy(matchingLines, groupingFn).toList();
        for (SectionIterator<String, Tuple3<String,String,String>> list: lists) {
            //if (list.file.equals(file)) {
                System.out.println(list.key() + "   " + list);
                while (list.hasNext()) {
                    System.out.println("   " + list.next());
                }
            //}
        }
        return null;

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