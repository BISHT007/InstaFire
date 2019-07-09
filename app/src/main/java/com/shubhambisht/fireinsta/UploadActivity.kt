package com.shubhambisht.fireinsta

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_upload.*
import java.net.URL
import java.util.*
import java.util.jar.Manifest

class UploadActivity : AppCompatActivity() {
    var selected:Uri? = null
    var mAuth:FirebaseAuth? = null
    var mAuthListner:FirebaseAuth.AuthStateListener? = null
    var firebaseDatabase: FirebaseDatabase? = null
    var myRef:DatabaseReference? = null
    var firebaseStorage: StorageReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)

        mAuth = FirebaseAuth.getInstance()
        mAuthListner = FirebaseAuth.AuthStateListener {  }
        firebaseDatabase = FirebaseDatabase.getInstance()
        myRef = firebaseDatabase!!.getReference()
        firebaseStorage = FirebaseStorage.getInstance().reference

        upload_image.setOnClickListener {
            if(checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1)
            }else
            {
            val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent,2)
            }
        }
        upload.setOnClickListener {
           val uuid = UUID.randomUUID()
            val imagename = "images/$uuid.jpg"
            val storeRef = firebaseStorage!!.child(imagename)
            storeRef.putFile(selected!!).addOnSuccessListener {
                val downloadurl = storeRef.downloadUrl.toString()
                val user = mAuth!!.currentUser
                val email = user!!.email.toString()
                val comment = comment.text.toString()
                val uuid = UUID.randomUUID()
                val uuidString = uuid.toString()

                myRef!!.child("posts").child(uuidString).child("useremail").setValue(email)
                myRef!!.child("posts").child(uuidString).child("comment").setValue(comment)
                myRef!!.child("posts").child(uuidString).child("downloadURL").setValue(downloadurl)

            }.addOnFailureListener {
                if(it != null)
                {
                 Toast.makeText(applicationContext,it.localizedMessage.toString(),Toast.LENGTH_SHORT).show()
                }
            }.addOnCompleteListener {
                if(it.isSuccessful){
                    Toast.makeText(applicationContext,"post shared",Toast.LENGTH_SHORT).show()
                    val  intent = Intent(applicationContext,FeedActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 1 ){
            if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent,2)
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == 2 && resultCode == Activity.RESULT_OK && data != null){
            selected = data.data

            try {
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,selected)
                upload_image.setImageBitmap(bitmap)
                }
            catch (e:Exception){e.printStackTrace()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
