import java.io.IOException;
import java.io.File;

import java.nio.*;
import java.nio.file.*;

import java.util.regex.*;

import plugins.*;
import plugins.wdx.ContentPlugin;


public class Hashes extends ContentPlugin {

    private final Pattern hashContentsPattern = Pattern.compile("^([0-9a-fA-F]+)@([0-9]+)$");

    public String getHashFromStream(Hash hash, File file) throws IOException {
        if (file.isDirectory()) {
            return null;
        }
        long lastModified = file.lastModified();
        AlternateDataStream hashADS = new AlternateDataStream(file, "Hash." + hash.name());
        if (hashADS.exists()) {
            String hashContents = hashADS.getContents();
            long time;
            // TODO: make regex match thread safe
            Matcher matcher = hashContentsPattern.matcher(hashContents);
            if (matcher.matches()) {
                String hashStr = matcher.group(1);
                if (hashStr.length() == hash.bitLength() / 4) {
                    String timeStr = matcher.group(2);
                    time = Long.parseLong(timeStr, 10);
                    if (lastModified <= time) {
                        return hashStr;
                    } else {
                        log.warn("invalid :Hash." + hash.name() + " " + hashContents + " on \"" + file.getPath() + "\" - outdated by " + (lastModified - time) + " ms");
                    }
                }
            } else {
                log.warn("invalid :Hash." + hash.name() + " " + hashContents + " on \"" + file.getPath() + "\" - not matching /" + hashContentsPattern + "/");
            }
            hashADS.deleteIfExists();
        }
        return null;
    }


    public String getHash(Hash hash, String fileName) throws IOException {
        File file = new File(fileName);
        if (file.isDirectory()) {
            return null;
        }
        String hashFromStream = getHashFromStream(hash, file);
        if (hashFromStream != null) {
            return hashFromStream;
        }
        // TODO: lock file while calculating hash
        long lastModified = file.lastModified();
        AlternateDataStream hashADS = new AlternateDataStream(file, "Hash." + hash.name());

        long ttlRead = 0;
        long t = -System.currentTimeMillis();

        Hash.Instance hi = hash.instance();
        for (ByteBuffer buffer: contents(fileName)) {
            ttlRead += buffer.remaining();
            hi.update(buffer);
        }

        t += System.currentTimeMillis();
        log.info("time=" + (t / 1000.0) + " sec, ttlRead=" + ttlRead + ", " + ((double)ttlRead * 0.00095367431640625 / t ) + " MB/sec");

        String result = hi.getValueAsHex();
        if (file.canWrite() && (file.lastModified() == lastModified) && (file.length() > 32 * 1024)) {
            t = System.currentTimeMillis();
            hashADS.createIfNotExists().setContents(result + "@" + t);
            file.setLastModified(lastModified); // reset to original
        }
        return result;
    }

    private void defineHashField(final Hash h) {
        define(new Field.STRING(h.name()) {
            public boolean isDelayInOrder(String fileName) throws IOException {
                File file = new File(fileName);
                if (file.isDirectory())
                    return false;
                if (file.length() < 1024 * 50)
                    return false;
                return getHashFromStream(h, file) == null;
            }
            public String getValue(String fileName) throws IOException {
                return getHash(h, fileName);
            }
        });
    }

    protected void initFields() {
        for (Hash h: Hash.values()) {
            defineHashField(h);
        }
    }

    public static void main(String... args) throws IOException {
        new Hashes().runTests(args);
    }

}