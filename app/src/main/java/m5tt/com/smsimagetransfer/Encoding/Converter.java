package m5tt.com.smsimagetransfer.Encoding;

/**
 * @author Matthew Baldwin
 */

public interface Converter
{
    byte[] encode(byte[] data);
    byte[] decode(byte[] data);
}
