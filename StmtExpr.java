
public interface StmtExpr {

    public static final class Return<T> extends StmtExpr1<T> {
    
        @SuppressWarnings("rawtypes")
        private static final Return instance = new Return();
        
        @SuppressWarnings("unchecked")
        public static <T> Return<T> create() {
            return instance;
        }

        public static <T> StmtExpr0 create(Expr0<T> x) {
            return Return.<T>create().bind1(x);
        }

        private Return() {
        }

        public StmtExpr0 bind1(final Expr0<? extends T> x1) {
            return new StmtExpr0 () {
                public String toSrc() {
                    return "return " + x1.toSrc() + ";\n";
                }
            };
        }

        public StmtExpr0 concat(StmtExpr0 x) {
            throw new UnsupportedOperationException("cannot append statements after return");
        }
    }

    public static final class If extends StmtExpr1<Boolean> {

        public static If create(StmtExpr0 thenStmts) {
            return new If(thenStmts);
        }

        private final StmtExpr0 thenStmts;

        private If(StmtExpr0 thenStmts) {
            this.thenStmts = thenStmts;
        }

        public StmtExpr0 bind1(final Expr0<? extends Boolean> condition) {
            return new StmtExpr0 () {
                public String toSrc() {
                    return "if (" + condition.toSrc() + ") {\n\t" + thenStmts.toSrc() + "}\n";
                }
            };
        }
    }

    public static final class Assignment<T> extends StmtExpr1<T> {

        public static <T> Assignment<T> create(VarExpr<T> v) {
            return new Assignment<T>(v);
        }

        public final VarExpr<T> variable;
        
        public Assignment(VarExpr<T> v) {
            this.variable = v;
        }

        public StmtExpr0 bind1(final Expr0<? extends T> x1) {
            return new StmtExpr0() {
                public String toSrc() {
                    return variable.toSrc() + " = " + x1.toSrc() + ";\n";
                }
            };
        }
    }
}
