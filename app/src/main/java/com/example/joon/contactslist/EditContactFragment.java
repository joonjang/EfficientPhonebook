package com.example.joon.contactslist;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.joon.contactslist.Utils.ChangePhotoDialog;
import com.example.joon.contactslist.Utils.DatabaseHelper;
import com.example.joon.contactslist.Utils.Init;
import com.example.joon.contactslist.Utils.UniversalImageLoader;
import com.example.joon.contactslist.models.Contact;

import java.util.ArrayList;
import java.util.List;

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
    private EditText mPhoneNumber, mName, mNote;
    private CircleImageView mContactImage;
    private Spinner mSelectDevice;
    private Toolbar toolbar;
    private String mSelectedImagePath;
//    private String  mTabColor;
    private int mPreviousKeyStroke;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editcontact, container, false);
        mPhoneNumber = (EditText) view.findViewById(R.id.etContactPhone);
        mName = (EditText) view.findViewById(R.id.etContactName);
        mNote = (EditText) view.findViewById(R.id.etContactNote);
        mContactImage = (CircleImageView) view.findViewById(R.id.contactImage);
        mSelectDevice = (Spinner) view.findViewById(R.id.selectDevice);
        toolbar = (Toolbar) view.findViewById(R.id.editContactToolbar);
        Log.d(TAG, "onCreateView: started");

//        //color tabs
//        ImageView mSelectedColorBrown = (ImageView) view.findViewById(R.id.ivCategoryBrown);
//        ImageView mSelectedColorGreen = (ImageView) view.findViewById(R.id.ivCategoryGreen);
//        ImageView mSelectedColorOrange = (ImageView) view.findViewById(R.id.ivCategoryOrange);
//        ImageView mSelectedColorPink = (ImageView) view.findViewById(R.id.ivCategoryPink);
//        ImageView mSelectedColorPurple = (ImageView) view.findViewById(R.id.ivCategoryPurple);
//        ImageView mSelectedColorRed = (ImageView) view.findViewById(R.id.ivCategoryRed);
//        ImageView mSelectedColorTeal = (ImageView) view.findViewById(R.id.ivCategoryTeal);
//        ImageView mSelectedColorYellow = (ImageView) view.findViewById(R.id.ivCategoryYellow);
//
//
//        mSelectedColorBrown.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mTabColor = "Brown";
//                Log.d(TAG, "onClick: Brown clicked");
//                Toast.makeText(getActivity(), "Categorized As Brown", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        //#4CAF50
//        mSelectedColorGreen.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mTabColor = "Green";
//                Log.d(TAG, "onClick: Green clicked");
//                Toast.makeText(getActivity(), "Categorized As Green", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        //#FF9800
//        mSelectedColorOrange.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mTabColor = "Orange";
//                Log.d(TAG, "onClick: Orange clicked");
//                Toast.makeText(getActivity(), "Categorized As Orange", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        //#E91E63
//        mSelectedColorPink.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mTabColor = "Pink";
//                Log.d(TAG, "onClick: Pink clicked");
//                Toast.makeText(getActivity(), "Categorized As Pink", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        //#9C27B0
//        mSelectedColorPurple.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mTabColor = "Purple";
//                Log.d(TAG, "onClick: Purple clicked");
//                Toast.makeText(getActivity(), "Categorized As Purple", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        //#F44336
//        mSelectedColorRed.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mTabColor = "Red";
//                Log.d(TAG, "onClick: Red clicked");
//                Toast.makeText(getActivity(), "Categorized As Red", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        //#009688
//        mSelectedColorTeal.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mTabColor = "Teal";
//                Log.d(TAG, "onClick: Teal clicked");
//                Toast.makeText(getActivity(), "Categorized As Teal", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        //#FFEB3B
//        mSelectedColorYellow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mTabColor = "Yellow";
//                Log.d(TAG, "onClick: Yellow clicked");
//                Toast.makeText(getActivity(), "Categorized As Yellow", Toast.LENGTH_SHORT).show();
//            }
//        });


        mSelectedImagePath = null;

        //set the heading for the toolbar
        TextView heading = (TextView) view.findViewById(R.id.textContactToolbar);
        heading.setText(R.string.edit_contact);

        //required for setting up toolbar
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        //get the contact from the bundle
        mContact = getContactFromBundle();

        if(mContact != null){
            init();
        }

        //navigation for the backarrow
        //consider auto saving when backing out, or a warning dialog
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

                if(checkStringIfNull(mName.getText().toString())){
                    Log.d(TAG, "onClick: saving changes to the contact " + mName.getText().toString());

                    //get the database helper and save the contact
                    DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
                    Cursor cursor =  databaseHelper.getContactID(mContact);

                    int contactID = -1;
                    while (cursor.moveToNext()){
                        contactID =  cursor.getInt(0);
                    }
                    if(contactID > -1){
                        if(mSelectedImagePath != null){
                            mContact.setProfileimage(mSelectedImagePath);
                        }
                        mContact.setName(mName.getText().toString());
                        mContact.setPhonenumber(mPhoneNumber.getText().toString());
                        mContact.setDevice(mSelectDevice.getSelectedItem().toString());
                        mContact.setNote(mNote.getText().toString());
//                        mContact.setTabcolour(null);

                        databaseHelper.updateContact(mContact, contactID);
                        Toast.makeText(getActivity(), "Contact Updated", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    }

                }
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

        initOnTextChangeListener();


        return view;

    }

    private boolean checkStringIfNull(String string){
        if(string.equals("")){
            return false;
        }else{
            return true;
        }
    }


    private void init(){
        mPhoneNumber.setText(mContact.getPhonenumber());
        mName.setText(mContact.getName());
        mNote.setText(mContact.getNote());
        UniversalImageLoader.setImage(mContact.getProfileimage(), mContactImage, null, "");

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
                DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
                Cursor cursor = databaseHelper.getContactID(mContact);
                int contactID = -1;
                while (cursor.moveToNext()){
                    contactID =  cursor.getInt(0);
                }
                if(contactID > -1){
                    if(databaseHelper.deleteContact(contactID) > 0){
                        Toast.makeText(getActivity(), "Contact deleted", Toast.LENGTH_SHORT).show();

                        //clear the arguments on the current bundle since the contact is deleted
                        this.getArguments().clear();

                        //remove previous fragment from the backstack (therefore navigating back)
                        getActivity().getSupportFragmentManager().popBackStack();
                    }else{
                        Toast.makeText(getActivity(), "Could not delete contact", Toast.LENGTH_SHORT).show();
                    }
                }

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
            imagePath = imagePath.replace(":/", "://");
            mSelectedImagePath = imagePath;
            UniversalImageLoader.setImage(imagePath, mContactImage, null, "");
        }
    }
    /*
    * Initialize the onTextChangeListener for formatting the phonenumber
    */
    private void initOnTextChangeListener(){

        mPhoneNumber.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                mPreviousKeyStroke = keyCode;

                return false;
            }
        });


        mNote.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                mPreviousKeyStroke = keyCode;

                return false;
            }
        });

        // add bullet "•" per line
        mNote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String words = s.toString();
                Log.d(TAG, "beforeTextChanged: words "+ s);

                List<String> myWordList = new ArrayList<String>();
                myWordList.add(s.toString());

                Log.d(TAG, "afterTextChanged: myWord " + myWordList);
                char charWord = myWordList.toString().charAt(s.toString().length());

                if(charWord == '\n' && mPreviousKeyStroke != KeyEvent.KEYCODE_DEL){
                    words = String.format("%s• ", s.toString());
                    mNote.setText(words);
                    mNote.setSelection(words.length());
                }


                if(words.length()==1 && mPreviousKeyStroke != KeyEvent.KEYCODE_DEL
                        && !words.contains("•")){
                    words = String.format("• %s", s.toString());
                    mNote.setText(words);
                    mNote.setSelection(words.length());
                }


            }
        });


        mPhoneNumber.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String number = s.toString();
                Log.d(TAG, "afterTextChanged: " + number);

                if(number.length()==3 && mPreviousKeyStroke != KeyEvent.KEYCODE_DEL
                        && !number.contains("(")){
                    number = String.format("(%s", s.toString().substring(0,3));
                    mPhoneNumber.setText(number);
                    mPhoneNumber.setSelection(number.length());
                }
                else if(number.length() == 5  && mPreviousKeyStroke != KeyEvent.KEYCODE_DEL
                        && !number.contains(")")){
                    number = String.format("(%s) %s",
                            s.toString().substring(1,4),
                            s.toString().substring(4,5));
                    mPhoneNumber.setText(number);
                    mPhoneNumber.setSelection(number.length());
                }
                else if(number.length()==10 && mPreviousKeyStroke != KeyEvent.KEYCODE_DEL
                        && !number.contains("-")){
                    number = String.format("(%s) %s-%s",
                            s.toString().substring(1,4),
                            s.toString().substring(6,9),
                            s.toString().substring(9,10));
                    mPhoneNumber.setText(number);
                    mPhoneNumber.setSelection(number.length());

                }

            }
        });
    }


}
