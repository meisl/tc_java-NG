import java.io.IOException;
import java.io.File;

import java.nio.*;
import java.nio.file.*;

import java.util.regex.*;
import java.security.*;

import plugins.*;
import plugins.wdx.ContentPlugin;


public class Hashes extends ContentPlugin {

    private final Pattern md5ContentsPattern = Pattern.compile("^([0-9a-fA-F]{32})@([0-9]+)$");

    public String getMD5fromStream(File file) throws IOException {
        if (file.isDirectory()) {
            return null;
        }
        long lastModified = file.lastModified();
        AlternateDataStream md5ADS = new AlternateDataStream(file, "MD5");
        if (md5ADS.exists()) {
            String md5Contents = md5ADS.getContents();
            long time;
            // TODO: make regex match thread safe
            Matcher matcher = md5ContentsPattern.matcher(md5Contents);
            if (matcher.matches()) {
                String md5Str = matcher.group(1);
                String timeStr = matcher.group(2);
                time = Long.parseLong(timeStr, 10);
                if (lastModified <= time) {
                    return md5Str;
                } else {
                    log.warn("invalid :MD5 " + md5Contents + " on \"" + file.getPath() + "\" - outdated by " + (lastModified - time) + " ms");
                }
            } else {
                log.warn("invalid :MD5 " + md5Contents + " on \"" + file.getPath() + "\" - not matching /" + md5ContentsPattern + "/");
            }
            md5ADS.deleteIfExists();
        }
        return null;
    }
    public String getMD5(String fileName) throws IOException {
        File file = new File(fileName);
        if (file.isDirectory()) {
            return null;
        }
        String md5FromStream = getMD5fromStream(file);
        if (md5FromStream != null) {
            return md5FromStream;
        }

        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            log.error(e);
            return "";
        }
        // TODO: lock file while calculating MD5
        long lastModified = file.lastModified();
        AlternateDataStream md5ADS = new AlternateDataStream(file, "MD5");

        long ttlRead = 0;
        long t = -System.currentTimeMillis();
        
        
/*
        FileChannel in = FileChannel.open(Paths.get(fileName));
        ByteBuffer buffer = ByteBuffer.allocate(1024 * 128);
        while (in.read(buffer) >= 0) {
            buffer.flip();
            ttlRead += buffer.remaining();
            md.update(buffer);
            buffer.clear();
        };
        in.close();
*/
        
        for (ByteBuffer buffer: contents(fileName)) {
            ttlRead += buffer.remaining();
            md.update(buffer);
        }

        t += System.currentTimeMillis();
        log.info("time=" + (t / 1000.0) + " sec, ttlRead=" + ttlRead + ", " + ((double)ttlRead * 0.00095367431640625 / t ) + " MB/sec");
        
        
        byte[] mdbytes = md.digest();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < mdbytes.length; i++) {
            sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        String result = sb.toString();
        if (file.canWrite() && (file.lastModified() == lastModified) && (file.length() > 32 * 1024)) {
            t = System.currentTimeMillis();
            md5ADS.createIfNotExists().setContents(result + "@" + t);
            file.setLastModified(lastModified); // reset to original
        }
        return result;
    }


    protected void initFields() {

        define(new Field.STRING("MD5") {
            public boolean isDelayInOrder(String fileName) throws IOException {
                File file = new File(fileName);
                if (file.isDirectory())
                    return false;
                if (file.length() < 1024 * 50)
                    return false;
                return getMD5fromStream(file) == null;
            }
            public String getValue(String fileName) throws IOException {
                return getMD5(fileName);
            }
        });

    }

    public static void main(String... args) throws IOException {
        new Hashes().runTests(args);
    }

}