

import java.nio.ByteBuffer;

import java.util.zip.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;



public enum Hash {
    CRC32(32, ProcessorFactory.fromChecksum(java.util.zip.CRC32.class)),
    Adler32(32, ProcessorFactory.fromChecksum(java.util.zip.Adler32.class)),
    MD2(128, ProcessorFactory.fromMessageDigest("MD2")),
    MD5(128, ProcessorFactory.fromMessageDigest("MD5")),
    SHA1(160, ProcessorFactory.fromMessageDigest("SHA1")),
    SHA256(256, ProcessorFactory.fromMessageDigest("SHA-256")),
    SHA384(384, ProcessorFactory.fromMessageDigest("SHA-384")),
    SHA512(512, ProcessorFactory.fromMessageDigest("SHA-512"));


    public static abstract class Processor {

        public final Hash hash;

        Processor(Hash hash) {
            this.hash = hash;
        }

        public abstract Processor update(ByteBuffer buf);

        public abstract byte[] getValueAsBytes();

        public String getValue() {
            StringBuilder result = new StringBuilder();
            byte[] bytes = this.getValueAsBytes();
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

    static abstract class ProcessorFactory {

        public abstract Processor newProcessor(Hash hash);

        public static ProcessorFactory fromChecksum(final Class<? extends java.util.zip.Checksum> checksumClass) {
            return new ProcessorFactory() {

                Checksum checksumInstance() {
                    try {
                        return checksumClass.newInstance();
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    } catch (InstantiationException e) {
                        throw new RuntimeException(e);
                    }
                }

                public Processor newProcessor(final Hash hash) {
                    return new Processor(hash) {
                        private Checksum cs = checksumInstance();
                        private byte[] byteArray = null;

                        public Processor update(ByteBuffer buf) {
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
                            return this;
                        }

                        public byte[] getValueAsBytes() {
                            final long value = cs.getValue();
                            return new byte[] { (byte)(value >> 24), (byte)(value >> 16), (byte)(value >> 8), (byte)value };
                        }
                    };
                }
            };
        }

        public static ProcessorFactory fromMessageDigest(final String algorithmName) {
            return new ProcessorFactory() {

                public Processor newProcessor(final Hash hash) {
                    try {
                        return new Processor(hash) {
                            MessageDigest md = MessageDigest.getInstance(algorithmName);

                            public Processor update(ByteBuffer buf) {
                                md.update(buf);
                                return this;
                            }

                            public byte[] getValueAsBytes() {
                                return md.digest();
                            }
                        };
                    } catch (NoSuchAlgorithmException e) {
                        throw new RuntimeException(e);
                    }
                }
            };
        }
    }

/* ------------------------------------------------------------------------------------------ */

    private final int bitLength;
    private ProcessorFactory factory;

    Hash(int bitLength, ProcessorFactory factory) {
        this.bitLength = bitLength;
        this.factory = factory;
    }

    public int bitLength() {
        return this.bitLength;
    }

    public Processor newProcessor() {
        return this.factory.newProcessor(this);
    }

    public String getValue(ByteBuffer buf) {
        return newProcessor().update(buf).getValue();
    }

    public byte[] getValueAsBytes(ByteBuffer buf) {
        return newProcessor().update(buf).getValueAsBytes();
    }

    public String getValue(Iterable<ByteBuffer> bufs) {
        Processor p = newProcessor();
        for (ByteBuffer buf: bufs) {
            p.update(buf);
        }
        return p.getValue();
    }

    public byte[] getValueAsBytes(Iterable<ByteBuffer> bufs) {
        Processor p = newProcessor();
        for (ByteBuffer buf: bufs) {
            p.update(buf);
        }
        return p.getValueAsBytes();
    }

}
