package m5tt.com.smsimagetransfer.SMS.Messages;

import android.util.Base64;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import m5tt.com.smsimagetransfer.Encoding.Base91Converter;
import m5tt.com.smsimagetransfer.Encoding.Converter;
import m5tt.com.smsimagetransfer.SMS.Packets.Packet;

/**
 * Created by 1550991 on 11/13/2017.
 */

public class DataDecoder {

    File outFile;
    byte[] decodedData;
    List<Packet> packetList;

    public DataDecoder(File file, List<Packet> packetList){
        this.outFile = file;
        this.packetList = packetList;
    }

    private void decode(){
        // Sort incoming packets
        Collections.sort(packetList, new Comparator<Packet>() {
            @Override
            public int compare(Packet packet, Packet t1) {
                return Integer.compare(packet.getNum(), t1.getNum());
            }
        });

        StringBuilder messageBuilder = new StringBuilder();

        for(Packet packet : packetList){
            messageBuilder.append(packet.getBody());
        }

        //Converter converter = new Base91Converter();
        decodedData = Base64.decode(messageBuilder.toString(), Base64.DEFAULT);
    }

    public void write() throws IOException {
        decode();
        try(FileOutputStream writer = new FileOutputStream(outFile)){
            writer.write(decodedData);
            writer.flush();
        }
    }

}
