package com.example.musicapp.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse

class SpotifyAuthActivity : AppCompatActivity() {

    private val CLIENT_ID = "84544f3e69f746c793a2db1f47ecae57"
    private val REDIRECT_URI = "rateify://callback"

    private val REQUEST_CODE = 1337

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Always initiate a new authorization request
        initiateAuthorization()
    }

    private fun initiateAuthorization() {
        val request = AuthorizationRequest.Builder(
            CLIENT_ID,
            AuthorizationResponse.Type.TOKEN,
            REDIRECT_URI
        ).setScopes(arrayOf("user-read-private", "playlist-read"))
            .build()

        AuthorizationClient.openLoginActivity(this, REQUEST_CODE, request)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        if (requestCode == REQUEST_CODE) {
            val response = AuthorizationClient.getResponse(resultCode, intent)

            when (response.type) {
                AuthorizationResponse.Type.TOKEN -> {
                    val accessToken = response.accessToken

                    val callingIntent = Intent()
                    callingIntent.putExtra("ACCESS_TOKEN", accessToken)
                    setResult(RESULT_OK, callingIntent)
                    finish()
                }
                AuthorizationResponse.Type.ERROR -> {
                    setResult(RESULT_CANCELED)
                    finish()
                }
                else -> {
                    setResult(RESULT_CANCELED)
                    finish()
                }
            }
        }
    }
}
