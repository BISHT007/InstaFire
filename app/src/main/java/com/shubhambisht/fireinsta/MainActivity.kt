package com.shubhambisht.fireinsta

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    var mAuth: FirebaseAuth? = null
    var mAuthListner: FirebaseAuth.AuthStateListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       /**    val username = name_signup.text.toString()
           val password = password_signup.text.toString() **/

        mAuth = FirebaseAuth.getInstance()
        mAuthListner = FirebaseAuth.AuthStateListener {}
        sign_up.setOnClickListener {

            mAuth?.createUserWithEmailAndPassword(name_signup.text.toString(), password_signup.text.toString())
                ?.addOnCompleteListener {
                    /**   if(!it.isSuccessful) return@addOnCompleteListener
                    else Log.d("sign","user completion UID :${it.result?.user?.uid}")**/
                    if (it.isSuccessful) {
                        Toast.makeText(applicationContext, "user created", Toast.LENGTH_SHORT).show()
                        val intent = Intent(applicationContext,FeedActivity::class.java)
                        startActivity(intent)
                    }
                }?.addOnFailureListener { exception ->
                    if (exception != null)
                        Toast.makeText(
                            applicationContext, exception.localizedMessage.toString(), Toast.LENGTH_SHORT).show()
                }
        }

        /**    mAuth!!.createUserWithEmailAndPassword(username, password).addOnCompleteListener {
        if (!it.isSuccessful) return@addOnCompleteListener
        else Log.d("sign", "user completion UID :${it.result?.user?.uid}")
        } **/

    sign_in.setOnClickListener {
        mAuth!!.signInWithEmailAndPassword(name_signup.text.toString(),password_signup.text.toString())
            .addOnCompleteListener {
                if (it.isSuccessful){
                    val intent = Intent(applicationContext,FeedActivity::class.java)
                    startActivity(intent)
                }
            }.addOnFailureListener {
                Toast.makeText(applicationContext,it.localizedMessage.toString(),Toast.LENGTH_SHORT).show()
            }
    }


    }
}

