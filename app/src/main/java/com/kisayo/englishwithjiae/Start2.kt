package com.kisayo.englishwithjiae

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kisayo.englishwithjiae.databinding.ActivityStart2Binding

class Start2 : AppCompatActivity() {

    private val binding by lazy { ActivityStart2Binding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btn10.setOnClickListener { toMainActivity2(10) }
        binding.btn20.setOnClickListener { toMainActivity2(20) }
        binding.btn30.setOnClickListener { toMainActivity2(30) }

        binding.questionPage.setOnClickListener {
            startActivity(Intent(this, Question::class.java))
        }
    }

    private fun toMainActivity2(progressCount: Int) {
        val intent = Intent(this, MainActivity2::class.java)
        intent.putExtra("PROGRESS_COUNT", progressCount)
        startActivity(intent)
    }

}