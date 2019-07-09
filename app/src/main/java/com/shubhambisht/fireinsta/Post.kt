package com.shubhambisht.fireinsta

import android.app.Activity
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.custom_layout.view.*
import org.w3c.dom.Comment
import java.security.AccessControlContext
import java.util.ArrayList

class Post(private val userEmail:ArrayList<String> ,
           private val userImage:ArrayList<String>,
           private val userComment:ArrayList<String>,
           private val context:Activity):ArrayAdapter<String>(context,R.layout.custom_layout,userEmail)
{
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
       val layoutInflater = context.layoutInflater
        val customView = layoutInflater.inflate(R.layout.custom_layout,null,true)
        customView.custom_user_name.text = userEmail[position]
        customView.custom_comment.text = userComment[position]
        Picasso.get().load(userImage[position]).into(customView.custom_imageView)
        return customView
    }
}