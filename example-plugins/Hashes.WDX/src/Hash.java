

import java.nio.ByteBuffer;

import java.util.zip.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public enum Hash {
    CRC32(32, new ChecksumFactory(java.util.zip.CRC32.class)),
    Adler32(32, new ChecksumFactory(java.util.zip.Adler32.class)),
    MD2(128, new MessageDigestFactory()),
    MD5(128, new MessageDigestFactory()),
    SHA1(160, new MessageDigestFactory()),
    SHA256(256, new MessageDigestFactory("SHA-256")),
    SHA384(384, new MessageDigestFactory("SHA-384")),
    SHA512(512, new MessageDigestFactory("SHA-512"));


    public static abstract class Instance {

        private final Hash hash;

        Instance(Hash hash) {
            this.hash = hash;
        }

        abstract void update(ByteBuffer buf);

        public abstract byte[] getValue();

        public final String name() {
            return this.hash.name();
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

    static class ChecksumFactory implements Factory {

        private Class<? extends java.util.zip.Checksum> checksumClass;

        ChecksumFactory(Class<? extends java.util.zip.Checksum> checksumClass) {
            this.checksumClass = checksumClass;
        }

        Checksum checksumInstance() {
            try {
                return this.checksumClass.newInstance();
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            }
        }

        public Instance instance(final Hash hash) {
            return new Instance(hash) {
                private Checksum cs = checksumInstance();
                private byte[] byteArray = null;

                void update(ByteBuffer buf) {
                    if (buf.hasArray()) {
                        byte[] byteArray = buf.array();
                        cs.update(byteArray, buf.arrayOffset(), buf.remaining());
                        buf.position(buf.limit());
                    } else {
                        if (this.byteArray == null) {
                            this.byteArray = new byte[buf.capacity()];
                        }
                        while (buf.remaining() >= this.byteArray.length) {
                            buf.get(this.byteArray);
                        }
                        int n = buf.remaining();
                        if (n > 0) {
                            buf.get(this.byteArray, 0, n);
                            cs.update(this.byteArray, 0, n);
                        }
                    }
                }

                public byte[] getValue() {
                    final long value = cs.getValue();
                    return new byte[] { (byte)(value >> 24), (byte)(value >> 16), (byte)(value >> 8), (byte)value };
                }
            };
        }
    }

    static class MessageDigestFactory implements Factory {

        private String algorithmName = null;

        MessageDigestFactory(String algorithmName) {
            this.algorithmName = algorithmName;
        }

        MessageDigestFactory() {
        }

        public Instance instance(final Hash hash) {
            if (algorithmName == null) {
                algorithmName = hash.name();
            }
            try {
                return new Instance(hash) {
                    MessageDigest md = MessageDigest.getInstance(algorithmName);

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

    private final int bitLength;
    private Factory factory;

    Hash(int bitLength, Factory factory) {
        this.bitLength = bitLength;
        this.factory = factory;
    }

    public int bitLength() {
        return this.bitLength;
    }

    public Instance instance() {
        return this.factory.instance(this);
    }

}
