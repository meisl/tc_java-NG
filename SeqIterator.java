import java.util.*;

public interface SeqIterator<T> extends Iterator<T> {

    public SeqIterator<T> filter(Func1<? super T, ? extends Boolean> predicate);

    public <U> SeqIterator<U> map(Func1<? super T, ? extends U> mapFn);

}