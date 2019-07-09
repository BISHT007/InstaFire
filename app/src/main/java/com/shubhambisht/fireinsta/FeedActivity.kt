package com.shubhambisht.fireinsta

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_feed.*
import kotlinx.android.synthetic.main.custom_layout.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class FeedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)

        firebaseDatabase = FirebaseDatabase.getInstance()
        myRef = firebaseDatabase?.getReference()
        adapter = Post(useremailfrmFb, userimagefrmFb, usercommentfrmFb, this)
        listView.adapter = adapter
        getdatafromfirebase()
   /**     custom_user_name.text = useremailfrmFb.toString()
        custom_comment.text = usercommentfrmFb.toString()  **/
   }

    var useremailfrmFb: ArrayList<String> = ArrayList<String>()
    var usercommentfrmFb: ArrayList<String> = ArrayList<String>()
    var userimagefrmFb: ArrayList<String> = ArrayList<String>()
    var firebaseDatabase: FirebaseDatabase? = null
    var myRef: DatabaseReference? = null
    var adapter: Post? = null

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.add_posts, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.add_posts) {
            val intent = Intent(this, UploadActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

    fun getdatafromfirebase() {
        val newRef = firebaseDatabase?.getReference("posts")
       newRef?.addValueEventListener(object :ValueEventListener {
           override fun onCancelled(p0: DatabaseError) {

           }

           override fun onDataChange(p0: DataSnapshot) {
           /**    println(p0)
               println("children" + p0.children)
           println("key"+ p0.key)
               println("value"+ p0.value)
            **/
               adapter?.clear()
               userimagefrmFb.clear()
               useremailfrmFb.clear()
               usercommentfrmFb.clear()
               for (snapshot in p0.children)
               {
                   val Hashmap =  snapshot.value as HashMap<String,String>
                   if(Hashmap.size>0) {
                       val email = Hashmap["useremail"]
                       val comment = Hashmap["comment"]
                       val downloadURl = Hashmap["downloadURL"]
                       if (email != null)
                       { useremailfrmFb.add(email) }
                       if (comment != null)
                       { usercommentfrmFb.add(comment) }
                       if (downloadURl != null)
                       { userimagefrmFb.add(downloadURl)
                       Log.d("down","url is "+ userimagefrmFb)
                       }
                       adapter?.notifyDataSetChanged()
                   }
               }
           }
       })
    }
}
