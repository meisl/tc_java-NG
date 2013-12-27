
public abstract class Func3<TArg0, TArg1, TArg2, TResult> extends Func1<TArg0, Func2<TArg1, TArg2, TResult>> {

    public final Func2<TArg1, TArg2, TResult> apply(final TArg0 arg0) {
        return new Func2<TArg1, TArg2, TResult>() {
            public final TResult apply(final TArg1 arg1, final TArg2 arg2) {
                return apply(arg0, arg1, arg2);
            }
            public String toString() {
                return "\\b c.( (" + Func3.this + ") " + arg0 + " b c )";
            }

        };
    }

    public abstract TResult apply(TArg0 arg0, TArg1 arg1, TArg2 arg2);
}
