
/* Represents an expression of type T
 * with no free variables inside.
 */
public abstract class Expr0<T> {

    public abstract String toSrc();

    public String toString() {
        return toSrc();
    }
}
