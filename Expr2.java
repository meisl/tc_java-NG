
public abstract class Expr2<TVar1, TVar2, T> {

    public abstract Expr1<TVar2, T> bind1(Expr0<TVar1> x1);
    
    public abstract Expr1<TVar1, T> bind2(Expr0<TVar2> x2);
    
    public final Expr0<T> bind(Expr0<TVar1> x1, Expr0<TVar2> x2) {
        return bind1(x1).bind1(x2);
    }
    
    public final <U> Expr2<U, TVar2, T> bind1(final Expr1<U, TVar1> x1) {
        final Expr2<TVar1, TVar2, T> self = this;
        return new Expr2<U, TVar2, T>() {
            public Expr1<TVar2, T> bind1(Expr0<U> y1) {
                return self.bind1(x1.bind1(y1));
            }
            public Expr1<U, T> bind2(Expr0<TVar2> y2) {
                return self.bind2(y2).bind1(x1);
            }
        };
    }
    
    public final <V> Expr2<TVar1, V, T> bind2(final Expr1<V, TVar2> x2) {
        final Expr2<TVar1, TVar2, T> self = this;
        return new Expr2<TVar1, V, T>() {
            public Expr1<V, T> bind1(Expr0<TVar1> y1) {
                return self.bind1(y1).bind1(x2);
            }
            public Expr1<TVar1, T> bind2(Expr0<V> y2) {
                return self.bind2(x2.bind1(y2));
            }
        };
    }

}
