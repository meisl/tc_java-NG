
public abstract class Fn2<TArg0, TArg1, TResult> extends Fn1<TArg0, Fn1<? extends TArg1, ? super TResult>> {

    public final Fn1<TArg1, TResult> apply(final TArg0 arg0) {
        return new Fn1<TArg1, TResult>() {
            public final TResult apply(final TArg1 arg1) {
                return Fn2.this.apply(arg0, arg1);
            }
            public String toString() {
                return "\\a.( (" + Fn2.this + ") " + arg0 + " a )";
            }
            //public Expr1<TArg1, TResult> toExpr1() {
            //    return Expr.fromFn(Fn2.this).bind1(new ConstExpr<>(arg0));
            //}
        };
    }
/*
    public Expr1<TArg1, TResult> toExpr1() {
        return null;
    }

    public abstract Expr2<TArg0, TArg1, TResult> toExpr2();
*/
    public abstract TResult apply(TArg0 arg0, TArg1 arg1);
}
