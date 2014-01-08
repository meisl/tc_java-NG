package fun;

public class Tuple2<T0, T1> extends Tuple1<T0> {

    public static <T1, TTuple extends Tuple2<?, ? extends T1>> Fn1<TTuple, T1> fn_project1() {
        return new Fn1<TTuple, T1>() {
            public T1 apply(TTuple t) {
                return t.item1;
            }
            public String toString() {
                return "\\t.(t.item1)";
            }
        };
    }

    private static <T, TTuple extends Tuple2<? extends T, ? extends T>> Fn1<TTuple, T> getProjection(String paramName, int i) {
        switch (i) {
            case 0: return fn_project0();
            case 1: return fn_project1();
        }
        throwNoSuchItem(paramName, i);
        return null;
    }

    public static <T> Fn1<Tuple2<T,T>, Tuple2<T,T>> fn_permute(final int a, final int b) {
        final Fn1<Tuple2<T,T>, T> p0 = getProjection("a", a);
        final Fn1<Tuple2<T,T>, T> p1 = getProjection("b", b);
        return new Fn1<Tuple2<T,T>, Tuple2<T,T>>() {
            public Tuple2<T,T> apply(Tuple2<T,T> t) {
                return Tuple.create(p0.apply(t), p1.apply(t));
            }
        };
    }

    public final T1 item1;

    protected Tuple2(T0 item0, T1 item1) {
        super(item0);
        this.item1 = item1;
    }
    
    public String toString() {
        return "<" + item0 + ", " + item1 + ">";
    }
}