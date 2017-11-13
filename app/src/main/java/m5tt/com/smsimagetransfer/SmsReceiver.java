package m5tt.com.smsimagetransfer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import m5tt.com.smsimagetransfer.SMS.Packets.Packet;

/**
 * @author Matthew Baldwin
 */

public class SmsReceiver extends BroadcastReceiver
{
    private static Map<String, List<Packet>> messages = new HashMap<>();

    @Override
    public void onReceive(Context context, Intent intent)
    {
        // TODO: make sure notifications are suppressed, dealt with properly

        if (intent == null || ! intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED"))
            return;

        SmsMessage[] receivedMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent);

        for (SmsMessage smsMessage : receivedMessages)
        {
            String contact = smsMessage.getOriginatingAddress();
            String message = smsMessage.getMessageBody();

            if (! Packet.isPacket(message))
                // TODO: raise an event to raise notification here i suppose
                return;

            try
            {
                Packet packet = Packet.parse(message);

                switch (packet.getType())
                {
                    case MESSAGE_START:
                        messages.put(contact, new ArrayList<Packet>());
                        // TODO: send ACK
                        break;
                    case MESSAGE_ACK:
                        // TODO: check ACK was success, confirm other metadata is good
                        // TODO: start OnTransferStart - there will have to be a temp service running
                        break;
                    case MESSAGE_CONTENT:
                        messages.get(contact).add(packet);
                        break;
                    case MESSAGE_END:
                        // TODO: decode and store
                        // TODO: remove actual sms messages - longterm
                        messages.remove(contact);
                        break;
                }
            } catch (ParseException e) {}     // never going to happen because of check
        }
    }
}
