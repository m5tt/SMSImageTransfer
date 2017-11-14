package m5tt.com.smsimagetransfer.SMS;

import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import m5tt.com.smsimagetransfer.Encoding.Base91Converter;
import m5tt.com.smsimagetransfer.Encoding.Converter;
import m5tt.com.smsimagetransfer.SMS.Messages.DataDecoder;
import m5tt.com.smsimagetransfer.SMS.Packets.Packet;
import m5tt.com.smsimagetransfer.SMS.Packets.PacketFactory;

/**
 * @author Matthew Baldwin
 */
public class SMSInputOutput
{
    private final static Converter converter = new Base91Converter();

    public List<Packet> fileToPackets(File file)
    {
        List<Packet> packets = null;

        try
        {
            // TODO: confirm there's no issues with new String(readFile())
            /*packets = new PacketFactory()
                    .getMessagePackets(new String(
                            (converter.encode(readFile(file)))));*/

            // For emulator purposes, use base64 encode due to bug with reception of base 91
            packets = new PacketFactory()
                    .getMessagePackets(new String(
                            (Base64.encode(readFile(file),Base64.DEFAULT))));
        }
        catch (IOException e) // TODO
        {

        }

        return packets;
    }

    public void packetsToFile(File outputFile, List<Packet> packets) throws IOException {

        DataDecoder decoder = new DataDecoder(outputFile, packets);
        decoder.write();

    }


    private byte[] readFile(File file) throws IOException
    {
        FileInputStream fis = new FileInputStream(file);

        byte[] data = new byte[(int) file.length()];
        fis.read(data);
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
