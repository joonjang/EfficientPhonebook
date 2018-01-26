package com.example.joon.contactslist;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.joon.contactslist.Utils.ContactListAdapter;
import com.example.joon.contactslist.models.Contact;

import java.util.ArrayList;

/**
 * Created by joon on 06/01/18.
 */

public class ViewContactsFragment extends Fragment{

    //delete this once debugged
    private String testImageURL = "i.pinimg.com/564x/1f/58/fa/1f58fa2acb4b32bfdfd5ae2c683d2822--sara-underwood-charcoal-art.jpg";
    private static final String TAG = "ViewContactsFragment";


    public interface OnContactSelectedListener{
        public void OnContactSelected(Contact con);
    }
    OnContactSelectedListener mContactListener;





    //variables and widgets
    private static final int STANDARD_APPBAR = 0;
    private static final int SEARCH_APPBAR = 1;
    private int mAppBarState;

    private AppBarLayout viewContactsBar, searchBar;
    private ContactListAdapter adapter;
    private ListView contactsList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_viewcontacts, container, false);
        viewContactsBar = (AppBarLayout) view.findViewById(R.id.viewContactsToolbar);
        searchBar = (AppBarLayout) view.findViewById(R.id.searchToolbar);
        contactsList = (ListView) view.findViewById(R.id.contactsList);
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

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mContactListener = (OnContactSelectedListener) getActivity();
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException: " + e.getMessage() );
        }
    }

    private void setupContactsList(){
        final ArrayList<Contact> contacts = new ArrayList<>();
        contacts.add(new Contact("Sarah Underwood", "604-420-6969", "mobile", "sarah@hotmail.com", testImageURL));
        contacts.add(new Contact("Sarah Underwood", "604-420-6969", "mobile", "sarah@hotmail.com", testImageURL));
        contacts.add(new Contact("Sarah Underwood", "604-420-6969", "mobile", "sarah@hotmail.com", testImageURL));
        contacts.add(new Contact("Sarah Underwood", "604-420-6969", "mobile", "sarah@hotmail.com", testImageURL));
        contacts.add(new Contact("Sarah Underwood", "604-420-6969", "mobile", "sarah@hotmail.com", testImageURL));
        contacts.add(new Contact("Sarah Underwood", "604-420-6969", "mobile", "sarah@hotmail.com", testImageURL));
        contacts.add(new Contact("Sarah Underwood", "604-420-6969", "mobile", "sarah@hotmail.com", testImageURL));
        contacts.add(new Contact("Sarah Underwood", "604-420-6969", "mobile", "sarah@hotmail.com", testImageURL));
        contacts.add(new Contact("Sarah Underwood", "604-420-6969", "mobile", "sarah@hotmail.com", testImageURL));
        contacts.add(new Contact("Sarah Underwood", "604-420-6969", "mobile", "sarah@hotmail.com", testImageURL));
        contacts.add(new Contact("Sarah Underwood", "604-420-6969", "mobile", "sarah@hotmail.com", testImageURL));
        contacts.add(new Contact("Sarah Underwood", "604-420-6969", "mobile", "sarah@hotmail.com", testImageURL));
        contacts.add(new Contact("Sarah Underwood", "604-420-6969", "mobile", "sarah@hotmail.com", testImageURL));
        contacts.add(new Contact("Sarah Underwood", "604-420-6969", "mobile", "sarah@hotmail.com", testImageURL));
        contacts.add(new Contact("Sarah Underwood", "604-420-6969", "mobile", "sarah@hotmail.com", testImageURL));
        contacts.add(new Contact("Sarah Underwood", "604-420-6969", "mobile", "sarah@hotmail.com", testImageURL));
        contacts.add(new Contact("Sarah Underwood", "604-420-6969", "mobile", "sarah@hotmail.com", testImageURL));
        contacts.add(new Contact("Sarah Underwood", "604-420-6969", "mobile", "sarah@hotmail.com", testImageURL));
        contacts.add(new Contact("Sarah Underwood", "604-420-6969", "mobile", "sarah@hotmail.com", testImageURL));
        contacts.add(new Contact("Sarah Underwood", "604-420-6969", "mobile", "sarah@hotmail.com", testImageURL));

        adapter = new ContactListAdapter(getActivity(), R.layout.layout_contactslistitem, contacts, "https://");
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
}





