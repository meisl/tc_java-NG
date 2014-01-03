import java.util.*;
import java.io.*;


public class Main {
    
    
    public static void main(String... args) throws IOException, InterruptedException {
        NtfsStreamsJ plugin = new NtfsStreamsJ(NtfsStreamsJ.Helper.STREAMS);
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