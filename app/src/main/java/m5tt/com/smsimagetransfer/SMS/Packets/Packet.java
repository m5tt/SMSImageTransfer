package m5tt.com.smsimagetransfer.SMS.Packets;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An SMS Packet, containing a header for order purposes, and a body of text containing data to transfer
 */

public class Packet {

    public enum PacketType { MESSAGE_START, MESSAGE_END, MESSAGE_ACK, MESSAGE_CONTENT}

    // The size limit for an SMS
    public static final int CHAR_LIMIT=160;

    // The metadata for the packet
    private PacketHead head;

    // The packet data
    private String body;

    // The packet as a string
    private String stringRepresentation;

    private PacketType type;

    private Packet(){} // Disable default constructor

    // Packet constructor
    protected Packet(PacketHead head, String body) throws PacketSizeException {
        this.head = head;
        this.body = body;
        this.type = PacketType.MESSAGE_CONTENT;
        if(head.getLength() + body.length() > CHAR_LIMIT){
            throw new PacketSizeException();
        }
    }

    public String getBody(){
        return this.body;
    }

    public PacketType getType(){
        return this.type;
    }

    @Override
    public String toString(){
        return stringRepresentation;
    }

    /**
     * Parse a message string into a packet
     * @param message The SMS message packet as String
     * @return The packet
     * @throws ParseException If the supplied string is not an SMS packet
     */
    public static Packet parse(String message) throws ParseException {
        if(!isPacket(message))
            throw new ParseException("Message is not a packet",0);

        // Use REGEX to extract the head
        Pattern pattern = Pattern.compile("^'_'\\d+'_'");
        Matcher matcher = pattern.matcher(message);

        // Parse the head
        PacketHead head = PacketHead.parse(matcher.group(0));

        // Get the body
        String packetBody = message.substring(head.getLength(),message.length());

        try {
            Packet packet = new Packet(head, packetBody);
            return packet;
        }
        catch (PacketSizeException e) {
            throw new ParseException("Message is not a packet", 0);
        }
    }

    public static boolean isPacket(String message){
        // Check header
        return message.matches("^'_'-?\\d+'_'");
    }
}
