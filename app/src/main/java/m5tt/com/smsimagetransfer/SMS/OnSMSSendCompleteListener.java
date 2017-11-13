package m5tt.com.smsimagetransfer.SMS;

/**
 * Created by Mark on 13-Nov-2017.
 */

public interface OnSMSSendCompleteListener
{
    void SMSSendComplete(SMSSendingTask.SMSSendingResult smsSendingResult);
}
