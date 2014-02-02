package plugins;

import java.io.IOException;

/** Wraps an IOException in an unchecked exception (ie one which you do not need to declare);
  * used as a work-around in {@link plugins.wdx.ContentPlugin#contents}
  *
  * @author Matthias Kling (meisl)
  */
@SuppressWarnings("serial")
public class UncheckedIOException extends RuntimeException {
    final IOException inner;
    public UncheckedIOException(IOException inner) {
        super(inner);
        this.inner = inner;
    }
}
