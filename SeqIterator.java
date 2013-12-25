import java.util.*;

public interface SeqIterator<T> extends Iterator<T> {

    public SeqIterator<T> filter(Func1<T, Boolean> predicate);
    
    public <U> SeqIterator<U> map(Func1<T, U> mapFn);

}