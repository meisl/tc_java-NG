
public class ConstExpr<T> extends Expr0<T> {

    private T value;

    public ConstExpr(Class<T> type, T value) {
        this(value);
    }

    public ConstExpr(T value) {
        this.value = value;
    }

    public String toSrc() {
        return "" + this.value;
    }
    
    public String toString() {
        return toSrc();
    }

}
