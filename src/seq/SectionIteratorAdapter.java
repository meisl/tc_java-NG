package seq;

import java.util.*;

public abstract class SectionIteratorAdapter<TKey, TElem> extends SeqIteratorAdapter<TElem> implements SectionIterator<TKey, TElem> {

    private final TKey key;
    private TElem firstResult;
    boolean beforeFirst;
    
    public SectionIteratorAdapter(TKey key) {
        this(key, null, false);
    }

    public SectionIteratorAdapter(TKey key, TElem firstResult) {
        this(key, firstResult, true);
    }
    
    public SectionIteratorAdapter(TKey key, TElem firstResult, boolean isFirstValid) {
        this.key = key;
        this.beforeFirst = isFirstValid;
        this.firstResult = firstResult;
    }

    public final TKey key() {
        return key;
    }
    
    protected final TElem seekNext() {
        if (beforeFirst) {
            beforeFirst = false;
            return firstResult;
        }
        return seekNext(this.key);
    }

    protected abstract TElem seekNext(TKey key);

}