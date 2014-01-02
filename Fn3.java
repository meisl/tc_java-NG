
public abstract class Fn3<TArg0, TArg1, TArg2, TResult> extends Fn1<TArg0, Fn2<? extends TArg1, ? extends TArg2, ? super TResult>> {

    public final Fn2<TArg1, TArg2, TResult> apply(final TArg0 arg0) {
        return new Fn2<TArg1, TArg2, TResult>() {
            public final TResult apply(final TArg1 arg1, final TArg2 arg2) {
                return apply(arg0, arg1, arg2);
            }
            public String toString() {
                return "\\b c.( (" + Fn3.this + ") " + arg0 + " b c )";
            }
            //public Expr2<TArg1, TArg2, TResult> toExpr2() {
            //    return Expr.fromFn(Fn3.this).bind1(new ConstExpr<>(arg0));
            //}
        };
    }

    //public abstract Expr3<TArg0, TArg1, TArg2, TResult> toExpr3();

    public abstract TResult apply(TArg0 arg0, TArg1 arg1, TArg2 arg2);

}
