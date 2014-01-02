
public class VarExpr<T> extends Expr0<T> {

    private static int instanceCount = 0;
    
    private String name;
    private Class<T> type;
    
    public VarExpr() {
        this.name = "x" + instanceCount;
        instanceCount++;
    }

    public StmtExpr0 declare(final Class<? super T> clazz) {
        final String clazzName = clazz.getName().startsWith("java.lang.") ? clazz.getName().substring(10) : clazz.getName();
        return new StmtExpr0() {
            public String toSrc() {
                return clazzName + " " + VarExpr.this.name + ";\n";
            }
        };
    }

    public StmtExpr0 assign(Expr0<? extends T> value) {
        return StmtExpr.Assignment.create(this).bind1(value);
    }
    
    public StmtExpr0 assign(T value) {
        return StmtExpr.Assignment.create(this).bind1(new ConstExpr<>(value));
    }

    public String toSrc() {
        return this.name;
    }
    
    public String toString() {
        return toSrc();
    }

}
