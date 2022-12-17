package com.example.orion.lockerpro

import android.content.Intent

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.renderscript.Sampler.Value
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import android.widget.VideoView
import androidx.biometric.BiometricManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
//import kotlinx.coroutines.DefaultExecutor.key
import java.util.concurrent.Executor

class MainActivity : AppCompatActivity() {
//    var value:String?=null
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    var mutable:Int?=null
    var database = FirebaseDatabase.getInstance()
    var myRef = database.reference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkDeviceHasBiometric()
        read()
        background.setOnClickListener {
        biometricPrompt.authenticate(promptInfo)

         }


        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence,
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(applicationContext,
                        "Authentication error: $errString", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult,
                ) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(
                        applicationContext,
                        "Authentication succeeded!", Toast.LENGTH_SHORT
                    )
                        .show()

                            if (mutable == 1){
                            myRef.child("User").setValue(mutable).addOnSuccessListener {
//                                lockstate.setBackgroundResource(R.drawable.logobackground)

                                    }
                               }
                                else{
                                myRef.child("User").setValue(mutable).addOnSuccessListener {
//                                    lockstate.setBackgroundResource(R.drawable.onbackground)

                                     }
                                }




//                            myRef.child("User").setValue(0)
//                            lockstate.setBackgroundResource(R.drawable.onbackground)



                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(applicationContext, "Authentication failed",
                        Toast.LENGTH_SHORT)
                        .show()
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for my app")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Use account password")
            .build()

    }

    fun read() {
        myRef.child("User").addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                try {
                    var td=snapshot.getValue()
                    if (td.toString()=="0"){
                        lockstate.setBackgroundResource(R.drawable.logobackground)
                        mutable = 1

                    }else
                    {
                        lockstate.setBackgroundResource(R.drawable.onbackground)
                        mutable = 0

                    }
                }catch (ex:Exception){}


//                  value = snapshot.getValue().toString()
//



//                {
//                    try {
//                        var td=dataSnapshot.value as HashMap<String,Any>
//                        if (td!=null){
//                            var value:String
//                            for(key in td.keys){
//                                value=td[key] as String
//                                if (value=="0"){
//                                    ////
//                                }else{
//                                    ////
//                                }
//                                mutalbe=value
//                            }
//                        }
//                    }catch (ex:Exception){}
//                }
            }


            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
//    fun newclick(view: View) {
//        var abcd = textView.text.toString()
//
//    }

    override fun onResume() {
        super.onResume()
        background.setOnPreparedListener { mp -> mp.isLooping = true }
        val videoPath: Uri =
            Uri.parse("android.resource://" + getPackageName().toString() + "/" + R.raw.nnnn)
        background.setVideoURI(videoPath)
        background.start()
    }





    fun checkDeviceHasBiometric() {
        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                Log.d("MY_APP_TAG", "App can authenticate using biometrics.")



            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Log.e("MY_APP_TAG", "No biometric features available on this device.")

                ///executable thing

            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Log.e("MY_APP_TAG", "Biometric features are currently unavailable.")

                ///executable thing

            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                // Prompts the user to create credentials that your app accepts.
                val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                    putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED, BIOMETRIC_STRONG or DEVICE_CREDENTIAL) }


                ///executable thing


                startActivityForResult(enrollIntent, 100)
            }
        }

        //////executable thing
    }
}



