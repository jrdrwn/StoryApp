package com.dicoding.picodiploma.loginwithanimation.view.detail

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.DetailStory
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityDetailBinding
import com.dicoding.picodiploma.loginwithanimation.loadImage
import com.google.android.material.snackbar.Snackbar

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val story = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra<DetailStory>(EXTRA_STORY)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<DetailStory>(EXTRA_STORY)
        }

        if (story != null) {
            binding.apply {
                progressBar.visibility = View.GONE
                tvDetailName.text = story.name
                tvDetailDescription.text = story.description
                ivDetailPhoto.loadImage(story.photoUrl)
            }

            supportActionBar?.title = getString(R.string.s_story, story.name)
        } else {
            Snackbar.make(
                binding.root,
                getString(R.string.failed_to_load_story), Snackbar.LENGTH_SHORT
            ).show()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    companion object {
        const val EXTRA_STORY = "extra_story"
    }
}