package m5tt.com.smsimagetransfer.SMS;

import android.telephony.SmsManager;
import java.util.List;

import m5tt.com.smsimagetransfer.SMS.Packets.Packet;

/**
 * Static helper class for sending sms packets
 *      - sends ack packets
 *      - sends list of sms content packets
 *
 * @author Matthew Baldwin
 */
public class SMSPacketSender
{
    private static final int SEND_DELAY_MILLIS = 100;
    private static final SmsManager smsManager = SmsManager.getDefault();

    public void sendAckPacket(Packet ackPacket, String contact)
    {
        smsManager.sendTextMessage(contact, null, ackPacket.toString(),
                null, null);
    }

    public void sendMessagePackets(List<Packet> contentPackets, String contact)
    {
        for (Packet contentPacket : contentPackets)
        {
            smsManager.sendTextMessage(contact, null, contentPacket.toString(),
                    null, null);
            try { Thread.sleep(SEND_DELAY_MILLIS); } catch (InterruptedException e) {}
        }
    }
}
