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

            if (query != null) {
                val fragment = SearchResultsFragment().apply {
                    arguments = Bundle().apply {
                        putString("QUERY", query)
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
