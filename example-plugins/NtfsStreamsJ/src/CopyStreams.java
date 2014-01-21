import java.io.*;
import plugins.AlternateDataStream;

public class CopyStreams {

    public static void main(String... args) throws IOException, InterruptedException {
        System.out.println("CopyStreams.main");
        if (args.length != 2) {
            throw new IllegalArgumentException("need two file names");
        }
        File fileFrom = new File(args[0]);
        File fileTo = new File(args[1]);
        
        NtfsStreamsJ plugin = new NtfsStreamsJ();
        int n = 0, skipped = 0;
        for (AlternateDataStream streamFrom: plugin.getStreams(fileFrom.getPath())) {
            n++;
            AlternateDataStream streamTo = new AlternateDataStream(fileTo, streamFrom.getRawName());
            if (streamTo.exists()) {
                skipped++;
                System.out.println("skipped :" + streamTo.getRawName() + " - already exists on " + fileTo);
            } else {
                streamTo.setContents(streamFrom.getContents());
            }
        }
        System.out.println("copied " + (n - skipped) + " of " + n + " (" + skipped + " skipped).");
    }
}