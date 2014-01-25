package fun;

public abstract class Predicate<TArg> extends Fn1<TArg, Boolean> {

    private static Predicate<?> fn_true = new Predicate<Object>() {
        public Boolean apply(Object obj) {
            return true;
        }
        public String toString() {
            return "\\e.(true)";
        }
    };

    private static Predicate<?> fn_false = new Predicate<Object>() {
        public Boolean apply(Object obj) {
            return false;
        }
        public String toString() {
            return "\\e.(false)";
        }
    };

    private static Predicate<?> fn_notNull = new Predicate<Object>() {
        public Boolean apply(Object obj) {
            return obj != null;
        }
        public String toString() {
            return "\\e.(e != null)";
        }
    };

    @SuppressWarnings("unchecked")
    public static <T> Predicate<T> constTrue() {
        return (Predicate<T>)fn_true;
    }

    @SuppressWarnings("unchecked")
    public static <T> Predicate<T> constFalse() {
        return (Predicate<T>)fn_false;
    }

    @SuppressWarnings("unchecked")
    public static <T> Predicate<T> notNull() {
        return (Predicate<T>)fn_notNull;
    }

}
