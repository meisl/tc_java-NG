
public abstract class Expr {

    public static <T> ConstExpr<T> fromFn(Fn0<T> f) {
        return new ConstExpr<T>(f.apply());
    }
/*
    public static <TVar1, T> Expr1<TVar1, T> fromFn(Fn1<TVar1, T> f) {
        return f.toExpr1();
    }

    public static <TVar1, TVar2, T> Expr2<TVar1, TVar2, T> fromFn(Fn2<TVar1, TVar2, T> f) {
        return f.toExpr2();
    }

    public static <TVar1, TVar2, TVar3, T> Expr3<TVar1, TVar2, TVar3, T> fromFn(Fn3<TVar1, TVar2, TVar3, T> f) {
        return f.toExpr3();
    }
*/
}
