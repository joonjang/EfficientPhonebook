package com.example.joon.contactslist;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.joon.contactslist.Utils.UniversalImageLoader;
import com.example.joon.contactslist.ViewContactsFragment.OnAddContactListener;
import com.example.joon.contactslist.models.Contact;
import com.jaychang.srv.SimpleRecyclerView;
import com.jaychang.srv.decoration.SectionHeaderProvider;
import com.jaychang.srv.decoration.SimpleSectionHeaderProvider;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity implements
        ViewContactsFragment.OnContactSelectedListener,
        ContactFragment.OnEditContactListener,
        OnAddContactListener {

    SimpleRecyclerView simpleRecyclerView;

    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE = 1;

    @Override
    public void onEditContactSelected(Contact contact) {
        Log.d(TAG, "OnContactSelected: contact selected from "
                + getString(R.string.edit_contact_fragment)
                + " " + contact.getName());

        EditContactFragment fragment = new EditContactFragment();

        //what is a bundle

        Bundle args = new Bundle();
        args.putParcelable(getString(R.string.contact), contact);
        fragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(getString(R.string.edit_contact_fragment));
        transaction.commit();
    }

    @Override
    public void onAddContact() {
        Log.d(TAG, "onAddContact: navigating to " + getString(R.string.add_contact_fragment));

        AddContactFragment fragment = new AddContactFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(getString(R.string.add_contact_fragment));
        transaction.commit();
    }


    @Override
    public void OnContactSelected(Contact contact) {
        Log.d(TAG, "OnContactSelected: contact selected from "
                + getString(R.string.view_contacts_fragment)
                + " " + contact.getName());

        ContactFragment fragment = new ContactFragment();

        //what is a bundle

        Bundle args = new Bundle();
        args.putParcelable(getString(R.string.contact), contact);
        fragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(getString(R.string.contact_fragment));
        transaction.commit();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: started");

        initImageLoader();
        init();
//        simpleRecyclerView=findViewById(R.id.contactsList);
//        this.addRecyclerHeaders();

    }

    /**
     * initialize the first fragment (ViewContactsFragment)
     */

    private void init() {
        ViewContactsFragment fragment = new ViewContactsFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // replace whatever is in this fragment_container view with this fragment
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container, fragment);
        // back stack is pressing the back on android (now you know how the back button works, wow!)
        transaction.addToBackStack(null);
        transaction.commit();
    }


    private void initImageLoader() {
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(MainActivity.this);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }

    /**
     * Compress a bitmap by the @param "quality"
     * Quality can be anywehre from 1-100 : 100 being the highest quality
     *
     * @param bitmap
     * @param quality
     * @return
     */
    public Bitmap compressBitmap(Bitmap bitmap, int quality) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);
        return bitmap;
    }

    /**
     * Generalized method for asking permission. Can pass any array of permission
     *
     * @param permissions
     */

    //checking a list of permission which will result in a dialogue box asking for permissions
    public void verifyPermission(String[] permissions) {
        Log.d(TAG, "verifyPermission: asking user for permission");
        ActivityCompat.requestPermissions(
                MainActivity.this,
                permissions,
                REQUEST_CODE
        );
    }

    /**
     * Checks to see if permissionw as granted for the passed parameters
     * ONLY ONE PERMISSION MAY BE CHECKED AT A TIME
     *
     * @param permission
     * @return
     */
    //in background of whether a single permission has been accepted
    public boolean checkPermission(String[] permission) {
        Log.d(TAG, "checkPermission: checking permission for: " + permission[0]);
        int permissionRequest = ActivityCompat.checkSelfPermission(
                MainActivity.this,
                permission[0]);

        if (permissionRequest != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "checkPermission: \n Permissions was not granted for: " + permission[0]);
            return false;
        } else {
            return true;
        }
    }

    //runs everytime a permission is granted or denied
    //catches all permissions ran in the background
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: requestCode: " + requestCode);

        switch (requestCode) {
            case REQUEST_CODE:
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "onRequestPermissionsResult: User has allowed permissions to access:" + permissions[i]);
                    } else {
                        break;
                    }
                }
                break;
        }
    }

//    //Recycler Header, COLOUR TAB HEADER
//    private  void addRecyclerHeaders(){
//        SectionHeaderProvider<Contact> sh = new SimpleSectionHeaderProvider<Contact>() {
//            @NonNull
//            @Override
//            public View getSectionHeaderView(@NonNull Contact item, int position) {
//                LayoutInflater inflater = getLayoutInflater();
//                View view = inflater.inflate(R.layout.snippet_header, null, false);
//                TextView textView = view.findViewById(R.id.tvHeaderTxt);
//                textView.setText(item.getTabcolour());
//                return view;
//            }
//
//            @Override
//            public boolean isSameSection(@NonNull Contact item, @NonNull Contact nextItem) {
//                return item.getTabcolour() == nextItem.getTabcolour();
//            }
//
//            public boolean isStick(){
//                return true;
//            }
//        };
//        simpleRecyclerView.setSectionHeader(sh);
//    }


}
