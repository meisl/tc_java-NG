import java.util.*;
import java.io.*;


public class Main {
    
    
    public static void main(String... args) throws IOException, InterruptedException {
        NtfsStreamsJ plugin = new NtfsStreamsJ();
        System.out.println(args[0]);
        List<AlternateDataStream> streams = plugin.getStreamsWithHelper(plugin.streams_exe, args[0]);
        System.out.println(streams.size());
    }

}