package m5tt.com.smsimagetransfer.SMS;

import android.os.AsyncTask;

import java.io.File;
import java.util.List;

import m5tt.com.smsimagetransfer.SMS.Packets.Packet;

/**
 * Created by Mark on 13-Nov-2017.
 */

public class SMSSendingTask extends AsyncTask<SMSSendPackage, SMSSendProgress, SMSSendingTask.SMSSendingResult>
{
    private OnSMSSendProgressUpdateListener onSMSSendProgressUpdateListener;
    private OnSMSSendCompleteListener onSMSSendCompleteListener;

    public enum SMSSendingResult
    {
        SUCCESS,
        TIMED_OUT,
        FAILED
    }

    public void setOnSMSSendProgressUpdateListener(OnSMSSendProgressUpdateListener onSMSSendProgressUpdateListener)
    {
        this.onSMSSendProgressUpdateListener = onSMSSendProgressUpdateListener;
    }

    public void setOnSMSSendCompleteListener(OnSMSSendCompleteListener onSMSSendCompleteListener)
    {
        this.onSMSSendCompleteListener = onSMSSendCompleteListener;
    }

    @Override
    protected SMSSendingResult doInBackground(SMSSendPackage... smsSendPackages)
    {
        File file = smsSendPackages[0].getFile();
        String phoneNum = smsSendPackages[0].getPhoneNum();

        SMSInputOutput fileConverter = new SMSInputOutput();
        SMSPacketSender packetSender = new SMSPacketSender();

        // Handle ack packet stuff

        List<Packet> packetList = fileConverter.fileToPackets(file);

        SMSSendProgress smsSendProgress = new SMSSendProgress(packetList.size());

        for (int i = 0; i < packetList.size(); i++)
        {
            packetSender.sendMessagePacket(packetList.get(i), phoneNum);
            smsSendProgress.incrementTexts();
            publishProgress(smsSendProgress);

            try
            {
                Thread.sleep(SMSPacketSender.SEND_DELAY_MILLIS);
            }
            catch (InterruptedException e)
            {
                return SMSSendingResult.FAILED;
            }
        }
        smsSendProgress.incrementTexts();
        publishProgress(smsSendProgress);

        return SMSSendingResult.SUCCESS;
    }

    @Override
    protected void onProgressUpdate(SMSSendProgress... smsSendProgresses)
    {
        if (onSMSSendProgressUpdateListener != null)
            onSMSSendProgressUpdateListener.SMSSendProgressUpdate(smsSendProgresses[0]);
    }

    @Override
    protected void onPostExecute(SMSSendingResult smsSendingResult)
    {
        if (onSMSSendCompleteListener != null)
            onSMSSendCompleteListener.SMSSendComplete(smsSendingResult);
    }
}
