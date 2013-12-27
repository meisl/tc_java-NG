
public abstract class Tuple {

    protected static final void throwNoSuchItem(String paramName, int i) {
        throw new IllegalArgumentException("parameter " + paramName + " out of bounds: " + i);
    }

    public static final <T0> Tuple1<T0> create(T0 item) {
        return new Tuple1<>(item);
    }

    public static final <T0, T1> Tuple2<T0, T1> create(T0 item0, T1 item1) {
        return new Tuple2<>(item0, item1);
    }

    public static final <T0, T1, T2> Tuple3<T0, T1, T2> create(T0 item0, T1 item1, T2 item2) {
        return new Tuple3<>(item0, item1, item2);
    }

}