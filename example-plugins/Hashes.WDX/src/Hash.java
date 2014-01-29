

import java.nio.ByteBuffer;

import java.util.zip.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public enum Hash {
    CRC32("CRC32", 32, new ChecksumFactory() { Checksum checksumInstance() { return new java.util.zip.CRC32(); } }),
    Adler32("Adler32", 32, new ChecksumFactory() { Checksum checksumInstance() { return new java.util.zip.Adler32(); } }),
    MD2("MD2", 128, new MessageDigestFactory()),
    MD5("MD5", 128, new MessageDigestFactory());

    public static abstract class Instance {

        private final Hash hash;

        Instance(Hash hash) {
            this.hash = hash;
        }

        abstract void update(ByteBuffer buf);

        public abstract byte[] getValue();

        public final String getName() {
            return this.hash.getName();
        }

        public int bitLength() {
            return this.hash.bitLength();
        }

        public String getValueAsHex() {
            StringBuilder result = new StringBuilder();
            byte[] bytes = this.getValue();
            for (int i = 0; i < bytes.length; i++) {
                int b = bytes[i] & 0xFF;
                if (b < 0x10) {
                    result.append(0);
                }
                result.append(Integer.toString(b, 16));
            }
            return result.toString();
        }

    }

    static interface Factory {
        Instance instance(Hash hash);
    }

    static abstract class ChecksumFactory implements Factory {

        abstract Checksum checksumInstance();

        public Instance instance(final Hash hash) {
            return new Instance(hash) {
                Checksum cs = checksumInstance();

                void update(ByteBuffer buf) {
                    if (!buf.hasArray()) {
                        throw new RuntimeException();
                    }
                    byte[] byteArray = buf.array();
                    cs.update(byteArray, buf.arrayOffset(), buf.remaining());
                    buf.position(buf.limit());
                }

                public byte[] getValue() {
                    final long value = cs.getValue();
                    return new byte[] { (byte)(value >> 24), (byte)(value >> 16), (byte)(value >> 8), (byte)value };
                }
            };
        }
    }

    static class MessageDigestFactory implements Factory {

        public Instance instance(final Hash hash) {
            try {
                return new Instance(hash) {
                    MessageDigest md = MessageDigest.getInstance(hash.getName());

                    void update(ByteBuffer buf) {
                        md.update(buf);
                    }

                    public byte[] getValue() {
                        return md.digest();
                    }
                };
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private final String name;
    private final int bitLength;
    private Factory factory;

    Hash(String name, int bitLength, Factory factory) {
        this.name = name;
        this.bitLength = bitLength;
        this.factory = factory;
    }

    public String getName() {
        return this.name;
    }

    public int bitLength() {
        return this.bitLength;
    }

    public Instance instance() {
        return this.factory.instance(this);
    }

}
