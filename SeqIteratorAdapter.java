import java.util.*;

public abstract class SeqIteratorAdapter<T> implements SeqIterator<T>, Enumeration<T> {

    private T nextOut;
    private boolean nextValid = false;
    private boolean endOfSeqCalled = false;

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
            return false;
        }
        if (nextValid) {    // TODO: rename "nextValid" -> "gotOnePending"
            return true;
        }
        nextOut = seekNext();
        return (nextValid = !endOfSeqCalled);
    }

    public final boolean hasMoreElements() {
        return hasNext();
    }

    public final T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        nextValid = false;    // TODO: rename "nextValid" -> "gotOnePending"
        return nextOut;
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

    public final SeqIterator<T> filter(final Func1<? super T, ? extends Boolean> predicate) {
        final SeqIterator<T> underlying = this;
        return new SeqIteratorAdapter<T>() {
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

    public final <U> SeqIterator<U> map(final Func1<? super T, ? extends U> map) {
        final SeqIterator<T> underlying = this;
        return new SeqIteratorAdapter<U>() {
            protected U seekNext() {
                return underlying.hasNext() ? map.apply(underlying.next()) : endOfSeq();
            }
        };
    }

    public final List<T> toList() {
        return Collections.list(this);
    }
}