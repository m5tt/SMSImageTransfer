package m5tt.com.smsimagetransfer.SMS;

/**
 * Created by Mark on 13-Nov-2017.
 */

public class SMSSendProgress
{
    private int totalTextMessages;
    private int currentTextMessage;

    public SMSSendProgress(int totalTextMessages)
    {
        this.totalTextMessages = totalTextMessages;
        this.currentTextMessage = 0;
    }

    public void incrementTexts()
    {
        this.currentTextMessage++;
    }

    public int getTotalTextMessages()
    {
        return totalTextMessages;
    }

    public int getCurrentTextMessage()
    {
        return currentTextMessage;
    }
}
