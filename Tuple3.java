
public class Tuple3<T0, T1, T2> {

    public static <T0, T1, T2> Func1<Tuple3<T0,T1,T2>, T0> fn_project0() {
        return new Func1<Tuple3<T0,T1,T2>, T0>() {
            public T0 apply(Tuple3<T0,T1,T2> t) {
                return t.item0;
            }
        };
    }

    public static <T0, T1, T2> Func1<Tuple3<T0,T1,T2>, T1> fn_project1() {
        return new Func1<Tuple3<T0,T1,T2>, T1>() {
            public T1 apply(Tuple3<T0,T1,T2> t) {
                return t.item1;
            }
        };
    }

    public static <T0, T1, T2> Func1<Tuple3<T0,T1,T2>, T2> fn_project2() {
        return new Func1<Tuple3<T0,T1,T2>, T2>() {
            public T2 apply(Tuple3<T0,T1,T2> t) {
                return t.item2;
            }
        };
    }
    
    private static <T> Func1<Tuple3<T,T,T>, T> getProjection(String paramName, int i) {
        switch (i) {
            case 0: return fn_project0();
            case 1: return fn_project1();
            case 2: return fn_project2();
        }
        throw new IllegalArgumentException("parameter " + paramName + " out of bounds: " + i);
    }

    public static <T> Func1<Tuple3<T,T,T>, Tuple3<T,T,T>> fn_permute(int a, int b, int c) {
        final Func1<Tuple3<T,T,T>, T> p0 = getProjection("a", a);
        final Func1<Tuple3<T,T,T>, T> p1 = getProjection("b", b);
        final Func1<Tuple3<T,T,T>, T> p2 = getProjection("c", c);
        return new Func1<Tuple3<T,T,T>, Tuple3<T,T,T>>() {
            public Tuple3<T,T,T> apply(Tuple3<T,T,T> in) {
                return new Tuple3<>(p0.apply(in), p1.apply(in), p2.apply(in));
            }
        };
    }

    public final T0 item0;
    public final T1 item1;
    public final T2 item2;

    public Tuple3(T0 item0, T1 item1, T2 item2) {
        this.item0 = item0;
        this.item1 = item1;
        this.item2 = item2;
    }
    
    public String toString() {
        return "<" + item0 + ", " + item1 + ", " + item2 + ">";
    }
}