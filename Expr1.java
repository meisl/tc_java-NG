

/* Represents an expression of type T
 * with (at least one occurrence of) one free variable of type S inside.
 */
public abstract class Expr1<S, T> {

    /* Bind each occurrence of the first free variable (of type S)
     * to the speficied expression with no free variables
     * and return the resulting expression of type T with no free variables.
     */
    public abstract Expr0<T> bind1(Expr0<? extends S> x1);

    /* Bind each occurrence of the first free variable (of type S)
     * to the speficied expression of type S with one free variable of type U
     * and return the resulting expression of type T with one free variable of type U.
     */
    public final <U> Expr1<U, T> bind1(final Expr1<U, S> x1) {
        final Expr1<S, T> self = this;
        return new Expr1<U, T> () {
            public Expr0<T> bind1(Expr0<? extends U> y1) {
                return self.bind1(x1.bind1(y1));
            }
        };
    }

}
