
    interface X<T> {
        Expr1<T, Boolean> toExpr1();
    }

public abstract class Predicate<TArg> extends Fn1<TArg, Boolean> implements X<TArg> {
    
    public static <T> Predicate<T> notNull() {
        return new Predicate<T>() {
            public Boolean apply(T obj) {
                return obj != null;
            }
            public String toString() {
                VarExpr<T> v = new VarExpr<T>();
                return "\\" + v + "." + this.toExpr1().bind1(v).toSrc();
            }
            public Expr1<T, Boolean> toExpr1() {
                return new Expr2<T, T, Boolean>() {
                    public Expr1<T, Boolean> bind1(final Expr0<T> x1) {
                        return new Expr1<T, Boolean>() {
                            public Expr0<Boolean> bind1(final Expr0<? extends T> y1) {
                                return new Expr0<Boolean>() {
                                    public String toSrc() {
                                        return "(" + x1.toSrc() + " != " + y1.toSrc() + ")";
                                    }
                                };
                            }
                        };
                    }
                    public Expr1<T, Boolean> bind2(final Expr0<T> x2) {
                        return new Expr1<T, Boolean>() {
                            public Expr0<Boolean> bind1(final Expr0<? extends T> y1) {
                                return new Expr0<Boolean>() {
                                    public String toSrc() {
                                        return "(" + y1.toSrc() + " != " + x2.toSrc() + ")";
                                    }
                                };
                            }
                        };
                    }
                
                }.bind2(new ConstExpr<T>(null));
            }
        };
    }
}
