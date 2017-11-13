package m5tt.com.smsimagetransfer.SMS;

import java.io.File;

/**
 * Created by Mark on 13-Nov-2017.
 */

public class SMSSendPackage
{
    private File file;
    private String phoneNum;

    public SMSSendPackage(File file, String phoneNum)
    {
        this.file = file;
        this.phoneNum = phoneNum;
    }

    public File getFile()
    {
        return file;
    }

    public String getPhoneNum()
    {
        return phoneNum;
    }
}
