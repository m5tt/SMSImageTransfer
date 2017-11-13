package m5tt.com.smsimagetransfer.SMS.Packets;

import android.preference.PreferenceActivity;

import java.text.ParseException;

/**
 * Packet metadata indicating packet number of count, as well as a time-based unique identifier
 * EXAMPLE: '_'1'_'
 */

class PacketHead {

    public static int VERSION = 1;

    // The marker for the start of the packet head
    public static final String beginMarker = "'-'";

    // The marker for the end of the packet head
    public static final String endMarker = "'-'";

    // Delimiter for separate header elements. Currently unused
    private static final String delimiter = "\\";

    // The packet's number. Negative for control packets
    private int packetNum;

    private String stringRepresentation;

    PacketHead(int packetNum){
        this.packetNum = packetNum;
        this.stringRepresentation = beginMarker + packetNum + endMarker;
    }

    // The packet number. Negative for control packets
    public int getPacketNum(){
        return this.packetNum;
    }

    @Override
    public String toString(){
        return stringRepresentation;
    }

    // Get the number of characters used by the head
    int getLength(){
        return stringRepresentation.length();
    }

    /**
     * Parse a packet head from a string
     * @param head The packet head as a string
     * @return The packet head
     * @throws ParseException if the string is not a packet head
     */
    static PacketHead parse(String head) throws ParseException {
        if(!isPacketHead(head))
            throw new ParseException("Invalid Packet Head", 0);
        int packetNum = Integer.parseInt(head.substring(beginMarker.length(),head.length()-endMarker.length()));
        return new PacketHead(packetNum);
    }

    /**
     * Check if the string is a packet head
     * @param head the candidate string head
     * @return True if is a packet head, false otherwise
     */
    private static boolean isPacketHead(String head){
        // Check header
        return head.matches("^"+beginMarker+"-?\\d+"+endMarker+"$");
    }
}
