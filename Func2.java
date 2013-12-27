
interface Fn2<TArg0, TArg1, TResult> extends Fn1<TArg0, Fn1<? extends TArg1, ? super TResult>> {
    TResult apply(TArg0 arg0, TArg1 arg1);
}

public abstract class Func2<TArg0, TArg1, TResult> implements Fn2<TArg0, TArg1, TResult> {//extends Func1<TArg0, Func1<TArg1, TResult>> {

    public final Fn1<TArg1, TResult> apply(final TArg0 arg0) {
        return new Fn1<TArg1, TResult>() {
            public final TResult apply(final TArg1 arg1) {
                return Func2.this.apply(arg0, arg1);
            }
            public String toString() {
                return "\\a.( (" + Func2.this + ") " + arg0 + " a )";
            }
        };
    }

    //public abstract TResult apply(TArg0 arg0, TArg1 arg1);
}
