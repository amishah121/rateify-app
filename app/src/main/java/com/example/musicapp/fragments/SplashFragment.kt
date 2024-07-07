package com.example.musicapp.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.musicapp.R
import com.example.musicapp.activities.SpotifyAuthActivity
import com.google.firebase.auth.FirebaseAuth

class SplashFragment : Fragment() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var navController: NavController

    private val SPOTIFY_AUTH_REQUEST_CODE = 1337

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)

        val handler = Handler(Looper.myLooper()!!)
        handler.postDelayed({
            startSpotifyAuthActivity()
        }, 2000)
    }

    private fun init(view: View) {
        mAuth = FirebaseAuth.getInstance()
        navController = Navigation.findNavController(view)
    }

    private fun startSpotifyAuthActivity() {
        val intent = Intent(requireContext(), SpotifyAuthActivity::class.java)
        startActivityForResult(intent, SPOTIFY_AUTH_REQUEST_CODE)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SPOTIFY_AUTH_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val accessToken = data?.getStringExtra("ACCESS_TOKEN")

            if (!accessToken.isNullOrEmpty()) {
                // Navigate to HomeFragment or perform necessary actions with accessToken
                navController.navigate(R.id.homeFragment)
            } else {
                // Handle case where accessToken is null or empty
                // For example, show an error message or retry authentication
            }
        } else {
            // Handle other result codes if needed
        }
    }
}
