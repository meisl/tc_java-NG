
public abstract class StmtExpr2<T1, T2> extends Expr2<T1, T2, Void> {

    public abstract StmtExpr1<T2> bind1(Expr0<T1> x1);

    public abstract StmtExpr1<T1> bind2(Expr0<T2> x2);

}
