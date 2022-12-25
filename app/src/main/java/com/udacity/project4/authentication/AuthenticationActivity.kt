package com.udacity.project4.authentication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.map
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.udacity.project4.R
import com.udacity.project4.databinding.ActivityAuthenticationBinding
import com.udacity.project4.locationreminders.RemindersActivity

/**
 * This class should be the starting point of the app, It asks the users to sign in / register, and redirects the
 * signed in users to the RemindersActivity.
 */
class AuthenticationActivity : AppCompatActivity() {
    private lateinit var binding:ActivityAuthenticationBinding
    private lateinit var firebaseAuth: FirebaseAuth
    companion object{
        const val SIGN_IN_CODE = 132
        private const val TAG = "AuthenticationActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_authentication)
        firebaseAuth = FirebaseAuth(FirebaseApp.getInstance())
        binding.authButton.setOnClickListener {
            if (firebaseAuth.currentUser == null)
                signInFlow()
            else done()
        }
    }

    private fun signInFlow() {
        val providers = arrayListOf(
            AuthUI.IdpConfig
                .EmailBuilder()
                .build(),AuthUI.IdpConfig.GoogleBuilder().build()
        )
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(), SIGN_IN_CODE
        )
    }

    //the function checks for authentication if true he will send the user to reminder fragment
    // if false (not Authenticated) it will through an toast (sign in failed) and Log (debugging purpose)
    override fun onActivityResult(requestingCode: Int, resultingCode: Int, intent: Intent?) {
        super.onActivityResult(requestingCode, resultingCode, intent)
        if (requestingCode == SIGN_IN_CODE) {
            val response = IdpResponse.fromResultIntent(intent)
            if (resultingCode == Activity.RESULT_OK) {
                Toast.makeText(this, "SignIn Successfully", Toast.LENGTH_SHORT).show()
                Log.i(
                    TAG, "Sign in Success ${
                        FirebaseAuth.getInstance().currentUser?.displayName
                    }"
                )
                done()
                finish()
            } else {
                Toast.makeText(this, "SignIn Failed", Toast.LENGTH_SHORT).show()
                Log.i(TAG, "Error in signing in ${response?.error?.errorCode}")
            }
        }
    }
    private fun done() = startActivity(Intent(this, RemindersActivity::class.java))
}
