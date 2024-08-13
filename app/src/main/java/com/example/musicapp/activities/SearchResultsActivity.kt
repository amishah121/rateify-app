package com.example.musicapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.musicapp.R
import com.example.musicapp.fragments.SearchResultsFragment

class SearchResultsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_results)

        if (savedInstanceState == null) {
            val query = intent.getStringExtra("QUERY")
            val accessToken = intent.getStringExtra("ACCESS_TOKEN") // Retrieve the access token

            if (query != null && accessToken != null) {
                val fragment = SearchResultsFragment().apply {
                    arguments = Bundle().apply {
                        putString("QUERY", query)
                        putString("ACCESS_TOKEN", accessToken) // Pass the access token to the fragment
                    }
                }
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit()
            } else {
                // Handle the case where query or accessToken is missing
                // For example, show an error message or finish the activity
                finish()
            }
        }
    }
}
