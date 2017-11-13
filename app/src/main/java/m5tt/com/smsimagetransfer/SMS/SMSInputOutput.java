package m5tt.com.smsimagetransfer.SMS;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import m5tt.com.smsimagetransfer.Encoding.Base91Converter;
import m5tt.com.smsimagetransfer.Encoding.Converter;
import m5tt.com.smsimagetransfer.SMS.Packets.Packet;
import m5tt.com.smsimagetransfer.SMS.Packets.PacketFactory;

/**
 * @author Matthew Baldwin
 */
public class SMSInputOutput
{
    private final static Converter converter = new Base91Converter();

    public List<Packet> fileToPackets(String filename)
    {
        List<Packet> packets = null;

        try
        {
            // TODO: confirm there's no issues with new String(readFile())
            packets = new PacketFactory()
                    .getMessagePackets(new String(
                            (converter.encode(readFile(filename)))));
        }
        catch (IOException e) // TODO
        {
        }

        return packets;
    }

    public void packetsToFile(List<Packet> packets)
    {
        /*
         * 1) Verify correct order
         * 2) Decode each packet
         * 3) Assemble into byte array and write
         */

        // TODO: for correct order - sort by packet number - should be able to access

        return;
    }


    private byte[] readFile(String filename) throws IOException
    {
        File file = new File(filename);
        FileInputStream fis = new FileInputStream(file);

        byte[] data = new byte[(int) file.length()];
        fis.close();

        return data;
    }

    private void writeFile(String filename, byte[] data) throws IOException
    {
        FileOutputStream fos = new FileOutputStream(filename);
        fos.write(data);
        fos.close();
    }
}
