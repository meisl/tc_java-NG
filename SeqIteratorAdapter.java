import java.util.*;

public abstract class SeqIteratorAdapter<T> implements SeqIterator<T> {

    private T nextOut;
    protected boolean nextValid = false;

    protected abstract T seekNext();

    public boolean hasNext() {
        if (nextValid) {
            return true;
        }
        nextValid = true;
        return (nextOut = seekNext()) != null;
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

    public final SeqIterator<T> filter(final Func1<T, Boolean> predicate) {
        return null;
    }

}