import fun.*;
import java.util.*;

public abstract class SeqIteratorAdapter<T> implements SeqIterator<T>, Enumeration<T> {

    public static final <T> SeqIterator<T> singleton(final T elem) {
        return new SeqIteratorAdapter<T>("singleton(" + elem + ")") {
            private boolean done = false;
            public T seekNext() {
                if (done) return endOfSeq();
                done = true;
                return elem;
            }
        };
    }

    private T nextOut;
    private boolean nextValid = false;
    private boolean endOfSeqCalled = false;

    private SeqIterator<T> tail = null;
    
    protected T previous = null;

    private String description;

    public SeqIteratorAdapter() {
        this.description = super.toString();
    }

    public SeqIteratorAdapter(String description) {
        this.description = description;
    }
    
    public final String toString() {
        return (this.tail == null) ? description : description + "\n  .concat(" + this.tail + ")";
    }

    protected final T endOfSeq() {
        if (endOfSeqCalled) {
            throw new IllegalStateException("endOfSeq() must be called more than once!");
        }
        endOfSeqCalled = true;
        return null;
    }

    /* Should return next item or <code>return endOfSeq();</code> if there are no more.
     * Note that you MUST call {@link endOfSeq} in order to indicate the end of the sequence,
     * this allows for <code>null</code> to be contained in the sequence.
     * Also note that {@link endOfSeq} will throw an IllegalStateException when called a 2nd time.
     * Subclasses have the guarantee that seekNext() will never be called again 
     * after {@link endOfSeq} they've called {@link endOfSeq} for the first time;
     */
    protected abstract T seekNext();

    public boolean hasNext() {
        if (endOfSeqCalled) {
            if (this.tail != null) {
                if (nextValid) {
                    return true;
                }
                if (this.tail.hasNext()) {
                    nextOut = this.tail.next();
                    return (nextValid = true);
                }
            }
            return false;
        }
        if (nextValid) {    // TODO: rename "nextValid" -> "gotOnePending"
            return true;
        }
        nextOut = seekNext();
        return (nextValid = !endOfSeqCalled) || hasNext(); // check tail if endOfSeqCalled
    }

    public final boolean hasMoreElements() {
        return hasNext();
    }

    public final T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        nextValid = false;    // TODO: rename "nextValid" -> "gotOnePending"
        return previous = nextOut;
    }

    public final T nextElement() {
        return next();
    }
    
    public void remove() {
        throw new UnsupportedOperationException();
    }
/*
    public final <U> SeqIterator<U> wrap(Func1<SeqIterator<T>, U> fn) {
        final SeqIterator<T> underlying = this;
        return new SeqIteratorAdapter<U>() {
            protected U seekNext() {
                return fn(underlying);
            }
        };
    }
*/
    public SeqIterator<T> concat(final SeqIterator<? extends T> rest) {
        final SeqIterator<T> head = this;
        return new SeqIteratorAdapter<T>(head.toString() + "\n  .concat(" + rest.toString() + ")") {
            protected T seekNext() {
                if (head.hasNext()) return head.next();
                if (rest.hasNext()) return rest.next();
                return endOfSeq();
            }
        };
    }

    public SeqIterator<T> append(T elem) {
        if (endOfSeqCalled) {
            throw new IllegalStateException("cannot append to exhausted SeqIterator");
        }
        SeqIterator<T> s = singleton(elem);
        this.tail = (this.tail == null) ? s : this.tail.concat(s);
        return this;
    }

    public final SeqIterator<T> filter(final Fn1<? super T, ? extends Boolean> predicate) {
        final SeqIterator<T> underlying = this;
        return new SeqIteratorAdapter<T>(underlying + "\n  .filter(" + predicate + ")") {
            protected T seekNext() {
                while (underlying.hasNext()) {
                    T item = underlying.next();
                    if (predicate.apply(item)) {
                        return item;
                    }
                }
                return endOfSeq();
            }
        };
    }

    public final <U> SeqIterator<U> map(final Fn1<? super T, ? extends U> map) {
        final SeqIterator<T> underlying = this;
        return new SeqIteratorAdapter<U>(underlying + "\n  .map(" + map + ")") {
            protected U seekNext() {
                return underlying.hasNext() ? map.apply(underlying.next()) : endOfSeq();
            }
        };
    }

    public <TKey> SeqIterator<SectionIterator<TKey, T>> sectionBy(
        final Fn2<? super T, ? super TKey, ? extends TKey> keyOf
    ) {
        return this.sectionBy(keyOf, Predicate.constTrue());
    }

    public <TKey> SeqIterator<SectionIterator<TKey, T>> sectionBy(
        final Fn2<? super T, ? super TKey, ? extends TKey> keyOf,
        final Fn1<? super T, ? extends Boolean> filter
    ) {
        final SeqIterator<T> underlying = this;
        return new SeqIteratorAdapter<SectionIterator<TKey, T>>() {
            private TKey lastKey = null;
            private T pendingElem = null; // <<<<
            private boolean gotPending = false;
            
            public SectionIterator<TKey, T> seekNext() {
                T elem;
                if (gotPending) {
                    elem = pendingElem;
                    gotPending = false;
                } else if (underlying.hasNext()) {
                    elem = underlying.next();
                } else {
                    return endOfSeq();
                }
                while (true) { // <<<<
                    TKey key = keyOf.apply(elem, lastKey);
                    if (key.equals(lastKey)) {
                        this.previous.append(elem);
                    } else {
                        lastKey = key;
                        return new SectionIteratorAdapter<TKey, T>(key, elem, filter.apply(elem)) {  // <<<
                            protected T seekNext(TKey currentKey) {
                                if (!underlying.hasNext()) {
                                    return endOfSeq();
                                }
                                T elem = underlying.next();
                                TKey key = keyOf.apply(elem, currentKey);
                                if (key.equals(currentKey)) {
                                    return elem;
                                }
                                pendingElem = elem; // <<<
                                gotPending = true;
                                return endOfSeq();
                            }
                        };
                    }
                    if (underlying.hasNext()) {
                        elem = underlying.next();
                    } else {
                        return endOfSeq();
                    }
                }
            }
        };
    }

    public final List<T> toList() {
        return Collections.list(this);
    }
}