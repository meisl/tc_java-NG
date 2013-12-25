import java.util.*;

public abstract class SectionIteratorAdapter<TKey, TElem> implements SectionIterator<TKey, TElem> {

    private final TKey key;
    
    public SectionIteratorAdapter(TKey key) {
        this.key = key;
    }

    public final TKey key() {
        return key;
    }

}