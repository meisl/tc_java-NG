
public abstract class Func2<TArg0, TArg1, TResult> extends Func1<TArg0, Func1<TArg1, TResult>> {

    public final Func1<TArg1, TResult> apply(final TArg0 arg0) {
        return new Func1<TArg1, TResult>() {
            public final TResult apply(final TArg1 arg1) {
                return apply(arg0, arg1);
            }
        };
    }

    public abstract TResult apply(TArg0 arg0, TArg1 arg1);
}
