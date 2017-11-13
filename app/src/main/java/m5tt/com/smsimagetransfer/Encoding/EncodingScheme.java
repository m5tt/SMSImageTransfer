package m5tt.com.smsimagetransfer.Encoding;

/**
 * Interface for converting data to and from a text-based representation
 */

public interface EncodingScheme {
    public byte[] encode(byte[] data);
    public byte[] decode(byte[] data);
}
