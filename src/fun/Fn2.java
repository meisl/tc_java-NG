package fun;

public abstract class Fn2<TArg0, TArg1, TResult> extends Fn1<TArg0, Fn1<? extends TArg1, ? super TResult>> {

    public final Fn1<TArg1, TResult> apply(final TArg0 arg0) {
        return new Fn1<TArg1, TResult>() {
            public final TResult apply(final TArg1 arg1) {
                return Fn2.this.apply(arg0, arg1);
            }
            public String toString() {
                return "\\a.( (" + Fn2.this + ") " + arg0 + " a )";
            }
        };
    }

    public <S> Fn2<S, TArg1, TResult> compose(final Fn1<? super S, ? extends TArg0> inner) {
        return new Fn2<S, TArg1, TResult>() {
            public TResult apply(S arg0, TArg1 arg1) {
                return Fn2.this.apply(inner.apply(arg0), arg1);
            }
            public String toString() {
                return "\\v w.( (" + Fn2.this + ") ((" + inner +  ") v) w )";
            }
        };
    }

    public abstract TResult apply(TArg0 arg0, TArg1 arg1);
}
