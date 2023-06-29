//package com.example.signin.adapter
//
//import android.content.Context
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.SimpleAdapter
//import android.widget.TextView
//import android.widget.Toast
//import androidx.annotation.IdRes
//import androidx.annotation.LayoutRes
//
//class CustomSimpleAdapter(
//    private val mContext: Context,
//    data: MutableList,
//@LayoutRes
//res: Int,
//from: Array,
//@IdRes
//to: IntArray
//) :
//// Passing these params to SimpleAdapter
//SimpleAdapter(mContext, data , res, from, to) {
//
//    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
//
//        // Get the view in our case list_item.xml
//        val view = super.getView(position, convertView, parent)
//
//        // Getting reference of ImageView that we
//        // have used in our list_item.xml file
//        // so that we can add user defined code
//        val avatarImageView = view.findViewById(R.id.avatarImageView)
//
//        // Reference of TextView which is treated a title
//        val titleTextView = view.findViewById(R.id.titleTextView)
//
//        // Adding an clickEvent to the ImageView, as soon as we click this
//        // ImageView we will see a Toast which will display a message
//        // Note: this event wil only fire when ImageView is pressed and
//        //       not when whole list_item is pressed
//        avatarImageView.setOnClickListener {
//            Toast.makeText(
//                mContext,
//                "Image with title ${titleTextView.text} is pressed",
//                Toast.LENGTH_SHORT
//            ).show()
//        }
//
//        // Finally returning our view
//        return view
//    }
//}