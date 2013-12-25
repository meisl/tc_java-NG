import java.util.*;

public interface SeqIterator<T> extends Iterator<T>, Enumeration<T> {

    public SeqIterator<T> concat(SeqIterator<? extends T> tail);

    public SeqIterator<T> filter(Func1<? super T, ? extends Boolean> predicate);

    public <U> SeqIterator<U> map(Func1<? super T, ? extends U> mapFn);

    public SeqIterator<T> append(T oneMore) throws IllegalStateException;

    public <TKey> SeqIterator<SectionIterator<TKey, T>> section(Func2<? super T, ? super TKey, ? extends TKey> keyOf);

    public List<T> toList();

}