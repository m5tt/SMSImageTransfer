package m5tt.com.smsimagetransfer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.angads25.filepicker.controller.DialogSelectionListener;
import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;

import java.io.File;

import m5tt.com.smsimagetransfer.SMS.SMSInputOutput;
import m5tt.com.smsimagetransfer.SMS.SMSPacketSender;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment
{
    private File currentUploadedImage;

    public MainActivityFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        final EditText phoneNumEditText = root.findViewById(R.id.phoneNum_EditText);
        Button uploadImageButton = root.findViewById(R.id.uploadImage_Button);
        final ImageView imagePreviewImageView = root.findViewById(R.id.imagePreview_ImageView);
        Button sendImageButton = root.findViewById(R.id.send_Button);

        uploadImageButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DialogProperties properties = new DialogProperties();

                properties.selection_mode = DialogConfigs.SINGLE_MODE;
                properties.selection_type = DialogConfigs.FILE_SELECT;
                properties.root = new File(DialogConfigs.DEFAULT_DIR);
                properties.error_dir = new File(DialogConfigs.DEFAULT_DIR);
                properties.offset = new File(DialogConfigs.DEFAULT_DIR);
                properties.extensions = new String[]{".jpg", ".png", ".jpeg", ".bmp"};

                FilePickerDialog dialog = new FilePickerDialog(getContext(), properties);
                dialog.setTitle("Select an Image");

                dialog.setDialogSelectionListener(new DialogSelectionListener()
                {
                    @Override
                    public void onSelectedFilePaths(String[] files)
                    {
                        String filePath = files[0]; // Single file select mode
                        File imageFile = new File(filePath);

                        if (imageFile.exists())
                        {
                            Bitmap imageBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                            imagePreviewImageView.setImageBitmap(imageBitmap);
                            currentUploadedImage = imageFile;
                        }
                    }
                });

                dialog.show();
            }
        });

        sendImageButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String phoneNum = phoneNumEditText.getText().toString();

                if (phoneNum.equals(""))
                {
                    Toast.makeText(getContext(), "Invalid Phone Number", Toast.LENGTH_LONG).show();
                    return;
                }

                if (currentUploadedImage == null)
                {
                    Toast.makeText(getContext(), "Please Upload an Image", Toast.LENGTH_LONG).show();
                    return;
                }

                SMSInputOutput fileConverter = new SMSInputOutput();
                SMSPacketSender packetSender = new SMSPacketSender();

                packetSender.sendMessagePackets(fileConverter.fileToPackets(currentUploadedImage), phoneNum);
            }
        });

        return root;
    }
}
