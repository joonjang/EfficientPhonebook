package com.example.joon.contactslist;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joon.contactslist.Utils.ContactListAdapter;
import com.example.joon.contactslist.Utils.DatabaseHelper;
import com.example.joon.contactslist.models.Contact;
import com.jaychang.srv.SimpleRecyclerView;
import com.jaychang.srv.decoration.SectionHeaderProvider;
import com.jaychang.srv.decoration.SimpleSectionHeaderProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

/**
 * Created by joon on 06/01/18.
 */

public class ViewContactsFragment extends Fragment{


    private static final String TAG = "ViewContactsFragment";


    public interface OnContactSelectedListener{
        public void OnContactSelected(Contact con);
    }
    OnContactSelectedListener mContactListener;

    public interface OnAddContactListener{
        public void onAddContact();
    }
    OnAddContactListener mOnAddContact;


    //variables and widgets
    private static final int STANDARD_APPBAR = 0;
    private static final int SEARCH_APPBAR = 1;
    private int mAppBarState;

    private AppBarLayout viewContactsBar, searchBar;
    private ContactListAdapter adapter;
    private ListView contactsList;
    private EditText mSearchContacts;
//    private SimpleRecyclerView simpleRecyclerView;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_viewcontacts, container, false);
        viewContactsBar = (AppBarLayout) view.findViewById(R.id.viewContactsToolbar);
        searchBar = (AppBarLayout) view.findViewById(R.id.searchToolbar);

        ///////////////////////////////////////////
        contactsList = (ListView) view.findViewById(R.id.contactsList);
        ///////////////////////////////////////////

        mSearchContacts = (EditText) view.findViewById(R.id.etSearchContacts);
        Log.d(TAG, "onCreateView: started");

        setAppBarState(STANDARD_APPBAR);

        setupContactsList();

        //make floating action button clickable and do something
        // navigate to add contacts fragment
        // due to being a fragment, findViewById is called with view in the front
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fabAddContact);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked fab");
                mOnAddContact.onAddContact();
            }
        });

        //imageview widget
        //widget allow buttons to have an action behind them
        ImageView ivSearchContact = (ImageView) view.findViewById(R.id.ivSearchIcon);
        ivSearchContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked search icon");
                toggleToolbarState();
            }
        });

        ImageView ivBackArrow = (ImageView) view.findViewById(R.id.ivBackArrow);
        ivBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked back arrow");
                toggleToolbarState();

            }
        });

//        // SimpleRecyclerView
//        simpleRecyclerView = (SimpleRecyclerView) view.findViewById(R.id.contactsList);
//        this.addRecyclerHeaders();
//        this.bindData();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mContactListener = (OnContactSelectedListener) getActivity();
            mOnAddContact = (OnAddContactListener) getActivity();
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException: " + e.getMessage() );
        }
    }

    private void setupContactsList(){
        final ArrayList<Contact> contacts = new ArrayList<>();

        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
        Cursor cursor = databaseHelper.getAllContacts();

        //Iterate through all the rows contained in the database
        if(!cursor.moveToNext()){
            Toast.makeText(getActivity(), "There are no contacts to show", Toast.LENGTH_SHORT).show();
        }

        while(cursor.moveToNext()){
            contacts.add(new Contact(
                    cursor.getString(1), //name
                    cursor.getString(2), //phone number
                    cursor.getString(3), //device
                    cursor.getString(4), //note
                    cursor.getString(5) //profile image uri
//                    cursor.getString(6)  //get color tab
            ));
        }

//        Log.d(TAG, "setupContactsList: image url: " + contacts.get(0).getProfileimage());

        //<--- sort the arraylist based on contact name OR ANYTHING ELSE --->
        Collections.sort(contacts, new Comparator<Contact>() {
            @Override
            public int compare(Contact o1, Contact o2) {
                return o1.getName().compareToIgnoreCase(o2.getName());
            }
        });
        // <-- change this to sort based on name or category, make an if statement with buttons of desired category type -->

        adapter = new ContactListAdapter(getActivity(), R.layout.layout_contactslistitem, contacts, "");

        mSearchContacts.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = mSearchContacts.getText().toString().toLowerCase(Locale.getDefault());
                adapter.filter(text);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        contactsList.setAdapter(adapter);

        contactsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Log.d(TAG, "onClick: navigating to contact "+getString(R.string.contact_fragment));

                // what is ".get"
                //pass the contact to the interface and send it to MainActivity
                mContactListener.OnContactSelected(contacts.get(position));
            }
        });



    }

    /**
     * Initiates the appbar state toggles
     */

    private void toggleToolbarState() {
        Log.d(TAG, "toggleToolbarState: toggling AppBarState");
        if(mAppBarState == STANDARD_APPBAR) {
            setAppBarState(SEARCH_APPBAR);
        }else{
            setAppBarState(STANDARD_APPBAR);
        }
    }

    /**
     * Sets the appbar state for either 'search' mode or 'standard' mode
     * @param state
     */

    //logic for hiding app bar
    private void setAppBarState(int state) {
        Log.d(TAG, "setAppBarState: changing app bar state to:" + state);

        mAppBarState = state;
        if(mAppBarState == STANDARD_APPBAR){
            searchBar.setVisibility(View.GONE);
            viewContactsBar.setVisibility(View.VISIBLE);
            //hide keyboard
            View view = getView();
            // what is input method manager
            //what is null pointer exception
            //what is all this?!
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            //potential errors
            try{
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

            }catch (NullPointerException e){
                Log.d(TAG, "setAppBarState: NullPointerException" + e.getMessage());
            }
        }
        else if(mAppBarState == SEARCH_APPBAR){
            viewContactsBar.setVisibility(View.GONE);
            searchBar.setVisibility(View.VISIBLE);

            //open the keyboard
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }

    //if back button is called, the app resumes in standard appbar state
    @Override
    public void onResume() {
        super.onResume();
        setAppBarState(STANDARD_APPBAR);
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

//    //Bind data to recycler view
//    private  void bindData(){
//
//        final ArrayList<Contact> contacts = new ArrayList<>();
//
//        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
//        Cursor cursor = databaseHelper.getAllContacts();
//
//
//    }
}






