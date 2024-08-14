package com.example.musicapp.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import com.spotify.sdk.android.auth.LoginActivity.REQUEST_CODE

class SpotifyAuthActivity : AppCompatActivity() {

//    private val CLIENT_ID = "84544f3e69f746c793a2db1f47ecae57"
//    private val REDIRECT_URI = "rateify://callback"
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        initiateAuthorization()
//    }
//
//    private fun initiateAuthorization() {
//        val request = AuthorizationRequest.Builder(
//            CLIENT_ID,
//            AuthorizationResponse.Type.TOKEN,
//            REDIRECT_URI
//        ).setScopes(arrayOf("user-read-private", "user-top-read", "playlist-read"))
//            .build()
//
//        AuthorizationClient.openLoginActivity(this, REQUEST_CODE, request)
//    }
//
//    @Deprecated("Deprecated in Java")
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == REQUEST_CODE) {
//            val response = AuthorizationClient.getResponse(resultCode, data)
//            Log.d("SpotifyAuthActivity", "Received response type: ${response.type}")
//
//            when (response.type) {
//                AuthorizationResponse.Type.TOKEN -> {
//                    val accessToken = response.accessToken
//                    Log.d("SpotifyAuthActivity", "Access token received: $accessToken")
//
//                    // Pass the access token back to the calling fragment/activity
//                    val callingIntent = Intent().apply {
//                        putExtra("ACCESS_TOKEN", accessToken)
//                    }
//                    setResult(RESULT_OK, callingIntent)
//                    finish()
//                }
//                AuthorizationResponse.Type.ERROR -> {
//                    Log.e("SpotifyAuthActivity", "Error response: ${response.error}")
//                    setResult(RESULT_CANCELED)
//                    finish()
//                }
//                else -> {
//                    Log.e("SpotifyAuthActivity", "Unhandled response type: ${response.type}")
//                    setResult(RESULT_CANCELED)
//                    finish()
//                }
//            }
//        }
//    }
}