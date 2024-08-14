package com.example.musicapp.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.musicapp.R
import com.example.musicapp.activities.SpotifyAuthActivity
import com.google.firebase.auth.FirebaseAuth

class SplashFragment : Fragment() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var navController: NavController

    private val spotifyAuthLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val accessToken = result.data?.getStringExtra("ACCESS_TOKEN")
            if (!accessToken.isNullOrEmpty()) {
                // Navigate to HomeFragment or perform necessary actions with accessToken
                navController.navigate(R.id.homeFragment)
            } else {
                // Handle case where accessToken is null or empty
            }
        } else {
            // Handle other result codes if needed
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)

        Handler(Looper.getMainLooper()).postDelayed({
            startSpotifyAuthActivity()
        }, 2000)
    }

    private fun init(view: View) {
        mAuth = FirebaseAuth.getInstance()
        navController = Navigation.findNavController(view)
    }

    private fun startSpotifyAuthActivity() {
        val intent = Intent(requireContext(), SpotifyAuthActivity::class.java)
        spotifyAuthLauncher.launch(intent)
    }
}

