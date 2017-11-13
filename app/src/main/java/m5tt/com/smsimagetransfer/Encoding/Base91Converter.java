package m5tt.com.smsimagetransfer.Encoding;

/**
 * @author Matthew Baldwin
 */

public class Base91Converter implements Converter
{
    @Override
    public byte[] encode(byte[] data)
    {
        return new Base91().encode(data);
    }

    @Override
    public byte[] decode(byte[] data)
    {
        return new Base91().decode(data);
    }
}
