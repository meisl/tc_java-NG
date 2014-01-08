package fun;

public abstract class Fn1<TArg0, TResult> {

    public <S> Fn1<S, TResult> compose(
        final Fn1<? super S, ? extends TArg0> inner
    ) {
        final Fn1<TArg0, TResult> outer = this;
        return new Fn1<S, TResult>() {
            public TResult apply(S value) {
                return outer.apply(inner.apply(value));
            }
            public String toString() {
                return "\\v.( (" + outer + ") ((" + inner +  ") v) )";
            }
        };
    }


    public abstract TResult apply(TArg0 arg0);

}

