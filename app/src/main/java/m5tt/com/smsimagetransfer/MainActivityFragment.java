package m5tt.com.smsimagetransfer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;

import java.io.File;
import java.text.DecimalFormat;

import m5tt.com.smsimagetransfer.SMS.OnSMSSendCompleteListener;
import m5tt.com.smsimagetransfer.SMS.OnSMSSendProgressUpdateListener;
import m5tt.com.smsimagetransfer.SMS.SMSSendPackage;
import m5tt.com.smsimagetransfer.SMS.SMSSendProgress;
import m5tt.com.smsimagetransfer.SMS.SMSSendingTask;


public class MainActivityFragment extends Fragment
{
    private static final int REQUEST_CODE_PICKER = 1;

    private View root;
    private EditText phoneNumEditText;
    private TextView fileSizeDisplayTextView;
    private ImageView imagePreviewImageView;

    private File currentUploadedImage;

    public MainActivityFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        root = inflater.inflate(R.layout.fragment_main, container, false);

        phoneNumEditText = root.findViewById(R.id.phoneNum_EditText);
        fileSizeDisplayTextView = root.findViewById(R.id.imageSize_TextView);
        Button uploadImageButton = root.findViewById(R.id.uploadImage_Button);
        imagePreviewImageView = root.findViewById(R.id.imagePreview_ImageView);
        FloatingActionButton sendImageButton = root.findViewById(R.id.send_Button);
        final ProgressBar sendProgressBar = root.findViewById(R.id.sendProgress_ProgressBar);

        uploadImageButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ImagePicker.create(MainActivityFragment.this)
                        .single()
                        .returnAfterFirst(true)
                        .start(REQUEST_CODE_PICKER);
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
                    Toast.makeText(getContext(), "Invalid phone number", Toast.LENGTH_LONG).show();
                    return;
                }

                if (currentUploadedImage == null)
                {
                    Toast.makeText(getContext(), "Please upload an image", Toast.LENGTH_LONG).show();
                    return;
                }
                disableViews();
                imagePreviewImageView.setVisibility(View.INVISIBLE);
                fileSizeDisplayTextView.setVisibility(View.INVISIBLE);
                sendProgressBar.setProgress(0);
                sendProgressBar.setVisibility(View.VISIBLE);

                SMSSendingTask smsSendingTask = new SMSSendingTask();

                smsSendingTask.setOnSMSSendProgressUpdateListener(new OnSMSSendProgressUpdateListener()
                {
                    @Override
                    public void SMSSendProgressUpdate(SMSSendProgress smsSendProgress)
                    {
                        // Update progress bar with current text count / total text count
                        if (sendProgressBar.getMax() <= 0)
                            sendProgressBar.setMax(smsSendProgress.getTotalTextMessages());
                        sendProgressBar.setProgress(smsSendProgress.getCurrentTextMessage());
                    }
                });

                smsSendingTask.setOnSMSSendCompleteListener(new OnSMSSendCompleteListener()
                {
                    @Override
                    public void SMSSendComplete(SMSSendingTask.SMSSendingResult smsSendingResult)
                    {
                        enableViews();
                        sendProgressBar.setVisibility(View.GONE);
                        imagePreviewImageView.setVisibility(View.VISIBLE);
                        fileSizeDisplayTextView.setVisibility(View.VISIBLE);
                        Toast.makeText(getContext(), "Successfully sent image", Toast.LENGTH_LONG).show();
                        // Re-enable views and inform the user that they successfully sent all text messages
                    }
                });

                // Disable views and start execute
                smsSendingTask.execute(new SMSSendPackage(currentUploadedImage, phoneNum));
            }
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_CODE_PICKER && resultCode == Activity.RESULT_OK && data != null)
        {
            Image image = ImagePicker.getImages(data).get(0);
            File imageFile = new File(image.getPath());

            if (imageFile.exists())
            {
                Bitmap imageBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                imagePreviewImageView.setImageBitmap(imageBitmap);
                currentUploadedImage = imageFile;

                DecimalFormat format = new DecimalFormat("##.00");
                fileSizeDisplayTextView.setText(format.format((double)imageFile.length() / 1024.0) + "KB");
                fileSizeDisplayTextView.setVisibility(View.VISIBLE);
            }
        }
    }

    public void disableViews()
    {
        disableView(root);
    }

    public void enableViews()
    {
        enableView(root);
    }

    private void disableView(View view)
    {
        if (view instanceof ViewGroup)
        {
            view.setEnabled(false);
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++)
            {
                disableView(group.getChildAt(i));
            }
        }
        else
            view.setEnabled(false);
    }

    private void enableView(View view)
    {
        if (view instanceof ViewGroup)
        {
            view.setEnabled(true);
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++)
            {
                enableView(group.getChildAt(i));
            }
        }
        else
            view.setEnabled(true);
    }
}
