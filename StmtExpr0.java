
public abstract class StmtExpr0 extends Expr0<Void> {

    public StmtExpr0 concat(final StmtExpr0 x) {
        final StmtExpr0 self = this;
        return new StmtExpr0() {
            public String toSrc() {
                return self.toSrc() + x.toSrc();
            }
        };
    }

//    public abstract <T> StmtExpr1<T> concat(StmtExpr1<T> x);

}
