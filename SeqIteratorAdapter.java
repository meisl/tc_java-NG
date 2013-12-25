import java.util.*;

public abstract class SeqIteratorAdapter<T> implements SeqIterator<T> {

    private T nextOut;
    private boolean nextValid = false;

    protected abstract T seekNext();

    public boolean hasNext() {
        if (nextValid) {
            return true;
        }
        nextValid = true;
        return (nextOut = seekNext()) != null;    // TODO: make null a (possibly) valid value of the sequence
    }

    public final T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        nextValid = false;
        return nextOut;
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
                return null;    // TODO: make null a (possibly) valid value of the sequence
            }
        };
    }

    public final <S> SeqIterator<S> map(final Func1<T, S> mapFn) {
        final SeqIterator<T> underlying = this;
        return new SeqIteratorAdapter<S>() {
            protected S seekNext() {
                if (underlying.hasNext()) {
                    T item = underlying.next();
                    return mapFn.apply(item);
                }
                return null;    // TODO: make null a (possibly) valid value of the sequence
            }
        };
    }

}