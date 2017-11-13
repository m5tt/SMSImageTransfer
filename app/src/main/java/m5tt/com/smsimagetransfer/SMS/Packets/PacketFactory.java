package m5tt.com.smsimagetransfer.SMS.Packets;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

/**
 * Create packets for data transmission over SMS
 */

public class PacketFactory {

    public enum PacketControlType { MESSAGE_START, MESSAGE_END, MESSAGE_ACK }

    public Packet getMessageStartPacket(int messageLength, String encoding){
        PacketHead head = new PacketHead(Packet.packetTypeValueOf(Packet.PacketType.MESSAGE_START));
        String messageInfo ="LEN:"+messageLength +  "|" +
                            "ENCODING:"+encoding + "|" +
                            "V_APPLICATION:"+ 1 + "|" +
                            "V_PACKET:"+Packet.VERSION + "|" +
                            "V_HEAD:"+PacketHead.VERSION;
        try {
            return new Packet(head,messageInfo).setType(Packet.PacketType.MESSAGE_START);
        } catch (PacketSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Packet getMessageEndPacket(String checksum){
        PacketHead head = new PacketHead(Packet.packetTypeValueOf(Packet.PacketType.MESSAGE_END));
        String messageInfo ="CHCK:"+checksum;
        try {
            return new Packet(head,messageInfo).setType(Packet.PacketType.MESSAGE_END);
        } catch (PacketSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Packet getAckPacket(boolean success){
        PacketHead head = new PacketHead(Packet.packetTypeValueOf(Packet.PacketType.MESSAGE_ACK));
        try {
            return new Packet(head,Boolean.toString(success)).setType(Packet.PacketType.MESSAGE_ACK);
        } catch (PacketSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Packet> getMessagePackets(String message){
        ArrayList<Packet> packets = new ArrayList<>();
        int packetNum=0;
        int msgStart=0;
        int msgEnd=0;
        while(msgStart != message.length()){
            PacketHead head = new PacketHead(packetNum);
            msgEnd = msgStart+(Packet.CHAR_LIMIT-head.getLength());
            String packetBody = message.substring(msgStart, msgEnd);
            msgStart = msgEnd;
            packetNum++;
            try {
                packets.add(new Packet(head,packetBody));
            } catch (PacketSizeException e) {
                e.printStackTrace();
            }
        }

        return packets;
    }
}
