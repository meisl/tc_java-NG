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
}
