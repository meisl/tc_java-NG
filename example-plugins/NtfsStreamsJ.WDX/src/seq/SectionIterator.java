package seq;

import java.util.*;

public interface SectionIterator<TKey, TElem> extends SeqIterator<TElem> {

    public TKey key();

}