import java.util.*;

public abstract class SeqIteratorAdapter<T> implements SeqIterator<T> {

    private boolean endOfSeqCalled = false;
    private boolean nextValid = false;
    private T nextOut;

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
        T next = seekNext();
        if (endOfSeqCalled) {
            nextValid = false;
            return false;
        }
        this.nextOut = next;
        return true;
    }

    public final T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        nextValid = false;    // TODO: rename "nextValid" -> "gotOnePending"
        return nextOut;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    public final SeqIterator<T> filter(final Func1<T, Boolean> predicate) {
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

    public final <S> SeqIterator<S> map(final Func1<T, S> map) {
        final SeqIterator<T> underlying = this;
        return new SeqIteratorAdapter<S>() {
            protected S seekNext() {
                return underlying.hasNext() ? map.apply(underlying.next()) : endOfSeq();
            }
        };
    }

}