package com.example.joon.contactslist.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.joon.contactslist.R;

import java.io.File;

/**
 * Created by joon on 21/01/18.
 */

public class ChangePhotoDialog extends DialogFragment {

    private static final String TAG = "ChangePhotoDialog";

    public interface OnPhotoReceivedListener{
        public void getBitmapImage(Bitmap bitmap);
        public void getImagePath(String imagePath);
    }
    OnPhotoReceivedListener mOnPhotoReceived;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_changephoto, container, false);

        //initialize the textview for starting the camera
        TextView takePhoto = (TextView) view.findViewById(R.id.dialogTakePhoto);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: starting camera");
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, Init.CAMERA_REQUEST_CODE);
            }
        });

        //initialize the textview for choosing an image from memory
        TextView selectPhoto = (TextView) view.findViewById(R.id.dialogChoosePhoto);
        selectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: accessing phone memory");
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, Init.PICKFILE_REQUEST_CODE);

            }
        });

        //initialize button for closing the dialog
        TextView cancelDialog = (TextView) view.findViewById(R.id.dialogCancel);
        cancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: closing dialog");
                getDialog().dismiss();

            }
        });


        return view;
    }

    //Going from fragment to fragment
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mOnPhotoReceived = (OnPhotoReceivedListener) getTargetFragment();
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException " + e.getMessage() );
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*
        Results whens taking a new image with camera
         */
        if(requestCode == Init.CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            Log.d(TAG, "onActivityResult: done taking a picture");
            //get the new image bitmap
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            Log.d(TAG, "onActivityResult: received bitmap: " + bitmap);

            //send the bitmap and fragment to the interface
            mOnPhotoReceived.getBitmapImage(bitmap);
            getDialog().dismiss();
        }

        /*
        Results when selecting new image from memory
         */
        if(requestCode == Init.PICKFILE_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            Uri selectedImageUri = data.getData();
            File file = new File(selectedImageUri.toString());
            Log.d(TAG, "onActivityResult: images: " + file.getPath());

            //send the bitmap and fragment to the interface
            mOnPhotoReceived.getImagePath(file.getPath());
            getDialog().dismiss();

        }
    }
}
