package fun;

public class Tuple3<T0, T1, T2> extends Tuple2<T0, T1> {

    public static <T2, TTuple extends Tuple3<?, ?, ? extends T2>> Fn1<TTuple, T2> fn_project2() {
        return new Fn1<TTuple, T2>() {
            public T2 apply(TTuple t) {
                return t.item2;
            }
        };
    }

    private static <T, TTuple extends Tuple3<? extends T, ? extends T, ? extends T>> Fn1<TTuple, T> getProjection(String paramName, int i) {
        switch (i) {
            case 0: return fn_project0();
            case 1: return fn_project1();
            case 2: return fn_project2();
        }
        throwNoSuchItem(paramName, i);
        return null;
    }

    public static <T> Fn1<Tuple3<T,T,T>, Tuple3<T,T,T>> fn_permute(final int a, final int b, final int c) {
        final Fn1<Tuple3<T,T,T>, T> p0 = getProjection("a", a);
        final Fn1<Tuple3<T,T,T>, T> p1 = getProjection("b", b);
        final Fn1<Tuple3<T,T,T>, T> p2 = getProjection("c", c);
        return new Fn1<Tuple3<T,T,T>, Tuple3<T,T,T>>() {
            public Tuple3<T,T,T> apply(Tuple3<T,T,T> in) {
                return Tuple.create(p0.apply(in), p1.apply(in), p2.apply(in));
            }
        };
    }


    public final T2 item2;

    protected Tuple3(T0 item0, T1 item1, T2 item2) {
        super(item0, item1);
        this.item2 = item2;
    }
    
    public String toString() {
        return "<" + item0 + ", " + item1 + ", " + item2 + ">";
    }
}