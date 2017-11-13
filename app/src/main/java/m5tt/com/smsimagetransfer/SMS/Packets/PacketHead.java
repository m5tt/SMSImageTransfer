package m5tt.com.smsimagetransfer.SMS.Packets;

import android.preference.PreferenceActivity;

import java.text.ParseException;

/**
 * Packet metadata indicating packet number of count, as well as a time-based unique identifier
 * EXAMPLE: '_'1'_'
 */

class PacketHead {
    public static final String beginMarker = "'-'";
    public static final String endMarker = "'-'";
    private static final String delimiter = "\\";
    private int packetNum;

    private String stringRepresentation;

    PacketHead(int packetNum){
        this.packetNum = packetNum;
        this.stringRepresentation = beginMarker + packetNum + endMarker;
    }

    public int getPacketNum(){
        return this.packetNum;
    }

    @Override
    public String toString(){
        return stringRepresentation;
    }

    public int getLength(){
        return stringRepresentation.length();
    }

    protected static PacketHead parse(String head) throws ParseException {
        if(!isPacketHead(head))
            throw new ParseException("Invalid Packet Head", 0);
        int packetNum = Integer.parseInt(head.substring(beginMarker.length(),head.length()-endMarker.length()));
        return new PacketHead(packetNum);
    }

    public static boolean isPacketHead(String head){
        // Check header
        return head.matches("^"+beginMarker+"-?\\d+"+endMarker+"$");
    }
}
