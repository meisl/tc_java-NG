import java.util.regex.*;

public abstract class Func {

    public static final Func1<String, MatchResult> regexMatch(final Pattern p) {
        final Matcher m = p.matcher("");
        return new Func1<String, MatchResult>() {
            public MatchResult apply(String s) {
                return m.reset(s).matches() ? m.toMatchResult() : null;
            }
            public String toString() {
                return "/" + p + "/.match";
            }
        };
    }
    
    public static final Func1<MatchResult, Tuple3<String, String, String>> toTuple3() {
        return new Func1<MatchResult, Tuple3<String, String, String>>() {
            public Tuple3 apply(MatchResult m) {
                return new Tuple3(m.group(0), m.group(1), m.group(2));
            }
            public String toString() {
                return "\\m.(<m.group(0), m.group(1), m.group(2)>)";
            }
        };
        
    }
}
