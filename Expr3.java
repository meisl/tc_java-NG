
public abstract class Expr3<TVar1, TVar2, TVar3, T> {

    public abstract Expr2<TVar2, TVar3, T> bind1(Expr0<TVar1> x1);
    
    public abstract Expr2<TVar1, TVar3, T> bind2(Expr0<TVar2> x2);

    public abstract Expr2<TVar1, TVar2, T> bind3(Expr0<TVar3> x3);

    public final Expr0<T> bind(Expr0<TVar1> x1, Expr0<TVar2> x2, Expr0<TVar3> x3) {
        return bind1(x1).bind1(x2).bind1(x3);
    }

}
