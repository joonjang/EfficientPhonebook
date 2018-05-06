package com.example.joon.contactslist.Utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.joon.contactslist.R;
import com.example.joon.contactslist.models.Contact;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by joon on 07/01/18.
 */

public class ContactListAdapter extends ArrayAdapter<Contact> {

    private LayoutInflater mInflater;
    private List<Contact> mContacts = null;
    private ArrayList<Contact> arrayList;   //used for search bar
    private int layoutResource;
    private Context mContext;
    private String mAppend;

    public ContactListAdapter(@NonNull Context context, int resource, @NonNull List<Contact> contacts, String append) {
        super(context, resource, contacts);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutResource = resource;
        this.mContext = context;
        mAppend = append;
        this.mContacts = contacts;
        arrayList = new ArrayList<>();
        this.arrayList.addAll(mContacts);
    }

    // in replacement of recycler view

    //change if more widgets wants to be added
    private static class ViewHolder {
        TextView name;
        CircleImageView contactImage;
        ProgressBar mProgressBar;
        ImageView tabColour;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        /**
         ******* ViewHolder Build Pattern Start *******
         */

        // view holds widget in memory ahead of time for efficiency
        final ViewHolder holder;

        if(convertView == null){
            convertView = mInflater.inflate(layoutResource, parent, false);
            holder = new ViewHolder();

            //------------------ Stuff to change ---------------------------------------------------
            holder.name = (TextView) convertView.findViewById(R.id.contactName);
            holder.contactImage = (CircleImageView) convertView.findViewById(R.id.contactImage);
//            holder.tabColour = (ImageView) convertView.findViewById(R.id.tabColour);
            //--------------------------------------------------------------------------------------

            holder.mProgressBar = (ProgressBar) convertView.findViewById(R.id.contactsProgressBar);

            //used to store view
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        //------------------ Stuff to change ---------------------------------------------------

        String name = getItem(position).getName();
        String imagePath = getItem(position).getProfileimage();
        String tabColour = getItem(position).getTabcolour();
        holder.name.setText(name);

        //Progress bar
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(mAppend + imagePath, holder.contactImage, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                holder.mProgressBar.setVisibility(view.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                holder.mProgressBar.setVisibility(view.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                holder.mProgressBar.setVisibility(view.GONE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                holder.mProgressBar.setVisibility(view.GONE);
            }
        });

        //set custom tab colour
        // REPLACE CIRCLE IMAGE FOR COLOUR?!
//        int color = Color.parseColor(tabColour);
//        ((GradientDrawable)holder.tabColour.getBackground()).setSize(50,50);

        //--------------------------------------------------------------------------------------

        return convertView;
    }

    //Filter class
    public void filter(String characterText){
        characterText = characterText.toLowerCase(Locale.getDefault());
        mContacts.clear();
        if(characterText.length() == 0){
            mContacts.addAll(arrayList);
        }else{
            mContacts.clear();
            for(Contact contact: arrayList){
                if(contact.getName().toLowerCase(Locale.getDefault()).contains(characterText)){
                    mContacts.add(contact);
                }
            }
        }
        notifyDataSetChanged();

    }

}
