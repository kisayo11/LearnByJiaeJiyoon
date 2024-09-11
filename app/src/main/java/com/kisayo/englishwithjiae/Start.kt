package com.kisayo.englishwithjiae

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kisayo.englishwithjiae.databinding.ActivityStartBinding


class Start : AppCompatActivity() {

    private lateinit var binding: ActivityStartBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // 버튼 클릭 리스너 설정
        binding.btn10.setOnClickListener { toMainActivity(10) }
        binding.btn20.setOnClickListener { toMainActivity(20) }
        binding.btn30.setOnClickListener { toMainActivity(30) }

        binding.questionPage.setOnClickListener {
            startActivity(Intent(this, Question::class.java))
        }

    }

    private fun toMainActivity(progressCount: Int) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("PROGRESS_COUNT", progressCount)
        startActivity(intent)
    }
}