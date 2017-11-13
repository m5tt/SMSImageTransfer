package m5tt.com.smsimagetransfer.SMS.Messages;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.List;

import m5tt.com.smsimagetransfer.Encoding.Base91;
import m5tt.com.smsimagetransfer.SMS.Packets.Packet;
import m5tt.com.smsimagetransfer.SMS.Packets.PacketFactory;

/**
 * Created by chicken on 11/13/17.
 */

public class DataTransfer {

    File file;
    byte[] encodedFile;

    DataTransfer(File file) throws IOException {
        this.file = file;
        encodeToBase91();
    }

    private void encodeToBase91() throws IOException {

        try(FileInputStream fin = new FileInputStream(file)) {

            byte[] data = new byte[(int) file.length()];
            fin.read(data);

            Base91 encoder = new Base91();
            encodedFile = encoder.encode(data);
        }
    }

    // TODO
    public void sendFile(){
        String message = new String(encodedFile);

        PacketFactory packetFactory = new PacketFactory();

        List<Packet> messagePackets = packetFactory.getMessagePackets(message);

        Packet startMessagePacket = packetFactory.getMessageStartPacket(file.getName(), messagePackets.size() , Base91.class.getName());

        Packet endMessagePacket = packetFactory.getMessageEndPacket("NO_CHECKSUM");

        // TODO: SEND START PACKET, WAIT FOR ACK, CHECK ACK SUCCESS

        // TODO: SEND MESSAGE PACKETS

        // TODO: SEND END PACKET
    }
}
