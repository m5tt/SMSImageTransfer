package m5tt.com.smsimagetransfer.SMS.Packets;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An SMS Packet, containing a header for order purposes, and a body of text containing data to transfer
 */

public class Packet {

    public static int VERSION = 1;

    public enum PacketType { MESSAGE_START, MESSAGE_END, MESSAGE_ACK, MESSAGE_CONTENT}

    // The size limit for an SMS
    public static final int CHAR_LIMIT=160;

    // The metadata for the packet
    private PacketHead head;

    // The packet data
    private String body;

    // The packet's use in the data transmission
    private PacketType type;

    private Packet(){} // Disable default constructor

    /**
     * Packet constructor
     * @param head The message metadata head
     * @param body The message body
     * @throws PacketSizeException If the packet is greater than the SMS limit of 160 chars
     */
    Packet(PacketHead head, String body) throws PacketSizeException {
        this.head = head;
        this.body = body;
        this.type = PacketType.MESSAGE_CONTENT;
        if(head.getLength() + body.length() > CHAR_LIMIT){
            throw new PacketSizeException();
        }
    }

    /**
     * Get the packet content
     * @return The packet message part
     */
    public String getBody(){
        return this.body;
    }

    /**
     * Get the packet type
     * @return The packet type
     */
    public PacketType getType(){
        return this.type;
    }

    /**
     * Set the packet type
     * @param type The packet type
     * @return The packet
     */
    Packet setType(PacketType type){
        this.type = type;
        return this;
    }

    /**
     * Get the packet string representation for sending as SMS
     * @return The packet as a string
     */
    @Override
    public String toString(){
        return head.toString()+ body;
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
            // Create the new packet and set it's type;
            return new Packet(head, packetBody).setType(packetTypeFromNum(head.getPacketNum()));

        }
        catch (PacketSizeException e) {
            throw new ParseException("Message is not a packet", 0);
        }
    }


    /**
     * Check if a message is a packet
     * @param message The sms message to verify
     * @return True if it is a packet, false otherwise
     */
    public static boolean isPacket(String message){
        // Check header
        return message.matches("^'_'-?\\d+'_'");
    }

    /**
     * Don't kill me Matt.
     * Get the packet type from the packet number. Used so as to not bloat the ENUM
     * Negative numbers used to represent non-message types to minimize header size
     * @return
     */
    public static PacketType packetTypeFromNum(int val){
        switch (val){
            case -1:
                return PacketType.MESSAGE_START;
            case -2:
                return PacketType.MESSAGE_END;
            case -3:
                return PacketType.MESSAGE_ACK;
            default:
                return PacketType.MESSAGE_CONTENT;
        }
    }

    public static int packetTypeValueOf(PacketType type){
        switch (type){
            case MESSAGE_START:
                return -1;
            case MESSAGE_END:
                return -2;
            case MESSAGE_ACK:
                return -3;
            default:
                return 0;
        }
    }
}
