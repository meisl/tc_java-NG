import java.util.*;
import java.io.*;


public class Main {
    
    
    public static void main(String... args) throws IOException, InterruptedException {
        NtfsStreamsJ plugin = new NtfsStreamsJ();
        for (String arg: args) {
            System.out.println();
            System.out.println(arg);
            List<AlternateDataStream> streams = plugin.getStreamsWithHelper(plugin.lads_exe, arg);
            System.out.println("  " + streams.size() + " streams:");
            for (AlternateDataStream s: streams) {
                System.out.println("   " + s);
            }
        }
    }

}