package com.example.joon.contactslist;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.joon.contactslist.Utils.ChangePhotoDialog;
import com.example.joon.contactslist.Utils.Init;
import com.example.joon.contactslist.Utils.UniversalImageLoader;
import com.example.joon.contactslist.models.Contact;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by joon on 11/01/18.
 */

public class EditContactFragment extends Fragment implements ChangePhotoDialog.OnPhotoReceivedListener{

    private static final String TAG = "EditContactFragment";

    //This will evade nullpointer exception when adding to a new bundle from MainActivity
    public EditContactFragment(){
        super();
        setArguments(new Bundle());
    }

    private Contact mContact;
    private EditText mPhoneNumber, mName, mEmail;
    private CircleImageView mContactImage;
    private Spinner mSelectDevice;
    private Toolbar toolbar;
    private String mSelectedImagePath;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editcontact, container, false);
        mPhoneNumber = (EditText) view.findViewById(R.id.etContactPhone);
        mName = (EditText) view.findViewById(R.id.etContactName);
        mEmail = (EditText) view.findViewById(R.id.etContactEmail);
        mContactImage = (CircleImageView) view.findViewById(R.id.contactImage);
        mSelectDevice = (Spinner) view.findViewById(R.id.selectDevice);
        toolbar = (Toolbar) view.findViewById(R.id.editContactToolbar);
        Log.d(TAG, "onCreateView: started");

        //required for setting up toolbar
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        //get the contact from the bundle
        mContact = getContactFromBundle();

        if(mContact != null){
            init();
        }

        //navigation for the backarrow
        ImageView ivBackArrow = (ImageView) view.findViewById(R.id.ivBackArrow);
        ivBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked back arrow");
                //remove previous fragment from the backstack (therefore navigating back)
                getActivity().getSupportFragmentManager().popBackStack();

            }
        });

        //save changes to the contact
        ImageView ivCheckMark = (ImageView) view.findViewById(R.id.ivCheckMark);
        ivCheckMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: saving the edited contact");
                //execute the save method for the database
            }
        });

        //initiate the dialog box for choosing the image
        ImageView ivCamera = (ImageView) view.findViewById(R.id.ivCamera);
        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*
                Make sure all permissions have been verified before opening the dialog
                 */
                for(int i = 0; i < Init.PERMISSIONS.length; i++){
                    String[] permission = {Init.PERMISSIONS[i]};
                    if(((MainActivity)getActivity()).checkPermission(permission)){
                        if(i == Init.PERMISSIONS.length - 1){
                            Log.d(TAG, "onClick: opening the 'image selection dialog box'");
                            ChangePhotoDialog dialog = new ChangePhotoDialog();
                            dialog.show(getFragmentManager(), getString(R.string.change_photo_dialog));
                            dialog.setTargetFragment(EditContactFragment.this, 0);
                        }

                    }else{
                        ((MainActivity)getActivity()).verifyPermission(permission);
                    }
                }
            }
        });


        return view;

    }


    private void init(){
        mPhoneNumber.setText(mContact.getPhonenumber());
        mName.setText(mContact.getName());
        mEmail.setText(mContact.getEmail());
        UniversalImageLoader.setImage(mContact.getProfileimage(), mContactImage, null, "https://");

        //Setting the device to the spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),R.array.device_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSelectDevice.setAdapter(adapter);
        int position = adapter.getPosition(mContact.getDevice());
        mSelectDevice.setSelection(position);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.contact_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuitem_delete:
                Log.d(TAG, "onOptionsItemSelected: deleting contact");

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Retreives the selected contact from the bundle (coming from MainActivity)
     * @return
     */
    private Contact getContactFromBundle(){
        Log.d(TAG, "getContactFromBundle: arguments" + getArguments());

        Bundle bundle = this.getArguments();
        if (bundle != null){
            return bundle.getParcelable(getString(R.string.contact));
        }else{
            return null;
        }
    }

    /**
     * Retrieves the selected image from the bundle (coming from ChangePhotoDialog)
     * @param bitmap
     */
    @Override
    public void getBitmapImage(Bitmap bitmap) {
        Log.d(TAG, "getBitmapImage: got the bitmap: " + bitmap);
        //get the bitmap from 'ChangePhotoDialog'
        if(bitmap != null){
            //compress the image (if you like)
            ((MainActivity)getActivity()).compressBitmap(bitmap, 70);
            mContactImage.setImageBitmap(bitmap);
        }
    }

    @Override
    public void getImagePath(String imagePath) {
        Log.d(TAG, "getImagePath: got the image path: " + imagePath);
        if(!imagePath.equals("")){
            imagePath = imagePath.replace(":/", "://")
            mSelectedImagePath = imagePath;
            UniversalImageLoader.setImage(imagePath, mContactImage, null, "");
        }
    }
}