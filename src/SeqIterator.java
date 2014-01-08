import java.util.*;

public interface SeqIterator<T> extends Iterator<T>, Enumeration<T> {

    public SeqIterator<T> concat(SeqIterator<? extends T> tail);

    public SeqIterator<T> append(T oneMore) throws IllegalStateException;

    public SeqIterator<T> filter(Fn1<? super T, ? extends Boolean> predicate);

    public <U> SeqIterator<U> map(Fn1<? super T, ? extends U> mapFn);

    public <TKey> SeqIterator<SectionIterator<TKey, T>> sectionBy(
        final Fn2<? super T, ? super TKey, ? extends TKey> keyOf
    );

    public <TKey> SeqIterator<SectionIterator<TKey, T>> sectionBy(
        final Fn2<? super T, ? super TKey, ? extends TKey> keyOf,
        final Fn1<? super T, ? extends Boolean> filter
    );


    public List<T> toList();

}