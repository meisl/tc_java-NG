import java.util.*;
import java.io.*;

import plugins.AlternateDataStream;


public class Main {

    public static void main(String... args) throws IOException, InterruptedException {
        NtfsStreamsJ plugin = new NtfsStreamsJ(NtfsStreamsJ.Helper.STREAMS);
        plugin.listFields();
        plugin.runTests(args);
        for (String arg: args) {
            System.out.println();
            System.out.println("Main(..): " + arg);
            List<AlternateDataStream> streams = plugin.getStreams(arg);
            System.out.println("  " + streams.size() + " streams:");
            for (AlternateDataStream s: streams) {
                System.out.println("   " + s);
            }
        }
    }

}