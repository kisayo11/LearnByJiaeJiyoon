package com.kisayo.englishwithjiae

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.firestore
import com.kisayo.englishwithjiae.databinding.ActivityQuestionBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Question : AppCompatActivity() {

    private val binding by lazy { ActivityQuestionBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.btnComplete.setOnClickListener { clickComplete() }

    }
    private fun clickComplete(){
        var name=binding.inputLayoutName.editText!!.text.toString()
        var msg=binding.inputLayoutMsg.editText!!.text.toString()

        val data:MutableMap<String,String> = mutableMapOf()
        data["name"]= name
        data["msg"]= msg

        val userRef :CollectionReference=Firebase.firestore.collection("board")
        var n=SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA).format(Date())
        userRef.document(n).set(data).addOnSuccessListener {
            Toast.makeText(this, "글 등록 완료", Toast.LENGTH_SHORT).show()
        }







//        //retrofit으로 세팅 with php, filezilla
//        var retrofit=RetrofitHelper.getRetrofitInstance()
//        val retrofitService = retrofit.create(RetrofitService::class.java)
//
//        val dataPart : MutableMap<String,String> = mutableMapOf()
//        dataPart["name"] = name
//        dataPart["msg"] = msg
//
//
//        val call=retrofitService.postDatatoServer(dataPart)
//        call.enqueue(object:Callback<String>{
//            override fun onResponse(p0: Call<String>, p1: Response<String>) {
//                val s=p1.body()
//                Toast.makeText(this@Question, "$s", Toast.LENGTH_SHORT).show()
//
//                finish()
//            }
//
//            override fun onFailure(p0: Call<String>, p1: Throwable) {
//                Toast.makeText(this@Question, "failed:${p1.message}", Toast.LENGTH_SHORT).show()
//
//            }
//
//        })

    }

}