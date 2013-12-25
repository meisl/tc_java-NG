
public abstract class Predicate<TArg> extends Func1<TArg, Boolean> {

    public static <T> Predicate<T> notNull() {
        return new Predicate<T>() {
            public Boolean apply(T obj) {
                return obj != null;
            }
            public String toString() {
                return "\\e.(e != null)";
            }
        };
    }
}
