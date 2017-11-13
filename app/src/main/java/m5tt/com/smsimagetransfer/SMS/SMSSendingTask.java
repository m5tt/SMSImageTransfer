package m5tt.com.smsimagetransfer.SMS;

import android.os.AsyncTask;

import java.io.File;

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

        packetSender.sendMessagePackets(fileConverter.fileToPackets(file), phoneNum);

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
