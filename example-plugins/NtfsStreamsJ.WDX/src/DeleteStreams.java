import java.io.*;
import java.nio.file.*;
import plugins.AlternateDataStream;

public class DeleteStreams {

    public static void main(String... args) throws IOException, InterruptedException {
        System.out.println(Files.isSymbolicLink(Paths.get("f:\\Temp\\a\\_.lnk")));
        System.out.println(Files.isRegularFile(Paths.get("f:\\Temp\\a\\_.lnk")));

        if (args.length != 2) {
            throw new IllegalArgumentException("need a file name and a stream name");
        }
        int length = new AlternateDataStream(args[0], args[1]).deleteIfExists();
        if (length < 0) {
            System.err.println("no such stream: " + args[0] + ":" + args[1]);
        } else {
            System.out.println("removed " + args[0] + ":" + args[1] + " (" + length + " bytes)");
        }
    }
}