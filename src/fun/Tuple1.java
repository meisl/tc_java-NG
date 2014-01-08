package fun;

public class Tuple1<T0> extends Tuple {

    public static <T0, TTuple extends Tuple1<? extends T0>> Fn1<TTuple, T0> fn_project0() {
        return new Fn1<TTuple, T0>() {
            public T0 apply(TTuple t) {
                return t.item0;
            }
            public String toString() {
                return "\\t.(t.item0)";
            }
        };
    }

    public final T0 item0;

    protected Tuple1(T0 item0) {
        this.item0 = item0;
    }
    
    public String toString() {
        return "<" + item0 + ">";
    }
}